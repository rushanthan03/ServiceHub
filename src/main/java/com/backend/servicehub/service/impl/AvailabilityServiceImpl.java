package com.backend.servicehub.service.impl;

import com.backend.servicehub.common.BookingStatus;
import com.backend.servicehub.dto.request.AvailabilityRequest;
import com.backend.servicehub.dto.response.AvailabilityResponse;
import com.backend.servicehub.dto.response.SimpleResponse;
import com.backend.servicehub.entity.Availability;
import com.backend.servicehub.entity.ServiceProvider;
import com.backend.servicehub.repository.AvailabilityRepository;
import com.backend.servicehub.repository.ServiceProviderRepository;
import com.backend.servicehub.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService {
    private final AvailabilityRepository availabilityRepository;
    private final ServiceProviderRepository serviceProviderRepository;

    @Override
    public ResponseEntity<SimpleResponse> createAvailability(AvailabilityRequest availabilityRequest) {
        SimpleResponse response = validateAvailabilityRequest(availabilityRequest);
        if (!response.isSuccess()) {
            return ResponseEntity.ok(response);
        }

        Long providerId = availabilityRequest.getServiceProviderId();
        Optional<ServiceProvider> serviceProviderOptional = serviceProviderRepository.findById(providerId);
        if (serviceProviderOptional.isEmpty()) {
            return ResponseEntity.ok(new SimpleResponse(false, "Service provider not found"));
        }

        boolean slotExists = availabilityRepository.existsByServiceProviderIdAndDateAndStartTimeAndEndTime(
                providerId,
                availabilityRequest.getDate(),
                availabilityRequest.getStartTime(),
                availabilityRequest.getEndTime()
        );
        if (slotExists) {
            return ResponseEntity.ok(new SimpleResponse(false, "Availability slot already exists"));
        }

        Availability availability = Availability.builder()
                .serviceProvider(serviceProviderOptional.get())
                .date(availabilityRequest.getDate())
                .startTime(availabilityRequest.getStartTime())
                .endTime(availabilityRequest.getEndTime())
                .bookingStatus(BookingStatus.AVAILABLE)
                .isAvailable(availabilityRequest.getAvailable() == null || availabilityRequest.getAvailable())
                .build();

        availabilityRepository.save(availability);
        return ResponseEntity.ok(new SimpleResponse(true, "Availability created successfully"));
    }

    @Override
    public ResponseEntity<SimpleResponse> updateAvailability(Long id, AvailabilityRequest availabilityRequest) {
        SimpleResponse response = validateAvailabilityRequest(availabilityRequest);
        if (!response.isSuccess()) {
            return ResponseEntity.ok(response);
        }

        Optional<Availability> availabilityOptional = availabilityRepository.findById(id);
        if (availabilityOptional.isEmpty()) {
            return ResponseEntity.ok(new SimpleResponse(false, "Availability not found"));
        }

        Optional<ServiceProvider> serviceProviderOptional = serviceProviderRepository.findById(
                availabilityRequest.getServiceProviderId()
        );
        if (serviceProviderOptional.isEmpty()) {
            return ResponseEntity.ok(new SimpleResponse(false, "Service provider not found"));
        }

        Availability existingAvailability = availabilityOptional.get();
        existingAvailability.setServiceProvider(serviceProviderOptional.get());
        existingAvailability.setDate(availabilityRequest.getDate());
        existingAvailability.setStartTime(availabilityRequest.getStartTime());
        existingAvailability.setEndTime(availabilityRequest.getEndTime());
        existingAvailability.setAvailable(availabilityRequest.getAvailable() == null || availabilityRequest.getAvailable());
        if (existingAvailability.isAvailable()) {
            existingAvailability.setBookingStatus(BookingStatus.AVAILABLE);
        }

        availabilityRepository.save(existingAvailability);
        return ResponseEntity.ok(new SimpleResponse(true, "Availability updated successfully"));
    }

    @Override
    public ResponseEntity<SimpleResponse> deleteAvailability(Long id) {
        if (!availabilityRepository.existsById(id)) {
            return ResponseEntity.ok(new SimpleResponse(false, "Availability not found"));
        }

        availabilityRepository.deleteById(id);
        return ResponseEntity.ok(new SimpleResponse(true, "Availability deleted successfully"));
    }

    @Override
    public ResponseEntity<List<AvailabilityResponse>> getByServiceProvider(Long serviceProviderId, LocalDate date) {
        List<Availability> availabilities;
        if (date == null) {
            availabilities = availabilityRepository.findByServiceProviderIdOrderByDateAscStartTimeAsc(serviceProviderId);
        } else {
            availabilities = availabilityRepository.findByServiceProviderIdAndDateOrderByStartTimeAsc(serviceProviderId, date);
        }

        List<AvailabilityResponse> response = availabilities.stream()
                .map(this::mapToAvailabilityResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<AvailabilityResponse> getById(Long id) {
        Optional<Availability> availabilityOptional = availabilityRepository.findById(id);
        return availabilityOptional.map(availability -> ResponseEntity.ok(mapToAvailabilityResponse(availability))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    private AvailabilityResponse mapToAvailabilityResponse(Availability availability) {
        return AvailabilityResponse.builder()
                .id(availability.getId())
                .serviceProviderId(
                        availability.getServiceProvider() != null ? availability.getServiceProvider().getId() : null
                )
                .date(availability.getDate())
                .startTime(availability.getStartTime())
                .endTime(availability.getEndTime())
                .bookingStatus(availability.getBookingStatus())
                .available(availability.isAvailable())
                .build();
    }

    private SimpleResponse validateAvailabilityRequest(AvailabilityRequest request) {
        if (request.getServiceProviderId() == null) {
            return new SimpleResponse(false, "Service provider id is required");
        }
        if (request.getDate() == null) {
            return new SimpleResponse(false, "Availability date is required");
        }
        if (request.getStartTime() == null || request.getEndTime() == null) {
            return new SimpleResponse(false, "Start time and end time are required");
        }
        if (!request.getEndTime().isAfter(request.getStartTime())) {
            return new SimpleResponse(false, "End time must be after start time");
        }
        return new SimpleResponse(true, "Valid request");
    }
}
