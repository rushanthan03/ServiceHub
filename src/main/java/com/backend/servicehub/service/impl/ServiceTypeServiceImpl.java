package com.backend.servicehub.service.impl;

import com.backend.servicehub.dto.request.ServiceTypeRequest;
import com.backend.servicehub.dto.response.PaginatedResponse;
import com.backend.servicehub.dto.response.ServiceTypeResponse;
import com.backend.servicehub.dto.response.SimpleResponse;
import com.backend.servicehub.entity.ServiceType;
import com.backend.servicehub.repository.ServiceRepository;
import com.backend.servicehub.service.ServiceTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ServiceTypeServiceImpl implements ServiceTypeService {
    private final ServiceRepository serviceRepository;

    @Override
    public ResponseEntity<SimpleResponse> saveServiceTypeDetails(ServiceTypeRequest serviceTypeRequest) {
        SimpleResponse response = new SimpleResponse(false, "");
        if (serviceRepository.existsByName(serviceTypeRequest.getName())) {
            response.setMessage("Service name already exists");
        } else {
            ServiceType serviceType = ServiceType.builder()
                    .name(serviceTypeRequest.getName())
                    .build();
            serviceRepository.save(serviceType);

            response.setSuccess(true);
            response.setMessage("Service details created successfully");
        }
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ServiceTypeResponse> getServiceTypeById(Long id) {
        Optional<ServiceType> serviceOptional = serviceRepository.findById(id);
        if (serviceOptional.isPresent()) {
            ServiceType serviceType = serviceOptional.get();
            ServiceTypeResponse serviceResponse = mapToServiceResponse(serviceType);
            return ResponseEntity.ok(serviceResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<SimpleResponse> deleteServiceType(Long id) {
        SimpleResponse response = new SimpleResponse(false, "");
        if (serviceRepository.existsById(id)) {
            serviceRepository.deleteById(id);
            response.setSuccess(true);
            response.setMessage("Service deleted successfully");
        } else {
            response.setMessage("Service not found");
        }
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<SimpleResponse> updateServiceType(Long id, ServiceTypeRequest serviceTypeRequest) {
        SimpleResponse response = new SimpleResponse(false, "");

        Optional<ServiceType> existingServiceOptional = serviceRepository.findById(id);
        if (existingServiceOptional.isPresent()) {
            ServiceType existingServiceType = existingServiceOptional.get();
            existingServiceType.setName(serviceTypeRequest.getName());
            serviceRepository.save(existingServiceType);
            response.setSuccess(true);
            response.setMessage("Service updated successfully");
        } else {
            response.setMessage("Service not found");
        }
        return ResponseEntity.ok(response);
    }

    private ServiceTypeResponse mapToServiceResponse(ServiceType serviceType) {
        return ServiceTypeResponse.builder()
                .id(serviceType.getId())
                .name(serviceType.getName())
                .build();
    }

    @Override
    public PaginatedResponse filterServiceType(String search, Integer pageSize, Integer pageCount) {
        if (pageCount < 1) {
            throw new IllegalArgumentException("Page count must be 1 or greater.");
        }
        Pageable paging = PageRequest.of(pageCount - 1, pageSize, Sort.by("id").descending());
        Page<ServiceType> servicePage = serviceRepository.filterService(search, paging);

        List<ServiceType> serviceTypeList = servicePage.getContent();
        long totalRecords = servicePage.getTotalElements();
        int totalPages = servicePage.getTotalPages();
        int pageNumber = servicePage.getNumber() + 1;

        List<ServiceTypeResponse> serviceResponses = serviceTypeList.stream()
                .map(this::mapToServiceResponse)
                .toList();

        return PaginatedResponse.builder()
                .totalRecords(totalRecords)
                .totalPages(totalPages)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .records(serviceResponses)
                .build();
    }


}
