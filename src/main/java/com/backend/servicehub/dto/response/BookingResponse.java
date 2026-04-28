package com.backend.servicehub.dto.response;

import com.backend.servicehub.common.BookingStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BookingResponse {
    private Long id;
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
