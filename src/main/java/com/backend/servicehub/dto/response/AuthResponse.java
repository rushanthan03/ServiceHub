package com.backend.servicehub.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AuthResponse {
    private String tokenType;
    private String accessToken;
    private String refreshToken;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private List<String> roles;
    private boolean isActive;
}
