package com.backend.servicehub.controller;

import com.backend.servicehub.dto.request.LoginRequest;
import com.backend.servicehub.dto.request.UserRequest;
import com.backend.servicehub.dto.response.SimpleResponse;
import com.backend.servicehub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {
    private final UserService userService;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<SimpleResponse> saveUser(
            @RequestParam("userRequest") UserRequest userRequest) {
        return userService.saveUser(userRequest);
    }

    @PostMapping(value = "/update-profile-image")
    public ResponseEntity<SimpleResponse> saveUserImage(
            @RequestParam(value = "image", required = false) MultipartFile image) {
        return userService.saveUserProfile(image);
    }
}
