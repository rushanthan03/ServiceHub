package com.backend.servicehub.Service;

import com.backend.servicehub.dto.request.ServiceTypeRequest;
import com.backend.servicehub.dto.response.PaginatedResponse;
import com.backend.servicehub.dto.response.ServiceTypeResponse;
import com.backend.servicehub.dto.response.SimpleResponse;
import com.backend.servicehub.entity.ServiceType;
import com.backend.servicehub.repository.ServiceRepository;
import com.backend.servicehub.service.impl.ServiceTypeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceTypeServiceImpTest {

    @Mock
    private ServiceRepository serviceRepository;

    @InjectMocks
    private ServiceTypeServiceImpl service;

    private ServiceType serviceType;

    @BeforeEach
    void setUp() {
        serviceType = ServiceType.builder()
                .id(1L)
                .name("Plumbing")
                .build();
    }


    // ---------------- SAVE ----------------

    @Test
    void saveServiceTypeDetails_shouldReturnFailure_whenNameExists() {
        ServiceTypeRequest request = new ServiceTypeRequest();
        request.setName("Plumbing");

        when(serviceRepository.existsByName("Plumbing")).thenReturn(true);

        SimpleResponse response =
                service.saveServiceTypeDetails(request).getBody();

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("Service name already exists", response.getMessage());
    }

    @Test
    void saveServiceTypeDetails_shouldSaveSuccessfully() {
        ServiceTypeRequest request = new ServiceTypeRequest();
        request.setName("Plumbing");

        when(serviceRepository.existsByName("Plumbing")).thenReturn(false);

        SimpleResponse response =
                service.saveServiceTypeDetails(request).getBody();

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Service details created successfully", response.getMessage());

        verify(serviceRepository, times(1)).save(any(ServiceType.class));
    }

    // ---------------- GET BY ID ----------------

    @Test
    void getServiceTypeById_shouldReturnService_whenExists() {
        when(serviceRepository.findById(1L))
                .thenReturn(Optional.of(serviceType));

        ServiceTypeResponse response =
                service.getServiceTypeById(1L).getBody();

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Plumbing", response.getName());
    }

    // ---------------- DELETE ----------------

    @Test
    void deleteServiceType_shouldDeleteSuccessfully() {
        when(serviceRepository.existsById(1L)).thenReturn(true);

        SimpleResponse response =
                service.deleteServiceType(1L).getBody();

        assertTrue(response.isSuccess());
        assertEquals("Service deleted successfully", response.getMessage());

        verify(serviceRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteServiceType_shouldFail_whenNotFound() {
        when(serviceRepository.existsById(1L)).thenReturn(false);

        SimpleResponse response =
                service.deleteServiceType(1L).getBody();

        assertFalse(response.isSuccess());
        assertEquals("Service not found", response.getMessage());

        verify(serviceRepository, never()).deleteById(anyLong());
    }

    // ---------------- UPDATE ----------------

    @Test
    void updateServiceType_shouldUpdateSuccessfully() {
        ServiceTypeRequest request = new ServiceTypeRequest();
        request.setName("Electrician");

        when(serviceRepository.findById(1L))
                .thenReturn(Optional.of(serviceType));

        SimpleResponse response =
                service.updateServiceType(1L, request).getBody();

        assertTrue(response.isSuccess());
        assertEquals("Service updated successfully", response.getMessage());

        verify(serviceRepository).save(serviceType);
    }

    // ---------------- FILTER ----------------

    @Test
    void filterServiceType_shouldReturnPaginatedResponse() {
        Page<ServiceType> page = new PageImpl<>(
                List.of(serviceType),
                PageRequest.of(0, 10),
                1
        );

        when(serviceRepository.filterService(eq("Plumb"), any(Pageable.class)))
                .thenReturn(page);

        PaginatedResponse response =
                service.filterServiceType("Plumb", 10, 1);

        assertEquals(1, response.getTotalRecords());
        assertEquals(1, response.getTotalPages());
        assertEquals(1, response.getPageNumber());
        assertEquals(10, response.getPageSize());
    }

    // ---------------- EDGE CASE ----------------

    @Test
    void filterServiceType_shouldThrowException_whenPageInvalid() {
        assertThrows(IllegalArgumentException.class,
                () -> service.filterServiceType("test", 10, 0));
    }

}
