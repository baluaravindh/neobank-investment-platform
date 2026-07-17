package com.neobank.user.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterUserRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
}
