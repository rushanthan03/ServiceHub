package com.backend.servicehub.entity;

import com.backend.servicehub.common.BookingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Booking extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;
    private String reason;
    private String location;
    private String contactNo;
    private String optionalContactNo;
    private LocalDate bookingDate;
    private String reference;

    @ManyToOne
    @JoinColumn(name = "availability_id")
    private Availability availability;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

}
