package com.backend.servicehub.dto.response;

import com.backend.servicehub.common.BookingStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class AvailabilityResponse {
    private Long id;
    private Long serviceProviderId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private BookingStatus bookingStatus;
    private boolean available;
}
