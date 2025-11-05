package com.eai.user.dto;

import java.io.Serializable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class LoginDTO implements Serializable {

    private static final Long id = 1L;
    @NotEmpty(message = "Email cannot be empty")
    @Email(message ="Invalid email. please insert the correct email")
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    private String password;
}
