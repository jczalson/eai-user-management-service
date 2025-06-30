package com.eai.user.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.eai.user.dto.LoginDTO;
import com.eai.user.dto.UserDTO;
import com.eai.user.dto.UserDTOInput;
import com.eai.user.dto.UserRoleInput;
import com.eai.user.entities.AppRole;
import com.eai.user.entities.UserStatusEnum;
import com.eai.user.exception.InvalidateRequestException;
import com.eai.user.service.AccountService;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("account/addRole")
    public AppRole saveRole(@RequestBody AppRole appRole) {
        return accountService.addRole(appRole);
    }

    @PostMapping(path = "account/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    // @PreAuthorize("hasAuthority('ADMIN')")
    public UserDTO saveUser(@RequestParam MultipartFile file, String email, String name, String password,
            UserStatusEnum status) throws Exception {
        UserDTOInput userInput = new UserDTOInput();
        userInput.setEmail(email);
        userInput.setPassword(password);
        userInput.setStatusEnum(status);
        userInput.setName(name);
        return accountService.addUser(file, userInput);
    }

    @PostMapping("account/login")
    public Map<String, String> login(@RequestBody LoginDTO login) {
        try {
            return accountService.verify(login);
        } catch (Exception ex) {
            throw new InvalidateRequestException(ex.getMessage());
        }

    }

    @PostMapping("account/roleToUser")
    public void addRoleToUser(@RequestBody UserRoleInput userRoleInput) {
        accountService.addRoleToUser(userRoleInput.getUserName(), userRoleInput.getRoleName());
    }

    @GetMapping("account/roles/{email}")
    public Map<String, List<String>> getRolesByUserName(@PathVariable String email) {
        return accountService.findRolesByUserName(email);
    }

    @GetMapping(path = "account/photo/{email}", produces = { MediaType.IMAGE_JPEG_VALUE })
    public byte[] getUserPhoto(@PathVariable String email) throws Exception {
        UserDTO user = accountService.loadUserByUsername(email);
        if (user != null && user.getUserPhoto() != null) {
            return user.getUserPhoto();
        }
        return null;
    }

    @GetMapping("account/users")
    public List<UserDTO> getUsers() {
        List<UserDTO> listOfUsers = accountService.listOfUsers();
        return listOfUsers;
    }
     @GetMapping("account/user/{id}")
    public UserDTO getUsers(@PathVariable String id) throws Exception {
        UserDTO user = accountService.loadUserByUsername(id);
        return user;
    }
}
