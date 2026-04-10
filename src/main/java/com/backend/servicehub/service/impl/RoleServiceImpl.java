package com.backend.servicehub.service.impl;

import com.backend.servicehub.dto.request.RoleRequest;
import com.backend.servicehub.dto.response.PaginatedResponse;
import com.backend.servicehub.dto.response.RoleResponse;
import com.backend.servicehub.dto.response.SimpleResponse;
import com.backend.servicehub.entity.Role;
import com.backend.servicehub.repository.RoleRepository;
import com.backend.servicehub.service.RoleService;
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
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public ResponseEntity<SimpleResponse> saveRoleDetails(RoleRequest roleRequest) {
        SimpleResponse response = new SimpleResponse(false, "");
        if (roleRepository.existsByName(roleRequest.getName())) {
            response.setMessage("Role name already exists");
        } else {
            Role role = Role.builder()
                    .name(roleRequest.getName())
                    .build();
            roleRepository.save(role);

            response.setSuccess(true);
            response.setMessage("Role details created successfully");
        }
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<RoleResponse> getRoleById(Long id) {
        Optional<Role> roleOptional = roleRepository.findById(id);
        if (roleOptional.isPresent()) {
            Role role = roleOptional.get();
            RoleResponse roleResponse = mapToRoleResponse(role);
            return ResponseEntity.ok(roleResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<SimpleResponse> deleteRole(Long id) {
        SimpleResponse response = new SimpleResponse(false, "");
        if (roleRepository.existsById(id)) {
            roleRepository.deleteById(id);
            response.setSuccess(true);
            response.setMessage("Role deleted successfully");
        } else {
            response.setMessage("Role not found");
        }
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<SimpleResponse> updateRole(Long id, RoleRequest roleRequest) {
        SimpleResponse response = new SimpleResponse(false, "");

        Optional<Role> existingRoleOptional = roleRepository.findById(id);
        if (existingRoleOptional.isPresent()) {
            Role existingRole = existingRoleOptional.get();
            existingRole.setName(roleRequest.getName());
            roleRepository.save(existingRole);
            response.setSuccess(true);
            response.setMessage("Role updated successfully");
        } else {
            response.setMessage("Role not found");
        }
        return ResponseEntity.ok(response);
    }

    private RoleResponse mapToRoleResponse(Role role) {
        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }

    @Override
    public PaginatedResponse filterRole(String search, Integer pageSize, Integer pageCount) {
        if (pageCount < 1) {
            throw new IllegalArgumentException("Page count must be 1 or greater.");
        }
        Pageable paging = PageRequest.of(pageCount - 1, pageSize, Sort.by("id").descending());
        Page<Role> rolePage = roleRepository.filterRole(search, paging);

        List<Role> roleList = rolePage.getContent();
        long totalRecords = rolePage.getTotalElements();
        int totalPages = rolePage.getTotalPages();
        int pageNumber = rolePage.getNumber() + 1;

        List<RoleResponse> roleResponses = roleList.stream()
                .map(this::mapToRoleResponse)
                .toList();

        return PaginatedResponse.builder()
                .totalRecords(totalRecords)
                .totalPages(totalPages)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .records(roleResponses)
                .build();
    }


}
