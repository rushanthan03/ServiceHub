package com.backend.servicehub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedResponse {
    private Long totalRecords;

    private int totalPages;

    private int pageNumber;

    private int pageSize;

    private Object records;
}
