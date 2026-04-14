package com.backend.servicehub.controller;

import com.backend.servicehub.dto.request.ServiceTypeRequest;
import com.backend.servicehub.dto.response.PaginatedResponse;
import com.backend.servicehub.dto.response.ServiceTypeResponse;
import com.backend.servicehub.dto.response.SimpleResponse;
import com.backend.servicehub.service.ServiceTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/serviceTypes")
public class ServiceTypeController {
    private final ServiceTypeService serviceTypeService;

    @PostMapping
    public ResponseEntity<SimpleResponse> saveServiceTypeDetails(@RequestBody ServiceTypeRequest serviceTypeRequest) {
        return serviceTypeService.saveServiceTypeDetails(serviceTypeRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceTypeResponse> getServiceTypeById(@PathVariable Long id) {
        return serviceTypeService.getServiceTypeById(id);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SimpleResponse> deleteServiceType(@PathVariable Long id) {
        return serviceTypeService.deleteServiceType(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SimpleResponse> updateServiceType(
            @PathVariable("id") Long id, @RequestBody ServiceTypeRequest serviceTypeRequest) {
        return serviceTypeService.updateServiceType(id, serviceTypeRequest);
    }

    @GetMapping("/filter")
    public ResponseEntity<PaginatedResponse> filterServiceType(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageCount", required = false, defaultValue = "1") Integer pageCount) {
        PaginatedResponse response = serviceTypeService.filterServiceType(search, pageSize, pageCount);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/update-service_image")
    public ResponseEntity<SimpleResponse> saveServiceImage(
            @RequestParam(value = "id") Long id,
            @RequestParam(value = "image") MultipartFile image
    ) {
        return serviceTypeService.serviceTypeImage(id, image);
    }

}