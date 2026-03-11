package com.eai.user.controller;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.eai.user.dto.EventTypeEnum;
import com.eai.user.dto.LoginDTO;
import com.eai.user.dto.UserDTO;
import com.eai.user.dto.UserDTOInput;
import com.eai.user.dto.UserRoleInput;
import com.eai.user.entities.AppRole;
import com.eai.user.entities.HttpResponse;
import com.eai.user.entities.UserPrincipal;
import com.eai.user.entities.UserStatusEnum;
import com.eai.user.event.NewUserEvent;
import com.eai.user.exception.RestApiException;
import com.eai.user.service.AccountService;
import com.eai.user.service.JWTService;
import com.eai.user.utilities.UserUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/account")
@Slf4j
public class AccountController {

  private static final String TOKEN_PREFIX = "Bearer ";

  @Autowired
  private AccountService accountService;

  @Autowired
  private JWTService jwtService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  // @Autowired
  // private UserConfigurationService configurationService;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private ApplicationEventPublisher publisher;

  @PostMapping("/addRole")
  public AppRole saveRole(@RequestBody AppRole appRole) {
    return accountService.addRole(appRole);
  }

  @PatchMapping("/update")
  public ResponseEntity<HttpResponse> updateUser(@RequestBody UserDTO userDTO) throws InterruptedException {
    TimeUnit.SECONDS.sleep(3);
    publisher.publishEvent(new NewUserEvent(EventTypeEnum.PROFILE_UPDATE, userDTO.getEmail()));
    UserDTO user = accountService.updateUser(userDTO);
    return ResponseEntity.ok().body(
        HttpResponse.builder()
            .timeStamp(LocalDateTime.now().toString())
            .data(Map.of("user", user))
            .message("User updated")
            .status(HttpStatus.OK)
            .statusCode(HttpStatus.OK.value())
            .build());
  }

  @PostMapping(path = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  // @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<HttpResponse> saveUser(@RequestPart("file") MultipartFile file, String email, String name,
      String password,
      UserStatusEnum status, boolean isMfa) throws Exception {
    TimeUnit.SECONDS.sleep(3);
    UserDTOInput userInput = new UserDTOInput();
    userInput.setEmail(email);
    userInput.setPassword(password);
    userInput.setStatusEnum(status);
    userInput.setName(name);
    userInput.setMfa(isMfa);
    UserDTO user = accountService.addUser(file, userInput);
    return ResponseEntity.ok().body(
        HttpResponse.builder()
            .timeStamp(LocalDateTime.now().toString())
            .data(Map.of("user", user))
            .message("User created")
            .status(HttpStatus.CREATED)
            .statusCode(HttpStatus.CREATED.value())
            .build());
  }

  @PostMapping("/login")
  public ResponseEntity<HttpResponse> login(@RequestBody @Valid LoginDTO login) throws Exception {
    // HandleException is built for all excptions
    UserDTO userDto = authenticate(login);
    return userDto.getIsMfa().booleanValue() ? sendVerificationCode(userDto) : sendResponse(userDto);
  }

  @GetMapping("/refresh/token")
  public ResponseEntity<HttpResponse> refreshToken(HttpServletRequest request) throws Exception {
    if (isHeaderTokenValidated(request)) {
      String refreshToken = request.getHeader(AUTHORIZATION).substring(TOKEN_PREFIX.length());
      UserDTO userDTO = accountService.getUserById(jwtService.extractUserIdFromToken(refreshToken));
      return ResponseEntity.ok().body(
          HttpResponse.builder()
              .timeStamp(LocalDateTime.now().toString())
              .data(Map.of("user", userDTO, "access_token",
                  jwtService.generateAccessToken(getUserPrincipal(userDTO)),
                  "refresh_token", refreshToken))
              .message("Token refreshed with success")
              .status(HttpStatus.OK)
              .statusCode(HttpStatus.OK.value())
              .build());
    } else {
      return ResponseEntity.badRequest().body(
          HttpResponse.builder()
              .timeStamp(LocalDateTime.now().toString())
              .reason("Refresh token is missing or invalid")
              .developerMessage("Refresh token is missing or invalid")
              .status(HttpStatus.BAD_REQUEST)
              .statusCode(HttpStatus.BAD_REQUEST.value())
              .build());
    }
  }

  private boolean isHeaderTokenValidated(HttpServletRequest request) {
    return request.getHeader(AUTHORIZATION) != null &&
        request.getHeader(AUTHORIZATION).startsWith(TOKEN_PREFIX) &&
        jwtService.validateToken(
            request.getHeader(AUTHORIZATION).substring(TOKEN_PREFIX.length()),
            accountService.getUserById(jwtService
                .extractUserIdFromToken(request.getHeader(AUTHORIZATION)
                    .substring(TOKEN_PREFIX.length()))));
  }

