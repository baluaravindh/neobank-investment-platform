package com.neobank.user.service.impl;

import com.neobank.user.dto.request.LoginRequest;
import com.neobank.user.dto.request.RegisterUserRequest;
import com.neobank.user.dto.response.LoginResponse;
import com.neobank.user.dto.response.RegisterUserResponse;
import com.neobank.user.entity.User;
import com.neobank.user.enums.UserRole;
import com.neobank.user.enums.UserStatus;
import com.neobank.user.exception.InvalidCredentialsException;
import com.neobank.user.exception.UserAlreadyExistsException;
import com.neobank.user.repository.UserRepository;
import com.neobank.user.service.UserService;
import com.neobank.user.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    @Override
    public RegisterUserResponse registerUser(RegisterUserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(encoder.encode(request.getPassword()))
                .status(UserStatus.ACTIVE)
                .role(UserRole.CUSTOMER)
                .build();

        User savedUser = userRepository.save(user);
        return mapToRegisterUserResponse(savedUser);
    }

    @Override
    public LoginResponse loginUser(LoginRequest request) {
        // Find the user by email.
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        // Compare the raw password from LoginRequest
        // with the BCrypt hashed password stored in User.
        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user);

        return LoginResponse.builder()
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .status(user.getStatus().name())
                .token(token)
                .message("User Logged in Successfully")
                .build();
    }

    private RegisterUserResponse mapToRegisterUserResponse(User user) {
        return RegisterUserResponse.builder()
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .status(user.getStatus().name())
                .role(user.getRole().name())
                .createdAt(user.getCreatedAt())
                .message("User Registered Successfully")
                .build();
    }
}
