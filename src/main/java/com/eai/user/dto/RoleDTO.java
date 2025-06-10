package com.eai.user.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDTO implements Serializable{

    private String userName;

    private  List<String> roles = new ArrayList<>();

}
