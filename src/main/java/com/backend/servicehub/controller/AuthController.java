package com.backend.servicehub.controller;

import com.backend.servicehub.dto.request.LoginRequest;
import com.backend.servicehub.dto.request.RefreshTokenRequest;
import com.backend.servicehub.dto.request.UserRequest;
import com.backend.servicehub.dto.response.AuthResponse;
import com.backend.servicehub.dto.response.RefreshTokenAuthResponse;
import com.backend.servicehub.dto.response.SimpleResponse;
import com.backend.servicehub.service.AuthService;
import com.backend.servicehub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {
    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/refresh")
    public RefreshTokenAuthResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<SimpleResponse> saveUser(
            @RequestBody UserRequest userRequest) {
        return userService.saveUser(userRequest);
    }

}
