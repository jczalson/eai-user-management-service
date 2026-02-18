package com.eai.user.filter;

import static java.util.Arrays.stream;
import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.eai.user.dto.UserDTO;
import com.eai.user.exception.ExceptionUtils;
import com.eai.user.repository.AppUserRepository;
import com.eai.user.service.JWTService;
import com.eai.user.utilities.AccountUtilities;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    @Autowired
    @Lazy
    private JWTService jwtService;

    @Autowired
    private AppUserRepository appUserRepository;

    /**
     * /account/refresh/token is whitelisted because 
     * don't need to be filtererd as it doesn't have authorities
     */
    private static final String[] PUBLIC_URLS = { "/account/register","/account/file",
            "/account/login", "/v3/api-docs/", "/account/verify/","/account/refresh/token",
            "/swagger-ui/","/account/user/image","/event/event/add","/event/user/events/add",
            "/event/user/event/","/event/user/events",
            "/swagger-ui.html", "/actuator/", "/ws/", "/url/", "/url-conf/" };

    protected static final String TOKEN_PREFIX = "Bearer ";

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getHeader(AUTHORIZATION) == null
                || !request.getHeader(AUTHORIZATION).startsWith(TOKEN_PREFIX)
                || Arrays.asList(PUBLIC_URLS).contains(request.getServletPath())
                || request.getMethod().equalsIgnoreCase("OPTIONS");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = getToken(request);
            Long userId = jwtService.extractUserIdFromToken(getToken(request));
            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDTO user = AccountUtilities.fromUserEntityToDto(appUserRepository.findById(userId).get());
                if (jwtService.validateToken(token, user)) {
                    String[] roles = jwtService.extractAuthoritiesFromToken(token).split(",");
                    List<GrantedAuthority> authorities = stream(roles).map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
                    Authentication authentication = jwtService.getAuthentication(user.getIdUser(), authorities,
                            request);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    SecurityContextHolder.clearContext();
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Error in filter {}", e.getMessage());
            try {
                ExceptionUtils.processError(request, response, e);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    private String getToken(HttpServletRequest request) {
        String token = null;
        String header = request.getHeader(AUTHORIZATION);
        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            token = header.replace(TOKEN_PREFIX, EMPTY);
        }
        return token;
    }

}
