package com.backend.servicehub.controller;

import com.backend.servicehub.dto.request.AvailabilityRequest;
import com.backend.servicehub.dto.response.AvailabilityResponse;
import com.backend.servicehub.dto.response.SimpleResponse;
import com.backend.servicehub.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/availabilities")
public class AvailabilityController {
    private final AvailabilityService availabilityService;

    @PostMapping
    public ResponseEntity<SimpleResponse> createAvailability(@RequestBody AvailabilityRequest availabilityRequest) {
        return availabilityService.createAvailability(availabilityRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SimpleResponse> updateAvailability(
            @PathVariable Long id,
            @RequestBody AvailabilityRequest availabilityRequest
    ) {
        return availabilityService.updateAvailability(id, availabilityRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SimpleResponse> deleteAvailability(@PathVariable Long id) {
        return availabilityService.deleteAvailability(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvailabilityResponse> getAvailabilityById(@PathVariable Long id) {
        return availabilityService.getById(id);
    }

    @GetMapping("/provider/{serviceProviderId}")
    public ResponseEntity<List<AvailabilityResponse>> getAvailabilityByProvider(
            @PathVariable Long serviceProviderId,
            @RequestParam(value = "date", required = false) LocalDate date
    ) {
        return availabilityService.getByServiceProvider(serviceProviderId, date);
    }
}
