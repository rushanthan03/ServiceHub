package com.backend.servicehub.service.impl;

import com.backend.servicehub.common.BookingStatus;
import com.backend.servicehub.dto.request.BookingRequest;
import com.backend.servicehub.dto.response.BookingResponse;
import com.backend.servicehub.dto.response.SimpleResponse;
import com.backend.servicehub.entity.Availability;
import com.backend.servicehub.entity.Booking;
import com.backend.servicehub.entity.User;
import com.backend.servicehub.repository.AvailabilityRepository;
import com.backend.servicehub.repository.BookingRepository;
import com.backend.servicehub.repository.UserRepository;
import com.backend.servicehub.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final AvailabilityRepository availabilityRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<SimpleResponse> createBooking(BookingRequest bookingRequest) {
        Booking booking = mapToBooking(bookingRequest, Booking.builder().build(), false);
        if (booking.getBookingStatus() == null) {
            booking.setBookingStatus(BookingStatus.PENDING);
        }
        bookingRepository.save(booking);
        return ResponseEntity.ok(new SimpleResponse(true, "Booking created successfully"));
    }

    @Override
    public ResponseEntity<SimpleResponse> updateBooking(Long id, BookingRequest bookingRequest) {
        Optional<Booking> bookingOptional = bookingRepository.findById(id);
        if (bookingOptional.isEmpty()) {
            return ResponseEntity.ok(new SimpleResponse(false, "Booking not found"));
        }

        Booking existingBooking = mapToBooking(bookingRequest, bookingOptional.get(), true);
        bookingRepository.save(existingBooking);
        return ResponseEntity.ok(new SimpleResponse(true, "Booking updated successfully"));
    }

    @Override
    public ResponseEntity<SimpleResponse> deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            return ResponseEntity.ok(new SimpleResponse(false, "Booking not found"));
        }
        bookingRepository.deleteById(id);
        return ResponseEntity.ok(new SimpleResponse(true, "Booking deleted successfully"));
    }

    @Override
    public ResponseEntity<BookingResponse> getBookingById(Long id) {
        Optional<Booking> bookingOptional = bookingRepository.findById(id);
        return bookingOptional.map(booking -> ResponseEntity.ok(mapToBookingResponse(booking))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        List<BookingResponse> bookings = bookingRepository.findAll().stream()
                .map(this::mapToBookingResponse)
                .toList();
        return ResponseEntity.ok(bookings);
    }

    @Override
    @Transactional
    public BookingResponse reserveSlot(Long slotId, Long customerId) {
        Availability slot = availabilityRepository.findByIdForUpdate(slotId)
                .orElseThrow(() -> new IllegalArgumentException("Slot not found"));

        if (slot.getBookingStatus() != BookingStatus.AVAILABLE || !slot.isAvailable()) {
            throw new IllegalStateException("Slot is no longer available");
        }

        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        slot.setBookingStatus(BookingStatus.PENDING);
        slot.setPendingExpiresAt(LocalDateTime.now().plusMinutes(15));
        slot.setAvailable(false);
        availabilityRepository.save(slot);

        Booking booking = Booking.builder()
                .availability(slot)
                .customer(customer)
                .bookingStatus(BookingStatus.PENDING)
                .bookingDate(slot.getDate())
                .build();

        return mapToBookingResponse(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public void confirmBooking(Long bookingId, Long customerId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (booking.getCustomer() == null || !booking.getCustomer().getId().equals(customerId)) {
            throw new IllegalStateException("Booking does not belong to customer");
        }

        if (booking.getBookingStatus() != BookingStatus.PENDING) {
            throw new IllegalStateException("Booking is not in PENDING state");
        }

        Availability slot = booking.getAvailability();
        if (slot == null) {
            throw new IllegalStateException("Booking does not have an assigned slot");
        }

        if (slot.getPendingExpiresAt() != null && LocalDateTime.now().isAfter(slot.getPendingExpiresAt())) {
            slot.setBookingStatus(BookingStatus.AVAILABLE);
            slot.setPendingExpiresAt(null);
            slot.setAvailable(true);
            booking.setBookingStatus(BookingStatus.EXPIRED);
            availabilityRepository.save(slot);
            bookingRepository.save(booking);
            throw new IllegalStateException("Reservation expired, please select again");
        }

        booking.setBookingStatus(BookingStatus.CONFIRMED);
        slot.setBookingStatus(BookingStatus.CONFIRMED);
        slot.setPendingExpiresAt(null);
        slot.setAvailable(false);
        availabilityRepository.save(slot);
        bookingRepository.save(booking);
    }

    @Scheduled(fixedDelay = 60_000)
    @Transactional
    public void releaseExpiredSlots() {
        List<Availability> expiredSlots = availabilityRepository.findByBookingStatusAndPendingExpiresAtBefore(
                BookingStatus.PENDING,
                LocalDateTime.now()
        );

        expiredSlots.forEach(slot -> {
            slot.setBookingStatus(BookingStatus.AVAILABLE);
            slot.setPendingExpiresAt(null);
            slot.setAvailable(true);
        });
        availabilityRepository.saveAll(expiredSlots);
    }

    private Booking mapToBooking(BookingRequest request, Booking booking, boolean partialUpdate) {
        if (request.getAvailabilityId() != null) {
            Availability availability = availabilityRepository.findById(request.getAvailabilityId())
                    .orElseThrow(() -> new IllegalArgumentException("Availability not found"));
            booking.setAvailability(availability);
        } else if (!partialUpdate) {
            booking.setAvailability(null);
        }

        if (request.getCustomerId() != null) {
            User customer = userRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
            booking.setCustomer(customer);
        } else if (!partialUpdate) {
            booking.setCustomer(null);
        }

        booking.setBookingStatus(request.getBookingStatus());
        booking.setReason(request.getReason());
        booking.setLocation(request.getLocation());
        booking.setContactNo(request.getContactNo());
        booking.setOptionalContactNo(request.getOptionalContactNo());
        booking.setBookingDate(request.getBookingDate());
        booking.setReference(request.getReference());
        return booking;
    }

    private BookingResponse mapToBookingResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .availabilityId(booking.getAvailability() != null ? booking.getAvailability().getId() : null)
                .customerId(booking.getCustomer() != null ? booking.getCustomer().getId() : null)
                .bookingStatus(booking.getBookingStatus())
                .reason(booking.getReason())
                .location(booking.getLocation())
                .contactNo(booking.getContactNo())
                .optionalContactNo(booking.getOptionalContactNo())
                .bookingDate(booking.getBookingDate())
                .reference(booking.getReference())
                .build();
    }
}
