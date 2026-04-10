package com.backend.servicehub.service;


import com.backend.servicehub.dto.request.RoleRequest;
import com.backend.servicehub.dto.response.PaginatedResponse;
import com.backend.servicehub.dto.response.RoleResponse;
import com.backend.servicehub.dto.response.SimpleResponse;
import org.springframework.http.ResponseEntity;

public interface RoleService {
    ResponseEntity<SimpleResponse> saveRoleDetails(RoleRequest cityRequest);

    ResponseEntity<RoleResponse> getRoleById(Long id);

    ResponseEntity<SimpleResponse> deleteRole(Long id);

    ResponseEntity<SimpleResponse> updateRole(Long id, RoleRequest roleRequest);

    PaginatedResponse filterRole(String search, Integer pageSize, Integer pageCount);

}
