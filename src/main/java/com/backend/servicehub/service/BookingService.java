package com.backend.servicehub.service;

import com.backend.servicehub.dto.request.BookingRequest;
import com.backend.servicehub.dto.response.BookingResponse;
import com.backend.servicehub.dto.response.SimpleResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BookingService {
    ResponseEntity<SimpleResponse> createBooking(BookingRequest bookingRequest);

    ResponseEntity<SimpleResponse> updateBooking(Long id, BookingRequest bookingRequest);

    ResponseEntity<SimpleResponse> deleteBooking(Long id);

    ResponseEntity<BookingResponse> getBookingById(Long id);

    ResponseEntity<List<BookingResponse>> getAllBookings();

    BookingResponse reserveSlot(Long slotId, Long customerId);

    void confirmBooking(Long bookingId, Long customerId);
}
