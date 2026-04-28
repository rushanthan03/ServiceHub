package com.backend.servicehub.dto.request;

import com.backend.servicehub.common.BookingStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingRequest {
    private Long availabilityId;
    private Long customerId;
    private BookingStatus bookingStatus;
    private String reason;
    private String location;
    private String contactNo;
    private String optionalContactNo;
    private LocalDate bookingDate;
    private String reference;
}
