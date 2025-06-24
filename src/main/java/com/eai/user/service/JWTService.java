package com.eai.user.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.eai.user.entities.AppUser;
import com.eai.user.exception.InvalidateRequestException;

@Service
public class JWTService {

    @Autowired
    private JwtEncoder jwtAccessTokenEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    
    public Map<String, String> generateAccessToken(AppUser user) {
         Map<String, String> maps = new HashMap<>();
        try {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        Instant instant = Instant.now();
        
            if (authentication.isAuthenticated()) {
                String roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(","));
                JwtClaimsSet claims = JwtClaimsSet.builder()
                        .issuedAt(instant)
                        .expiresAt(instant.plus(30, ChronoUnit.MINUTES))
                        .issuer("http://localhost:9008/eai/api/user-management/")
                        .subject(user.getEmail())
                        .claim("scope", roles)
                        .build();
                JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters
                        .from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
                String token = jwtAccessTokenEncoder.encode(jwtEncoderParameters).getTokenValue();
              
            SecurityContextHolder.getContext().setAuthentication(authentication);
                maps.put("access-token", token);
            }
        } catch (Exception ex) {
            throw new InvalidateRequestException("User not authenticated " + ex.getMessage());
        }
        return maps;
    }

}
