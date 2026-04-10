package com.backend.servicehub.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ServiceTypeResponse {
    private Long id;
    private String name;
}
