package com.eai.user.service;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.eai.user.dto.UserDTO;
import com.eai.user.entities.AppUser;
import com.eai.user.entities.UserPrincipal;
import com.eai.user.repository.AppUserRepository;
import com.eai.user.utilities.AccountUtilities;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MyCustomeUserService implements UserDetailsService{

    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
     Optional<AppUser> userOp = appUserRepository.findUserByEmailAndStatus(username);
     if(!userOp.isPresent()){
      throw new UsernameNotFoundException("User not found");
     }
     UserDTO userDto = AccountUtilities.fromUserEntityToDto(userOp.get());
        return new UserPrincipal(userDto);
    }

}
