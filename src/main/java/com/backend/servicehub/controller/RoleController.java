package com.backend.servicehub.controller;

import com.backend.servicehub.dto.request.RoleRequest;
import com.backend.servicehub.dto.response.PaginatedResponse;
import com.backend.servicehub.dto.response.RoleResponse;
import com.backend.servicehub.dto.response.SimpleResponse;
import com.backend.servicehub.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/roles")
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<SimpleResponse> saveRoleDetails(@RequestBody RoleRequest roleRequest) {
        return roleService.saveRoleDetails(roleRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> getRoleById(@PathVariable Long id) {
        return roleService.getRoleById(id);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SimpleResponse> deleteRole(@PathVariable Long id) {
        return roleService.deleteRole(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SimpleResponse> updateRole(
            @PathVariable("id") Long id, @RequestBody RoleRequest roleRequest) {
        return roleService.updateRole(id, roleRequest);
    }

    @GetMapping("/filter")
    public ResponseEntity<PaginatedResponse> filterRole(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageCount", required = false, defaultValue = "1") Integer pageCount) {
        PaginatedResponse response = roleService.filterRole(search, pageSize, pageCount);
        return ResponseEntity.ok(response);
    }

}