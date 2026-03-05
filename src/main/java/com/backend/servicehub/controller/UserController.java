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
@RequestMapping("user")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<SimpleResponse> saveUser(@RequestBody UserRequest userRequest) {
        return userService.saveUser(userRequest);

    }

    @PutMapping("/{id}")
    public ResponseEntity<SimpleResponse> updateUser(
            @PathVariable("id") Long id, @RequestBody UserRequest userRequest) {
        return userService.updateUser(id, userRequest);
    }

    @GetMapping
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }


    @PutMapping("archive/{id}")
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
