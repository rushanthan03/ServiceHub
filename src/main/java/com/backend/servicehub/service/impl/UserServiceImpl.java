package com.backend.servicehub.service.impl;

import com.backend.servicehub.dto.request.LoginRequest;
import com.backend.servicehub.dto.request.UserRequest;
import com.backend.servicehub.dto.response.PaginatedResponse;
import com.backend.servicehub.dto.response.SimpleResponse;
import com.backend.servicehub.dto.response.UserResponse;
import com.backend.servicehub.entity.User;
import com.backend.servicehub.repository.UserRepository;
import com.backend.servicehub.security.JwtUtil;
import com.backend.servicehub.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    @Transactional
    public ResponseEntity<SimpleResponse> saveUser(UserRequest userRequest) {
        String normalizedEmail = userRequest.getEmail().trim().toLowerCase();
        Optional<User> existingUserOpt = userRepository.findByEmail(normalizedEmail);

        if (existingUserOpt.isPresent()) {
            return ResponseEntity.ok(SimpleResponse.builder().success(false).message("User name already exists").build());
        }
        // New user registration
        User user = User.builder()
                .email(normalizedEmail)
                .password(bCryptPasswordEncoder.encode(userRequest.getPassword()))
                .gender(userRequest.getGender())
                .dateOfBirth(userRequest.getDateOfBirth())
                .mobile(userRequest.getMobile())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .isActive(true)
                .build();
        userRepository.save(user);

        return ResponseEntity.ok(SimpleResponse.builder()
                .success(true)
                .message("User registered successfully")
                .build());
    }


    @Transactional
    public ResponseEntity<SimpleResponse> updateUser(Long id, UserRequest userRequest) {
        SimpleResponse response = new SimpleResponse(false, "");
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            BeanUtils.copyProperties(userRequest, existingUser, getNullPropertyNames(userRequest));
            userRepository.save(existingUser);
            response.setSuccess(true);
            response.setMessage("User updated successfully");
        } else {
            response.setMessage("User not Found");
        }
        return ResponseEntity.ok(response);
    }


    @Transactional
    public ResponseEntity<UserResponse> getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            UserResponse userResponse = mapToDUserResponse(user);
            return ResponseEntity.ok(userResponse);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    private UserResponse mapToDUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .mobile(user.getMobile())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .isActive(user.isActive())
                .build();
    }


    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        return emptyNames.toArray(new String[0]);
    }

    @Transactional
    public ResponseEntity<SimpleResponse> archive(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        SimpleResponse response;
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setActive(!existingUser.isActive());
            userRepository.save(existingUser);
            response = new SimpleResponse(true, "User status updated successfully");
        } else {
            response = new SimpleResponse(false, "User not Found");
        }
        return ResponseEntity.ok(response);

    }

    @Transactional
    public PaginatedResponse filterUser(String search, Boolean isActive, Integer pageSize, Integer pageCount) {
        if (pageCount < 1) {
            throw new IllegalArgumentException("Page count must be 1 or greater.");
        }
        Pageable paging = PageRequest.of(pageCount - 1, pageSize, Sort.by("id").descending());
        Page<User> userPage = userRepository.filterUser(search, isActive, paging);

        List<User> userList = userPage.getContent().stream()
                .distinct()
                .toList();

        long totalRecords = userPage.getTotalElements();
        int totalPages = userPage.getTotalPages();
        int pageNumber = userPage.getNumber() + 1;

        List<UserResponse> petResponses = userList.stream()
                .map(this::mapToDUserResponse)
                .toList();

        return PaginatedResponse.builder()
                .totalRecords(totalRecords)
                .totalPages(totalPages)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .records(petResponses)
                .build();

    }

    @Override
    public String login(LoginRequest loginRequest) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        if (authentication.isAuthenticated()) {
            return jwtUtil.generateToken(loginRequest.getEmail());
        } else {
            return "fail";
        }
    }

}
