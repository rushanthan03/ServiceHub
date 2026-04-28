package com.backend.servicehub.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ServiceProviderRequest {
    private String name;
    private Long userId;
    private List<Long> serviceTypeIds;
}
