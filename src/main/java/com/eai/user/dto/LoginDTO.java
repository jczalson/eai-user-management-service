package com.eai.user.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO implements Serializable {

    private static final Long id = 1L;
    private String email;
    private String password;
}
