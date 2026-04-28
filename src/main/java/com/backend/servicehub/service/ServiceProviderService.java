package com.backend.servicehub.service;

import com.backend.servicehub.dto.request.ServiceProviderRequest;
import com.backend.servicehub.dto.response.ServiceProviderResponse;
import com.backend.servicehub.dto.response.SimpleResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ServiceProviderService {
    ResponseEntity<SimpleResponse> createServiceProvider(ServiceProviderRequest serviceProviderRequest);

    ResponseEntity<SimpleResponse> updateServiceProvider(Long id, ServiceProviderRequest serviceProviderRequest);

    ResponseEntity<SimpleResponse> deleteServiceProvider(Long id);

    ResponseEntity<ServiceProviderResponse> getServiceProviderById(Long id);

    ResponseEntity<List<ServiceProviderResponse>> getAllServiceProviders();
}
