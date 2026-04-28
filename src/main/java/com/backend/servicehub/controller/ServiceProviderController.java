package com.backend.servicehub.controller;

import com.backend.servicehub.dto.request.ServiceProviderRequest;
import com.backend.servicehub.dto.response.ServiceProviderResponse;
import com.backend.servicehub.dto.response.SimpleResponse;
import com.backend.servicehub.service.ServiceProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/serviceProviders")
public class ServiceProviderController {
    private final ServiceProviderService serviceProviderService;

    @PostMapping
    public ResponseEntity<SimpleResponse> createServiceProvider(@RequestBody ServiceProviderRequest request) {
        return serviceProviderService.createServiceProvider(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SimpleResponse> updateServiceProvider(
            @PathVariable Long id,
            @RequestBody ServiceProviderRequest request
    ) {
        return serviceProviderService.updateServiceProvider(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SimpleResponse> deleteServiceProvider(@PathVariable Long id) {
        return serviceProviderService.deleteServiceProvider(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceProviderResponse> getServiceProviderById(@PathVariable Long id) {
        return serviceProviderService.getServiceProviderById(id);
    }

    @GetMapping
    public ResponseEntity<List<ServiceProviderResponse>> getAllServiceProviders() {
        return serviceProviderService.getAllServiceProviders();
    }
}
