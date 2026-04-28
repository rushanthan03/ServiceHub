package com.backend.servicehub.service.impl;

import com.backend.servicehub.dto.request.ServiceProviderRequest;
import com.backend.servicehub.dto.response.ServiceProviderResponse;
import com.backend.servicehub.dto.response.SimpleResponse;
import com.backend.servicehub.entity.ServiceProvider;
import com.backend.servicehub.entity.ServiceType;
import com.backend.servicehub.entity.User;
import com.backend.servicehub.repository.ServiceProviderRepository;
import com.backend.servicehub.repository.ServiceRepository;
import com.backend.servicehub.repository.UserRepository;
import com.backend.servicehub.service.ServiceProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServiceProviderServiceImpl implements ServiceProviderService {
    private final ServiceProviderRepository serviceProviderRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;

    @Override
    public ResponseEntity<SimpleResponse> createServiceProvider(ServiceProviderRequest serviceProviderRequest) {
        SimpleResponse validation = validateServiceProviderRequest(serviceProviderRequest);
        if (!validation.isSuccess()) {
            return ResponseEntity.ok(validation);
        }

        Optional<User> userOptional = userRepository.findById(serviceProviderRequest.getUserId());
        if (userOptional.isEmpty()) {
            return ResponseEntity.ok(new SimpleResponse(false, "User not found"));
        }

        List<ServiceType> serviceTypes = getServiceTypes(serviceProviderRequest.getServiceTypeIds());
        if (!serviceProviderRequest.getServiceTypeIds().isEmpty() && serviceTypes.size() != serviceProviderRequest.getServiceTypeIds().size()) {
            return ResponseEntity.ok(new SimpleResponse(false, "One or more service types were not found"));
        }

        ServiceProvider serviceProvider = ServiceProvider.builder()
                .name(serviceProviderRequest.getName())
                .user(userOptional.get())
                .serviceTypes(serviceTypes)
                .build();
        serviceProviderRepository.save(serviceProvider);

        return ResponseEntity.ok(new SimpleResponse(true, "Service provider created successfully"));
    }

    @Override
    public ResponseEntity<SimpleResponse> updateServiceProvider(Long id, ServiceProviderRequest serviceProviderRequest) {
        SimpleResponse validation = validateServiceProviderRequest(serviceProviderRequest);
        if (!validation.isSuccess()) {
            return ResponseEntity.ok(validation);
        }

        Optional<ServiceProvider> existingProviderOptional = serviceProviderRepository.findById(id);
        if (existingProviderOptional.isEmpty()) {
            return ResponseEntity.ok(new SimpleResponse(false, "Service provider not found"));
        }

        Optional<User> userOptional = userRepository.findById(serviceProviderRequest.getUserId());
        if (userOptional.isEmpty()) {
            return ResponseEntity.ok(new SimpleResponse(false, "User not found"));
        }

        List<ServiceType> serviceTypes = getServiceTypes(serviceProviderRequest.getServiceTypeIds());
        if (!serviceProviderRequest.getServiceTypeIds().isEmpty() && serviceTypes.size() != serviceProviderRequest.getServiceTypeIds().size()) {
            return ResponseEntity.ok(new SimpleResponse(false, "One or more service types were not found"));
        }

        ServiceProvider existingProvider = existingProviderOptional.get();
        existingProvider.setName(serviceProviderRequest.getName());
        existingProvider.setUser(userOptional.get());
        existingProvider.setServiceTypes(serviceTypes);
        serviceProviderRepository.save(existingProvider);

        return ResponseEntity.ok(new SimpleResponse(true, "Service provider updated successfully"));
    }

    @Override
    public ResponseEntity<SimpleResponse> deleteServiceProvider(Long id) {
        if (!serviceProviderRepository.existsById(id)) {
            return ResponseEntity.ok(new SimpleResponse(false, "Service provider not found"));
        }
        serviceProviderRepository.deleteById(id);
        return ResponseEntity.ok(new SimpleResponse(true, "Service provider deleted successfully"));
    }

    @Override
    public ResponseEntity<ServiceProviderResponse> getServiceProviderById(Long id) {
        Optional<ServiceProvider> providerOptional = serviceProviderRepository.findById(id);
        return providerOptional.map(serviceProvider -> ResponseEntity.ok(mapToServiceProviderResponse(serviceProvider))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<ServiceProviderResponse>> getAllServiceProviders() {
        List<ServiceProviderResponse> providers = serviceProviderRepository.findAll().stream()
                .map(this::mapToServiceProviderResponse)
                .toList();
        return ResponseEntity.ok(providers);
    }

    private List<ServiceType> getServiceTypes(List<Long> serviceTypeIds) {
        if (serviceTypeIds == null || serviceTypeIds.isEmpty()) {
            return Collections.emptyList();
        }
        return serviceRepository.findAllById(serviceTypeIds);
    }

    private SimpleResponse validateServiceProviderRequest(ServiceProviderRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            return new SimpleResponse(false, "Service provider name is required");
        }
        if (request.getUserId() == null) {
            return new SimpleResponse(false, "User id is required");
        }
        if (request.getServiceTypeIds() == null) {
            request.setServiceTypeIds(Collections.emptyList());
        }
        return new SimpleResponse(true, "Valid request");
    }

    private ServiceProviderResponse mapToServiceProviderResponse(ServiceProvider serviceProvider) {
        List<Long> serviceTypeIds = serviceProvider.getServiceTypes() == null
                ? Collections.emptyList()
                : serviceProvider.getServiceTypes().stream().map(ServiceType::getId).toList();

        return ServiceProviderResponse.builder()
                .id(serviceProvider.getId())
                .name(serviceProvider.getName())
                .userId(serviceProvider.getUser() != null ? serviceProvider.getUser().getId() : null)
                .userEmail(serviceProvider.getUser() != null ? serviceProvider.getUser().getEmail() : null)
                .serviceTypeIds(serviceTypeIds)
                .build();
    }
}
