package com.backend.servicehub.repository;

import com.backend.servicehub.common.BookingStatus;
import com.backend.servicehub.entity.Availability;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Availability a WHERE a.id = :id")
    Optional<Availability> findByIdForUpdate(@Param("id") Long id);

    boolean existsByServiceProviderIdAndDateAndStartTimeAndEndTime(
            Long serviceProviderId, LocalDate date, LocalTime startTime, LocalTime endTime);

    List<Availability> findByServiceProviderIdOrderByDateAscStartTimeAsc(Long serviceProviderId);

    List<Availability> findByServiceProviderIdAndDateOrderByStartTimeAsc(Long serviceProviderId, LocalDate date);

    List<Availability> findByBookingStatusAndPendingExpiresAtBefore(BookingStatus bookingStatus, LocalDateTime dateTime);
}
