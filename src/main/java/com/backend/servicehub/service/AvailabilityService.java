package com.backend.servicehub.service;

import com.backend.servicehub.dto.request.AvailabilityRequest;
import com.backend.servicehub.dto.response.AvailabilityResponse;
import com.backend.servicehub.dto.response.SimpleResponse;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface AvailabilityService {
    ResponseEntity<SimpleResponse> createAvailability(AvailabilityRequest availabilityRequest);

    ResponseEntity<SimpleResponse> updateAvailability(Long id, AvailabilityRequest availabilityRequest);

    ResponseEntity<SimpleResponse> deleteAvailability(Long id);

    ResponseEntity<List<AvailabilityResponse>> getByServiceProvider(Long serviceProviderId, LocalDate date);

    ResponseEntity<AvailabilityResponse> getById(Long id);
}
