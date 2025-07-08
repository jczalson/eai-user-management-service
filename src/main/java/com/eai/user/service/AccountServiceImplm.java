package com.eai.user.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eai.user.dto.LoginDTO;
import com.eai.user.dto.UserDTO;
import com.eai.user.dto.UserDTOInput;
import com.eai.user.entities.AppRole;
import com.eai.user.entities.AppUser;
import com.eai.user.entities.AppUserRole;
import com.eai.user.entities.AppUserRoleKey;
import com.eai.user.exception.InvalidateRequestException;
import com.eai.user.messaging.producer.UserActivityProducer;
import com.eai.user.repository.AppRoleRepository;
import com.eai.user.repository.AppUserRepository;
import com.eai.user.repository.UserRoleRepository;
import com.eai.user.utilities.AccountUtilities;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AccountServiceImplm implements AccountService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AppRoleRepository appRoleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserActivityProducer userActivityProducer;

    @Override
    public void addRoleToUser(String email, String roleName) {
        try {
            Optional<AppUser> appUser = appUserRepository.findUserByEmailAndStatus(email);
            Optional<AppRole> role = appRoleRepository.findByRoleName(roleName);
            if (appUser.isPresent() && role.isPresent()) {
                AppUserRole appUserRole = new AppUserRole();
                AppUserRoleKey key = new AppUserRoleKey();
                key.setIdRole(role.get().getIdRole());
                key.setIdUser(appUser.get().getIdUser());
                appUserRole.setIdUserRole(Math.abs(UUID.randomUUID().getMostSignificantBits()));
                appUserRole.setAppUserRoleKey(key);
                appUserRole.setAppUser(appUser.get());
                appUserRole.setAppRole(role.get());
                userRoleRepository.save(appUserRole);
            } else {
                throw new InvalidateRequestException("Error occurred when  adding role");
            }
        } catch (Exception ex) {
            throw new InvalidateRequestException("Error occurred when  adding role", ex);
        }

    }

    @Override
    public UserDTO loadUserByUsername(String email)  {
        Optional<AppUser> user = appUserRepository.findUserByEmailAndStatus(email);
        if (user.isPresent()) {
            return AccountUtilities.fromUserEntityToDto(user.get());
        }
        return null;
    }

    @Override
    public List<UserDTO> listOfUsers() {
        List<UserDTO> users = new ArrayList<>();
        List<AppUser> usersList = appUserRepository.findAll();
        usersList.stream().forEach(user -> {
            users.add(AccountUtilities.fromUserEntityToDto(user));
        });
        return users;
    }

    @Override
    public AppRole addRole(AppRole appRole) {
        return appRoleRepository.save(appRole);
    }

    @Override
    public UserDTO addUser(MultipartFile file, UserDTOInput userInput) throws IOException {
        userInput.setPassword(passwordEncoder.encode(userInput.getPassword()));
        Path folderPath = Paths.get(System.getProperty("user.home"), "zale-data","pictures");
        if(!Files.exists(folderPath)){
           Files.createDirectories(folderPath);
        }
        // String fileName = UUID.randomUUID().toString();
        Path filePath = Paths.get(System.getProperty("user.home"), "zale-data","pictures"
        ,LocalDate.now()+"_"+System.currentTimeMillis()+"_"+userInput.getEmail().split("@")[0]);
        Files.copy(file.getInputStream(), filePath);
        userInput.setPhoto(filePath.toUri().toString());
        UserDTO dto = AccountUtilities.fromUserEntityToDto(appUserRepository.save(AccountUtilities.fromUserDtoInputToEntity(userInput)));
        userActivityProducer.sendUserActivityMessage(dto);
        return dto;
    }

    @Override
    public List<AppRole> listOfAllRoles() {
        return appRoleRepository.findAll();
    }

    @Override
    public Map<String, List<String>> findRolesByUserName(String userName) {
        Map<String, List<String>> mapRoles = new HashMap<>();
        Optional<AppUser> appUser = appUserRepository.findUserByEmailAndStatus(userName);
        if (appUser.isPresent()) {
            List<AppUserRole> userRoleList = appUser.get().getUserRoleList();
            List<String> collect = userRoleList.stream().map(r -> r.getAppRole().getRoleName())
                    .collect(Collectors.toList());
            mapRoles.put(appUser.get().getEmail(), collect);
        }
        return mapRoles;
    }

    @Override
    public Map<String, String> verify(LoginDTO login) throws Exception {
                return jwtService.generateAccessToken(login);
                
    }

}
