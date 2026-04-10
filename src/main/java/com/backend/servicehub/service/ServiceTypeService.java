package com.backend.servicehub.service;


import com.backend.servicehub.dto.request.ServiceTypeRequest;
import com.backend.servicehub.dto.response.PaginatedResponse;
import com.backend.servicehub.dto.response.ServiceTypeResponse;
import com.backend.servicehub.dto.response.SimpleResponse;
import org.springframework.http.ResponseEntity;

public interface ServiceTypeService {
    ResponseEntity<SimpleResponse> saveServiceTypeDetails(ServiceTypeRequest cityRequest);

    ResponseEntity<ServiceTypeResponse> getServiceTypeById(Long id);

    ResponseEntity<SimpleResponse> deleteServiceType(Long id);

    ResponseEntity<SimpleResponse> updateServiceType(Long id, ServiceTypeRequest serviceTypeRequest);

    PaginatedResponse filterServiceType(String search, Integer pageSize, Integer pageCount);

}
