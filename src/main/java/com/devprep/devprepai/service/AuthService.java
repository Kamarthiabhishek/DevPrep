package com.devprep.devprepai.service;

import com.devprep.devprepai.dto.AuthResponse;
import com.devprep.devprepai.dto.LoginRequest;
import com.devprep.devprepai.dto.RegisterRequest;
import com.devprep.devprepai.entity.User;
import com.devprep.devprepai.repository.UserRepository;
import com.devprep.devprepai.security.JWTUtil;
import java.util.Locale;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class AuthService {

    private static final String DEFAULT_ROLE = "ROLE_USER";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JWTUtil jwtUtil
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        User user = new User(
                request.name().trim(),
                request.email().trim(),
                passwordEncoder.encode(request.password()),
                request.role()
        );

        User savedUser = userRepository.save(user);
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(savedUser.getEmail())
                .password(savedUser.getPassword())
                .authorities(savedUser.getRole())
                .build();

        return buildAuthResponse(userDetails);
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        return buildAuthResponse((UserDetails) authentication.getPrincipal());
    }

    private AuthResponse buildAuthResponse(UserDetails userDetails) {
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        return new AuthResponse(jwtUtil.generateToken(userDetails), "Bearer", userDetails.getUsername(), role);
    }
}
