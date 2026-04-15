package com.backend.servicehub.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String mobile;
    private List<Long> roles;
}
