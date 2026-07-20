package com.neobank.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String status;
    private String token;
    private String message;
}
