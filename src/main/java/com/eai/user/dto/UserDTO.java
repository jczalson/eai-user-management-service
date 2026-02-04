package com.eai.user.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.eai.user.entities.UserStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class UserDTO implements Serializable {

    private static final Long serializedId = 1L;

    private Long idUser;

    private String email;

    private String name;

    private String photo;

    private String imageUrl;

    private Boolean isMfa;
    //  @JsonIgnore
    private byte[] userPhoto;

    // @JsonProperty( access = JsonProperty.Access.WRITE_ONLY)
    @JsonIgnore
    private String password;

    private UserStatusEnum statusEnum;

    List<String> rolesOfUser = new ArrayList<String>();

}
