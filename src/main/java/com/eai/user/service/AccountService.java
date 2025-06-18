package com.eai.user.service;

import java.util.List;
import java.util.Map;

import com.eai.user.dto.UserDTO;
import com.eai.user.entities.AppRole;
import com.eai.user.entities.AppUser;

public interface AccountService {

    public void addRoleToUser(String email, String roleName);

    public UserDTO loadUserByUsername(String email);

    public List<UserDTO> listOfUsers();

    public AppRole addRole(AppRole AppRole);

    public UserDTO addUser(AppUser appUser);

    public List<AppRole> listOfAllRoles();

    public  Map<String,List<String>> findRolesByUserName(String userName);

    public Map<String, String> verify(AppUser user) throws Exception;
}
