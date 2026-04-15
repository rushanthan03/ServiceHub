package com.backend.servicehub.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefreshTokenAuthResponse {
    private String tokenType;
    private String accessToken;
    private String refreshToken;

}
