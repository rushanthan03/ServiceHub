package com.backend.servicehub.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
public class UserResponse {
    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String mobile;
    private boolean isActive;
    private String profileImage;
    private List<RoleResponse> roleResponseList;
}
