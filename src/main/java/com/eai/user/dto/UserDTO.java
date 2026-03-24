package com.eai.user.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.eai.user.entities.UserStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class UserDTO implements Serializable {

    private static final Long serializedId = 1L;

    private Long idUser;

    private String email;

    private String name;

    private String imageUrl;

    private Boolean isMfa;
    
    // @JsonProperty( access = JsonProperty.Access.WRITE_ONLY)
    @JsonIgnore
    private String password;

    private UserStatusEnum userStatusEnum;

    List<String> roles = new ArrayList<String>();

}
