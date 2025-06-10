package com.eai.user.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.eai.user.entities.UserStatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserDTO implements Serializable {

    private static final Long serializedId = 1L;

    private Long idUser;

    private String userName;

    @JsonProperty( access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private UserStatusEnum statusEnum;

    List<String> rolesOfUser = new ArrayList<String>();

}
