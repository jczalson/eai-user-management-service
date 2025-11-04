package com.eai.user.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eai.user.dto.TwoFactorVerificationsDTO;
import com.eai.user.entities.HttpResponse;
import com.eai.user.service.TwoFactorVerificationsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/verifications")
@RequiredArgsConstructor
public class TwoFactorVerificationsController {

    @Autowired
    private TwoFactorVerificationsService twoFactorService;

    @PostMapping("/create/{idUser}")
    public ResponseEntity<HttpResponse> createTwoFactorVerification(@PathVariable(name = "idUser") long iduser){
      TwoFactorVerificationsDTO twoFactorVerification = twoFactorService.createVerificationCode(iduser);
        return ResponseEntity.ok().
        body(HttpResponse.builder()
        .message("Verification code created")
        .status(HttpStatus.CREATED)
        .statusCode(HttpStatus.CREATED.value())
        .timeStamp(LocalDateTime.now().toString())
        .data(Map.of("verification-code",twoFactorVerification))
        .build());
    }

    // @GetMapping("/verify/{email}/{code}")
    // public ResponseEntity<HttpResponse> getTwoFactorVerification(@PathVariable("email") String email,@PathVariable("code") String code){
    //   TwoFactorVerificationsDTO twoFactorVerification = twoFactorService.getCodeVerificationByIdUserAndCode(email,code);
    //     return ResponseEntity.ok().
    //     body(HttpResponse.builder()
    //     .message("Verification code retrieved")
    //     .status(HttpStatus.OK)
    //     .statusCode(HttpStatus.OK.value())
    //     .timeStamp(LocalDateTime.now().toString())
    //     .data(Map.of("verification-code",twoFactorVerification))
    //     .build());
    // }
}
