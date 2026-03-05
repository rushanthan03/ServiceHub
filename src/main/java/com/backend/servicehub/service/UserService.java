package com.backend.servicehub.service;

import com.backend.servicehub.dto.request.UserRequest;
import com.backend.servicehub.dto.response.PaginatedResponse;
import com.backend.servicehub.dto.response.SimpleResponse;
import com.backend.servicehub.dto.response.UserResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<SimpleResponse> saveUser(UserRequest userRequest);

    ResponseEntity<SimpleResponse> updateUser(Long id, UserRequest userRequest);

    ResponseEntity<UserResponse> getUserById(Long id);

    ResponseEntity<SimpleResponse> archive(Long id);

    PaginatedResponse filterUser(String search, Boolean isActive, Integer pageSize, Integer pageCount);

}
