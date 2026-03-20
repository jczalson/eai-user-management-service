package com.eai.user.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.eai.user.dto.TwoFactorVerificationsDTO;
import com.eai.user.dto.UserDTO;
import com.eai.user.dto.UserDTOInput;
import com.eai.user.entities.AppRole;
import com.eai.user.entities.AppUser;
import com.eai.user.entities.AppUserRole;
import com.eai.user.entities.AppUserRoleKey;
import com.eai.user.exception.RestApiException;
import com.eai.user.messaging.producer.UserActivityProducer;
import com.eai.user.repository.AppRoleRepository;
import com.eai.user.repository.AppUserRepository;
import com.eai.user.repository.UserRoleRepository;
import com.eai.user.utilities.AccountUtilities;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@Transactional
@Slf4j
public class AccountServiceImplm implements AccountService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AppRoleRepository appRoleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // @Autowired
    // private JWTService jwtService;

    @Autowired
    private TwoFactorVerificationsService twoFactorVerificationsService;

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
                throw new RestApiException("Error occurred when  adding role");
            }
        } catch (Exception ex) {
            throw new RestApiException("Error occurred when  adding role", ex);
        }

    }

    @Override
    public UserDTO loadUserByUsername(String email) {
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
    public UserDTO updateUserImage(MultipartFile image, UserDTO userDTO) {
        String imageUrl = buildImageUrl(userDTO.getEmail());
        userDTO.setImageUrl(imageUrl);
        saveImage(image, userDTO.getEmail());
        appUserRepository.updateImage(imageUrl,userDTO.getIdUser());
     return userDTO;
    }

    private void saveImage(MultipartFile image, String email) {
        Path folderPath = Paths.get(System.getProperty("user.home")+"/Downloads/image").toAbsolutePath().normalize();
        if (!Files.exists(folderPath)) {
            try {
                Files.createDirectories(folderPath);
            } catch (Exception e) {
                log.error("Error creating folder directory", e.getMessage());
                throw new RestApiException("Unable to create folder",e);
            }
           log.info("Folder created correctly {}", folderPath);
        }
        try {
           Files.copy(image.getInputStream(), folderPath.resolve(email+".png"),REPLACE_EXISTING);
        log.info("Image saved correctly in {}", folderPath);
        } catch (Exception e) {
           log.error("Error occured when saving image", e);
        }
    }

    private String buildImageUrl(String email) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/account/user/image/" + email +".png").toUriString();
    }

    @Override
    public UserDTO addUser(UserDTOInput userInput) throws IOException {
        userInput.setPassword(passwordEncoder.encode(userInput.getPassword()));
        UserDTO dto = AccountUtilities
                .fromUserEntityToDto(appUserRepository.save(AccountUtilities.fromUserDtoInputToEntity(userInput)));
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

    // @Override
    // public String createAccessToken(UserDTO user) throws Exception {
    // return jwtService.generateAccessToken(user);

    // }

    // @Override
    // public String createRefreshToken(UserDTO user) throws Exception {
    // return jwtService.generateRefreshToken(user);

    // }

    @Override
    public UserDTO verify(String email, String code) {
        if (isCodeExpired(code)) {
            throw new RestApiException("Code is expired. Please login again");
        }
        try {
            UserDTO userEmail = this.loadUserByUsername(email);
            TwoFactorVerificationsDTO userFromFactorVerificationByCode = this.twoFactorVerificationsService
                    .getUserFromFactorVerificationByCode(code);
            UserDTO userCode = userFromFactorVerificationByCode.getUser();
            if (userFromFactorVerificationByCode != null && userFromFactorVerificationByCode != null
                    && userFromFactorVerificationByCode.getUser() != null) {
                if (userCode.getEmail().equalsIgnoreCase(userEmail.getEmail())) {
                    twoFactorVerificationsService.deleteCode(userFromFactorVerificationByCode);
                    return userEmail;
                } else {
                    throw new RestApiException("Code is invalid. please try again");
                }
            }
        } catch (EmptyResultDataAccessException ex) {
            throw new RestApiException("Could not find record");
        } catch (Exception ex) {
            throw new RestApiException("Could not find record");
        }
        return null;
    }

    private boolean isCodeExpired(String code) {
        try {
            TwoFactorVerificationsDTO two = twoFactorVerificationsService.getUserFromFactorVerificationByCode(code);
            return two != null && two.getExpiryDate() != null && two.getExpiryDate().isBefore(LocalDateTime.now());

        } catch (Exception e) {
            throw new RestApiException("Record not found " + e.getMessage());
        }
    }

    @Override
    public void sendVerificationCode(UserDTO user) {
        TwoFactorVerificationsDTO verificationCode = twoFactorVerificationsService
                .createVerificationCode(user.getIdUser());
        log.info("Verification code: " + verificationCode.getCode());
    }

    @Override
    public UserDTO getUserById(Long userId) {
        return AccountUtilities.fromUserEntityToDto(appUserRepository.findById(userId).get());
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        if (userDTO != null) {
            if (StringUtils.isBlank(String.valueOf(userDTO.getIdUser()))) {
                throw new RestApiException("UserId cannot be null");
            }
            if (StringUtils.isBlank(userDTO.getEmail())) {
                throw new RestApiException("Email cannot be null");
            }
            if (StringUtils.isBlank(userDTO.getName())) {
                throw new RestApiException("UserId cannot be null");
            }
            UserDTO dto = fromExistedToUpdatedOne(getUserById(userDTO.getIdUser()), userDTO);
            return AccountUtilities
                    .fromUserEntityToDto(appUserRepository.save(AccountUtilities.fromUserDtoToEntity(dto)));
        }
        return null;
    }

    private UserDTO fromExistedToUpdatedOne(UserDTO existedUserDto, UserDTO userDto) {
        if (StringUtils.isNotBlank(userDto.getPassword()))
            existedUserDto.setPassword(userDto.getPassword());

        if (userDto.getStatusEnum() != null)
            existedUserDto.setStatusEnum(userDto.getStatusEnum());

        if (StringUtils.isNotBlank(userDto.getEmail()))
            existedUserDto.setEmail(userDto.getEmail());

        if (StringUtils.isNotBlank(userDto.getName()))
            existedUserDto.setName(userDto.getName());

        if (userDto.getIsMfa() != null)
            existedUserDto.setIsMfa(userDto.getIsMfa());
        // BeanUtils.copyProperties(userDto, existedUserDto);
        return existedUserDto;
    }

}
