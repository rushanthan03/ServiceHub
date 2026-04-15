package com.backend.servicehub.service.impl;

import com.backend.servicehub.common.TokenType;
import com.backend.servicehub.dto.request.LoginRequest;
import com.backend.servicehub.dto.request.RefreshTokenRequest;
import com.backend.servicehub.dto.response.AuthResponse;
import com.backend.servicehub.dto.response.RefreshTokenAuthResponse;
import com.backend.servicehub.entity.Role;
import com.backend.servicehub.entity.User;
import com.backend.servicehub.repository.UserRepository;
import com.backend.servicehub.security.JwtUtil;
import com.backend.servicehub.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;


    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        String normalizedEmail = loginRequest.getEmail().trim().toLowerCase();
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(normalizedEmail, loginRequest.getPassword()));
        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.isActive()) {
            throw new DisabledException("User account is inactive");
        }

        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .toList();

        if (authentication.isAuthenticated()) {
            return AuthResponse.builder()
                    .accessToken(jwtUtil.generateAccessToken(normalizedEmail))
                    .refreshToken(jwtUtil.generateRefreshToken(normalizedEmail))
                    .tokenType("Bearer")
                    .userId(user.getId())
                    .userId(user.getId())
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .roles(roles)
                    .isActive(user.isActive())
                    .build();
        } else {
            throw new BadCredentialsException("Invalid email or password");
        }
    }

    @Override
    public RefreshTokenAuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        String username = jwtUtil.extractUserName(refreshToken);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        boolean isValidRefreshToken = jwtUtil.validateToken(refreshToken, userDetails, TokenType.REFRESH);

        if (!isValidRefreshToken) {
            throw new RuntimeException("Invalid refresh token");
        }

        return RefreshTokenAuthResponse.builder()
                .accessToken(jwtUtil.generateAccessToken(username))
                .refreshToken(jwtUtil.generateRefreshToken(username))
                .tokenType("Bearer")
                .build();
    }

    @Override
    public Optional<User> getOptionalCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        String email = authentication.getName();
        return userRepository.findByEmail(email);
    }
}
