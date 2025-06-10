package com.eai.user.utilities;

import java.util.stream.Collectors;

import com.eai.user.dto.UserDTO;
import com.eai.user.entities.AppUser;


public class AccountUtilities {

   public static UserDTO fromUserEntityToDto(AppUser user) {
       UserDTO dto = new UserDTO();
       dto.setIdUser(user.getIdUser());
       dto.setPassword(user.getPassword());
       dto.setStatusEnum(user.getUserStatusEnum());
       dto.setUserName(user.getEmail());
       if(user != null && !user.getUserRoleList().isEmpty()){
        dto.getRolesOfUser().addAll(user.getUserRoleList().stream()
                    .map(role -> role.getAppRole().getRoleName())
                    .collect(Collectors.toList()));
       }
        return dto;
    }

}
