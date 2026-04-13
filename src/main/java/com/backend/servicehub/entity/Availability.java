package com.backend.servicehub.entity;

import com.backend.servicehub.common.BookingStatus;
import com.backend.servicehub.common.Days;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Availability extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    private LocalDateTime pendingExpiresAt;

    @Version
    private Long version;
    private boolean isAvailable;
    
    @ManyToOne
    @JoinColumn(name = "provider_id")
    private ServiceProvider serviceProvider;

}
