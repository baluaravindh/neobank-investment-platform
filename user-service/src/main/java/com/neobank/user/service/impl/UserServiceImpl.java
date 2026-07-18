package com.neobank.user.service.impl;

import com.neobank.user.dto.request.RegisterUserRequest;
import com.neobank.user.dto.response.RegisterUserResponse;
import com.neobank.user.entity.User;
import com.neobank.user.enums.UserStatus;
import com.neobank.user.exception.UserAlreadyExistsException;
import com.neobank.user.repository.UserRepository;
import com.neobank.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

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
                .build();

        User savedUser = userRepository.save(user);
        return mapToRegisterUserResponse(savedUser);
    }

    private RegisterUserResponse mapToRegisterUserResponse(User user) {
        return RegisterUserResponse.builder()
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .status(user.getStatus().name())
                .createdAt(user.getCreatedAt())
                .message("User Registered Successfully")
                .build();
    }
}
