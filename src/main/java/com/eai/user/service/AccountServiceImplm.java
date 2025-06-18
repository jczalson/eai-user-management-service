package com.eai.user.service;

import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.eai.user.dto.RoleDTO;
import com.eai.user.dto.UserDTO;
import com.eai.user.entities.AppRole;
import com.eai.user.entities.AppUser;
import com.eai.user.entities.AppUserRole;
import com.eai.user.entities.AppUserRoleKey;
import com.eai.user.exception.InvalidateRequestException;
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
    public UserDTO addUser(AppUser appUser) {
        String pwd = appUser.getPassword();
        appUser.setPassword(passwordEncoder.encode(pwd));
        UserDTO dto = AccountUtilities.fromUserEntityToDto(appUserRepository.save(appUser));
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
    public Map<String, String> verify(AppUser user) throws Exception {
                return jwtService.generateAccessToken(user);
                
    }

}