  private UserDTO authenticate(LoginDTO login){
    try {
      if(null != accountService.loadUserByUsername(login.getEmail())){
        publisher.publishEvent(new NewUserEvent(EventTypeEnum.LOGIN_ATTEMPT, login.getEmail()));
      }
      Authentication authentication = authenticationManager
          .authenticate(new UsernamePasswordAuthenticationToken(login.getEmail(),
              login.getPassword()));
      UserDTO loggedIndUser = UserUtils.getLoggedIndUser(authentication);
      if(!loggedIndUser.getIsMfa().booleanValue()){
        publisher.publishEvent(new NewUserEvent(EventTypeEnum.LOGIN_ATTEMPT_SUCCESS, login.getEmail()));
      }
      return loggedIndUser;
    } catch (Exception e) {
      publisher.publishEvent(new NewUserEvent(EventTypeEnum.LOGIN_ATTEMPT_FAILED, login.getEmail()));
      throw new RestApiException("Fail to log in "+e.getMessage());
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
            .data(Map.of("user",userDto,"access_token",
                this.jwtService.generateAccessToken(
                    getUserPrincipal(userDto)),
                "refresh_token",
                this.jwtService.generateRefreshToken(
                    getUserPrincipal(userDto))))
            .message("Login success")
            .status(HttpStatus.OK)
            .statusCode(HttpStatus.OK.value())
            .build());
  }

  @GetMapping("/verify/code/{email}/{code}")
  public ResponseEntity<HttpResponse> verify(@PathVariable("email") String email,
      @PathVariable("code") String code) throws Exception {
    UserDTO user = accountService.verify(email, code);
    publisher.publishEvent(new NewUserEvent(EventTypeEnum.LOGIN_ATTEMPT_SUCCESS, user.getEmail()));
    return ResponseEntity.ok().body(
        HttpResponse.builder()
            .timeStamp(LocalDateTime.now().toString())
            .data(Map.of("access_token", this.jwtService.generateAccessToken(getUserPrincipal(user)),
                "refresh_token", this.jwtService.generateRefreshToken(getUserPrincipal(user)),
                "user", user))
            .message("Login success")
            .status(HttpStatus.OK)
            .statusCode(HttpStatus.OK.value())
            .build());
  }

  @GetMapping("/user/profile")
  public ResponseEntity<HttpResponse> profile(Authentication authentication) throws Exception {
    UserDTO user = accountService
        .loadUserByUsername(UserUtils.getAuthenticatedUser(authentication).getEmail());
    return ResponseEntity.ok().body(
        HttpResponse.builder()
            .timeStamp(LocalDateTime.now().toString())
            .data(Map.of("user", user,"access_token",jwtService.generateAccessToken(getUserPrincipal(user)),
          "refresh_token",jwtService.generateRefreshToken(getUserPrincipal(user))))
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

  @GetMapping(path = "/image/{email}", consumes = { MediaType.IMAGE_JPEG_VALUE })
  public String getUserImage(@PathVariable String email) throws Exception {
    UserDTO user = accountService.loadUserByUsername(email);
    if (user != null && user.getImageUrl() != null) {
      return user.getImageUrl();
    }
    return null;
  }

  @PatchMapping(path = "/update/image")
  public ResponseEntity<HttpResponse> updateImageUrl(Authentication authentication,
      @RequestParam("image") MultipartFile image) throws Exception {
    UserDTO user = UserUtils.getAuthenticatedUser(authentication);
    UserDTO updateUserImage = accountService.updateUserImage(image, user);
    publisher.publishEvent(new NewUserEvent(EventTypeEnum.PROFILE_IMAGE_UPDATED, user.getEmail()));
    return ResponseEntity.ok().body(
        HttpResponse.builder()
            .timeStamp(LocalDateTime.now().toString())
            .data(Map.of("user", updateUserImage))
            .message("Profile image updated")
            .status(HttpStatus.OK)
            .statusCode(HttpStatus.OK.value())
            .build());
  }

  @GetMapping(path = "/user/image/{fileName}", produces = { MediaType.IMAGE_PNG_VALUE })
  public byte[] getProfileImage(@PathVariable("fileName") String fileName) throws Exception {
    return Files.readAllBytes(Paths.get(System.getProperty("user.home") + "/Downloads/image/" + fileName));
  }

  @GetMapping("/users")
  public ResponseEntity<HttpResponse> getUsers() {
    List<UserDTO> listOfUsers = accountService.listOfUsers();
    return ResponseEntity.ok().body(
        HttpResponse.builder()
            .timeStamp(LocalDateTime.now().toString()).data(Map.of("users", listOfUsers))
            .message("Retrieved users")
            .build());
  }

  @GetMapping("/user/{id}")
  public UserDTO getUsers(@PathVariable String id) throws Exception {
    UserDTO user = accountService.loadUserByUsername(id);
    return user;
  }

  private UserPrincipal getUserPrincipal(UserDTO userDTO) throws Exception {
    UserPrincipal userPrincipal = new UserPrincipal();
    userPrincipal.setUser(accountService.loadUserByUsername(userDTO.getEmail()));
    return userPrincipal;
  }
}