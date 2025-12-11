package com.eai.user.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import com.eai.user.dto.UserDTO;
import com.eai.user.entities.UserPrincipal;
import com.eai.user.exception.InvalidateRequestException;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class JWTService {

    private Logger logger = LoggerFactory.getLogger(JWTService.class);
    @Autowired
    private JwtEncoder jwtAccessTokenEncoder;

    @Autowired
    private JwtDecoder jwtDecoder;

     @Autowired
    private AccountService accountService;

    public String generateAccessToken(UserPrincipal user) {
        Instant instant = Instant.now();
        String roles = user.getAuthorities().stream().map(role -> role.toString()).collect(Collectors.joining(","));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(instant)
                .expiresAt(instant.plus(30, ChronoUnit.MINUTES))
                .issuer("http://localhost:9008/eai/api/user-management/")
                .subject(user.getUsername())
                .claim("scope", roles)
                .build();
        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters
                .from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
        String token = jwtAccessTokenEncoder.encode(jwtEncoderParameters).getTokenValue();

        return token;
    }

    public String extractUserNameFromToken(String token) {
        String userName = null;
        try {
            if(token !=null){
            Jwt jwt = jwtDecoder.decode(token);
            userName = jwt.getSubject();
            }
        } catch (JwtException e) {
            logger.error("Error occured for JWT decoder maybe the token is expired {}", e.getLocalizedMessage());
        throw new InvalidateRequestException(e.getMessage());
        }
        return userName;
    }

    public String extractAuthoritiesFromToken(String token) {
        String  autorities = null;
        try {
            Jwt jwt = jwtDecoder.decode(token);
              autorities =  (String) jwt.getClaims().get("scope");
             logger.info("claims "+autorities.toString() );
        } catch (JwtException e) {
            logger.error("Error occured for JWT decoder maybe the token is expired {}", e.getLocalizedMessage());
        }
        return autorities;
    }

    public boolean validateToken(String token, UserDetails userDetails) {
         
        String userName = token !=null? extractUserNameFromToken(token): null;
        if (userName != null && userName.equals(userDetails.getUsername())) {
            return true;
        }
        return false;
    }

    public String generateRefreshToken(UserPrincipal user) {
        
        String roles = user.getAuthorities().stream().map(role->role.toString()).collect(Collectors.joining(","));
        Instant instant = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(instant)
                .expiresAt(instant.plus(120, ChronoUnit.MINUTES))
                .issuer("http://localhost:9008/eai/api/user-management/")
                .subject(user.getUsername())
                .claim("scope", roles)
                .build();
        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters
                .from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
        String token = jwtAccessTokenEncoder.encode(jwtEncoderParameters).getTokenValue();
        return token;
    }

    public Authentication getAuthentication(String email, List<GrantedAuthority> authorities, HttpServletRequest request) throws Exception{
     UsernamePasswordAuthenticationToken userToken= new UsernamePasswordAuthenticationToken(accountService.loadUserByUsername(email), null, authorities);
    userToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
     return userToken;
    }
}
