package com.backend.servicehub.service;

import com.backend.servicehub.dto.request.LoginRequest;
import com.backend.servicehub.dto.request.UserRequest;
import com.backend.servicehub.dto.response.PaginatedResponse;
import com.backend.servicehub.dto.response.SimpleResponse;
import com.backend.servicehub.dto.response.UserResponse;
import com.backend.servicehub.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UserService {
    ResponseEntity<SimpleResponse> saveUser(UserRequest userRequest);

    ResponseEntity<SimpleResponse> saveUserProfile(MultipartFile profileImage);

    ResponseEntity<SimpleResponse> updateUser(Long id, UserRequest userRequest);

    ResponseEntity<UserResponse> getUserById(Long id);

    ResponseEntity<SimpleResponse> archive(Long id);

    PaginatedResponse filterUser(String search, Boolean isActive, Integer pageSize, Integer pageCount);

    String login(LoginRequest loginRequest);

    Optional<User> getOptionalCurrentUser();

}
