package com.neobank.user.service;

import com.neobank.user.dto.request.RegisterUserRequest;
import com.neobank.user.dto.response.RegisterUserResponse;

public interface UserService {
    RegisterUserResponse registerUser(RegisterUserRequest request);
}
