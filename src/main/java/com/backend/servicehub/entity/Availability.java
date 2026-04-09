package com.backend.servicehub.entity;

import com.backend.servicehub.common.Days;
import jakarta.persistence.*;
import lombok.*;

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
    @Enumerated(EnumType.STRING)
    private Days day;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean isAvailable;
    
    @ManyToOne
    @JoinColumn(name = "provider_id")
    private ServiceProvider serviceProvider;

}
