package com.backend.servicehub.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ServiceProviderResponse {
    private Long id;
    private String name;
    private Long userId;
    private String userEmail;
    private List<Long> serviceTypeIds;
}
