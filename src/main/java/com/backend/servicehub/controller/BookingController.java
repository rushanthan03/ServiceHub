package com.backend.servicehub.controller;

import com.backend.servicehub.dto.request.BookingRequest;
import com.backend.servicehub.dto.response.BookingResponse;
import com.backend.servicehub.dto.response.SimpleResponse;
import com.backend.servicehub.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<SimpleResponse> createBooking(@RequestBody BookingRequest bookingRequest) {
        return bookingService.createBooking(bookingRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SimpleResponse> updateBooking(
            @PathVariable Long id,
            @RequestBody BookingRequest bookingRequest
    ) {
        return bookingService.updateBooking(id, bookingRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SimpleResponse> deleteBooking(@PathVariable Long id) {
        return bookingService.deleteBooking(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id);
    }

    @GetMapping
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @PostMapping("/reserve")
    public ResponseEntity<BookingResponse> reserveSlot(
            @RequestParam("slotId") Long slotId,
            @RequestParam("customerId") Long customerId
    ) {
        return ResponseEntity.ok(bookingService.reserveSlot(slotId, customerId));
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<SimpleResponse> confirmBooking(
            @PathVariable("id") Long bookingId,
            @RequestParam("customerId") Long customerId
    ) {
        bookingService.confirmBooking(bookingId, customerId);
        return ResponseEntity.ok(new SimpleResponse(true, "Booking confirmed successfully"));
    }
}
