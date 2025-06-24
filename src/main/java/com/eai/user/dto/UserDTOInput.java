package com.eai.user.dto;

import java.io.Serializable;

import com.eai.user.entities.UserStatusEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserDTOInput implements Serializable {

    private static final Long serializedId = 1L;

    private String email;

    private String photo;

    private String password;

    private UserStatusEnum statusEnum;

}
