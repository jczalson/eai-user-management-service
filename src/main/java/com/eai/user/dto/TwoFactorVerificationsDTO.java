package com.eai.user.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class TwoFactorVerificationsDTO {

    private static final long serialVersionUID = 1L;
    
    private long id;

    private String code;

    private UserDTO user = new UserDTO();

    private LocalDateTime expiryDate;
}
