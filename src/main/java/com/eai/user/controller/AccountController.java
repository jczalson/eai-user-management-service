package com.eai.user.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.eai.user.dto.UserDTO;
import com.eai.user.dto.UserRoleInput;
import com.eai.user.entities.AppRole;
import com.eai.user.entities.AppUser;
import com.eai.user.service.AccountService;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("account/addRole")
    public AppRole saveRole(@RequestBody AppRole appRole){
       return accountService.addRole(appRole);
    }

    @PostMapping("account/register")
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserDTO saveUser(@RequestBody AppUser user){
       return accountService.addUser(user);
    }

    @PostMapping("account/login")
    public Map<String, String> login(@RequestBody AppUser user){
       return accountService.verify(user);
    }

    @PostMapping("account/roleToUser")
    public void addRoleToUser(@RequestBody UserRoleInput userRoleInput){
        accountService.addRoleToUser(userRoleInput.getUserName(), userRoleInput.getRoleName());
    }

    @GetMapping("account/roles/{email}")
    public Map<String,List<String>> getRolesByUserName(@PathVariable String email){
        return accountService.findRolesByUserName(email);
    }

     @GetMapping("account/users")
    public List<UserDTO> getUsers(){
        List<UserDTO> listOfUsers = accountService.listOfUsers();
        return listOfUsers;
    }
}
