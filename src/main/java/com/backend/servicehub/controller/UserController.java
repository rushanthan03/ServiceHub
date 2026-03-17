package com.backend.servicehub.controller;


import com.backend.servicehub.dto.request.UserRequest;
import com.backend.servicehub.dto.response.PaginatedResponse;
import com.backend.servicehub.dto.response.SimpleResponse;
import com.backend.servicehub.dto.response.UserResponse;
import com.backend.servicehub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @PutMapping("/{id}")
    public ResponseEntity<SimpleResponse> updateUser(
            @PathVariable Long id, @RequestBody UserRequest userRequest) {
        return userService.updateUser(id, userRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }


    @PutMapping("archive")
    public ResponseEntity<SimpleResponse> userStatus(@RequestParam("id") Long userId) {
        return userService.archive(userId);
    }

    @GetMapping("/filter")
    public ResponseEntity<PaginatedResponse> filterUser(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "isActive", required = false) Boolean isActive,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageCount", required = false, defaultValue = "1") Integer pageCount) {
        PaginatedResponse response = userService.filterUser(search, isActive, pageSize, pageCount);
        return ResponseEntity.ok(response);
    }


}
