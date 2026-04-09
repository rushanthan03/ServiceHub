package com.backend.servicehub.entity;

import com.backend.servicehub.common.BookingStatus;
import com.backend.servicehub.common.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Payment extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    private String transactionId;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;


}
