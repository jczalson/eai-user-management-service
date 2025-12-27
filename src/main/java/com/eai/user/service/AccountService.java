package com.eai.user.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.eai.user.dto.UserDTO;
import com.eai.user.dto.UserDTOInput;
import com.eai.user.entities.AppRole;

public interface AccountService {

    public void addRoleToUser(String email, String roleName);

    public UserDTO loadUserByUsername(String email) throws Exception;

    public List<UserDTO> listOfUsers();

    public AppRole addRole(AppRole AppRole);

    public UserDTO addUser(MultipartFile file, UserDTOInput dtoInput) throws IOException;

    public List<AppRole> listOfAllRoles();

    public Map<String, List<String>> findRolesByUserName(String userName);

    // public String createAccessToken(UserDTO user) throws Exception;

    // public String createRefreshToken(UserDTO user) throws Exception;

    public UserDTO verify(String email, String code);

    public void sendVerificationCode(UserDTO user);

    public UserDTO getUserById(Long userId);

    public UserDTO updateUser(UserDTO userDTO);
}
