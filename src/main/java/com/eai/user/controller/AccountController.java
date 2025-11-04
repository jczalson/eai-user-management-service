package com.eai.user.controller;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.eai.user.dto.LoginDTO;
import com.eai.user.dto.UserDTO;
import com.eai.user.dto.UserDTOInput;
import com.eai.user.dto.UserRoleInput;
import com.eai.user.entities.AppRole;
import com.eai.user.entities.HttpResponse;
import com.eai.user.entities.UserPrincipal;
import com.eai.user.entities.UserStatusEnum;
import com.eai.user.exception.InvalidateRequestException;
import com.eai.user.service.AccountService;
import com.eai.user.service.JWTService;
import com.eai.user.service.UserConfigurationService;

@RestController
@RequestMapping(path = "account/")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserConfigurationService configurationService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/addRole")
    public AppRole saveRole(@RequestBody AppRole appRole) {
        return accountService.addRole(appRole);
    }

    @PostMapping(path = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    // @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<HttpResponse> saveUser(@RequestParam MultipartFile file, String email, String name,
            String password,
            UserStatusEnum status) throws Exception {
        UserDTOInput userInput = new UserDTOInput();
        userInput.setEmail(email);
        userInput.setPassword(password);
        userInput.setStatusEnum(status);
        userInput.setName(name);
        accountService.addUser(file, userInput);
        return ResponseEntity.created(getUri()).body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("user", accountService.addUser(file, userInput)))
                        .message("User created")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build());
    }

    private URI getUri() {
        return URI.create(
                ServletUriComponentsBuilder.fromCurrentContextPath().path("account/get/<idUser>").toUriString());
    }

    @PostMapping("/login")
    public ResponseEntity<HttpResponse> login(@RequestBody LoginDTO login) {
        // AtomicBoolean attbute = new AtomicBoolean();
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword()));
            UserDTO userDto = accountService.loadUserByUsername(login.getEmail());
                    return userDto.getIsMfa().booleanValue()? sendVerificationCode(userDto) : sendResponse(userDto);
        } catch (Exception ex) {
            throw new InvalidateRequestException(ex.getMessage());
        }

    }

    private ResponseEntity<HttpResponse> sendVerificationCode(UserDTO user) {
    accountService.sendVerificationCode(user);
        return ResponseEntity.ok().body(
        HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .data(Map.of("user", user))
                .message("Verification code sent")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build()); 
    }

    private ResponseEntity<HttpResponse> sendResponse(UserDTO userDto) throws Exception {
        return ResponseEntity.ok().body(
        HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .data(Map.of("access-token", this.jwtService.generateAccessToken(getUserPrincipal(userDto)),
                        "refresh-token", this.jwtService.generateRefreshToken(getUserPrincipal(userDto))))
                .message("Login success")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build());
    }
     @GetMapping("/verify/code/{email}/{code}")
    public ResponseEntity<HttpResponse> verify(@PathVariable("email") String email,
            @PathVariable("code") String code) throws Exception {
        UserDTO user = accountService.verify(email, code);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("access-token",
                                this.jwtService
                                        .generateAccessToken(getUserPrincipal(user)),
                                "refresh-token",
                                this.jwtService
                                        .generateRefreshToken(getUserPrincipal(user)),
                                        "user",user))
                        .message("Login success")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @GetMapping("/verify/profile")
    public ResponseEntity<HttpResponse> profile(Authentication authentication) throws Exception {
        UserDTO user = accountService.loadUserByUsername(authentication.getName());
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("user", user))
                        .message("Profile retrieved")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @PostMapping("/roleToUser")
    public void addRoleToUser(@RequestBody UserRoleInput userRoleInput) {
        accountService.addRoleToUser(userRoleInput.getUserName(), userRoleInput.getRoleName());
    }

    @GetMapping("/roles/{email}")
    public Map<String, List<String>> getRolesByUserName(@PathVariable String email) {
        return accountService.findRolesByUserName(email);
    }

    @GetMapping(path = "/photo/{email}", produces = { MediaType.IMAGE_JPEG_VALUE })
    public byte[] getUserPhoto(@PathVariable String email) throws Exception {
        UserDTO user = accountService.loadUserByUsername(email);
        if (user != null && user.getUserPhoto() != null) {
            return user.getUserPhoto();
        }
        return null;
    }

    @GetMapping("/users")
    public ResponseEntity<HttpResponse> getUsers() {
        List<UserDTO> listOfUsers = accountService.listOfUsers();
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("users", listOfUsers))
                        .message("Retrieved users")
                        .status(HttpStatus.FOUND)
                        .statusCode(HttpStatus.FOUND.value())
                        .build());
    }

    @GetMapping("/user/{id}")
    public UserDTO getUsers(@PathVariable String id) throws Exception {
        UserDTO user = accountService.loadUserByUsername(id);
        return user;
    }

   private UserPrincipal getUserPrincipal(UserDTO userDTO) throws Exception{
    UserPrincipal userPrincipal = new UserPrincipal();
    userPrincipal.setUser(accountService.loadUserByUsername(userDTO.getUserName()));
    return userPrincipal;
   }
}
