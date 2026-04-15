package com.backend.servicehub.service;

import com.backend.servicehub.dto.request.LoginRequest;
import com.backend.servicehub.dto.request.RefreshTokenRequest;
import com.backend.servicehub.dto.response.AuthResponse;
import com.backend.servicehub.dto.response.RefreshTokenAuthResponse;
import com.backend.servicehub.entity.User;

import java.util.Optional;

public interface AuthService {
    AuthResponse login(LoginRequest loginRequest);

    RefreshTokenAuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    Optional<User> getOptionalCurrentUser();

}
