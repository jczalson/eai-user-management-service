package com.eai.user.utilities;

import org.springframework.security.core.Authentication;

import com.eai.user.dto.UserDTO;
import com.eai.user.entities.UserPrincipal;

public class UserUtils {


    public static UserDTO getAuthenticatedUser(Authentication authentication){
        return ((UserDTO) authentication.getPrincipal());
    }

      public static UserDTO getLoggedIndUser(Authentication authenication) {
        return ((UserPrincipal) authenication.getPrincipal()).getUser();
    }
}
