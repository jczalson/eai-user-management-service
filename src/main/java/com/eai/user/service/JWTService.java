package com.eai.user.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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
                .expiresAt(instant.plus(10, ChronoUnit.MINUTES))
                .issuer("http://localhost:9008/eai/api/user-management/")
                .subject(String.valueOf(user.getUser().getIdUser()))
                .claim("scope", roles)
                .build();
        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters
                .from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
        String token = jwtAccessTokenEncoder.encode(jwtEncoderParameters).getTokenValue();

        return token;
    }

    public Long extractUserIdFromToken(String token) {
        Long userId = null;
        try {
            if(token !=null){
            Jwt jwt = jwtDecoder.decode(token);
            userId = Long.parseLong(jwt.getSubject());
            }
        } catch (JwtException e) {
            logger.error("Error occured for JWT decoder maybe the token is expired {}", e.getLocalizedMessage());
        throw new JwtException(e.getMessage());
        }
        return userId;
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

    public boolean validateToken(String token, UserDTO user) {
        Long userId = token !=null? extractUserIdFromToken(token): null;
        if (userId != null && userId.equals(new Long(user.getIdUser()))) {
            return true;
        }
        return false;
    }

    public String generateRefreshToken(UserPrincipal user) {
        Instant instant = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(instant)
                .expiresAt(instant.plus(50, ChronoUnit.MINUTES))
                .issuer("http://localhost:9008/eai/api/user-management/")
                .subject(String.valueOf(user.getUser().getIdUser()))
                .claim("scope", "No need for refresh token")
                .build();
        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters
                .from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
        String token = jwtAccessTokenEncoder.encode(jwtEncoderParameters).getTokenValue();
        return token;
    }

    public Authentication getAuthentication(Long userId, List<GrantedAuthority> authorities, HttpServletRequest request) throws Exception{
     UsernamePasswordAuthenticationToken userToken= new UsernamePasswordAuthenticationToken(accountService.getUserById(userId), null, authorities);
    userToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
     return userToken;
    }
}
