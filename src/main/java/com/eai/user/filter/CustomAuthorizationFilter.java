package com.eai.user.filter;

import static java.util.Arrays.stream;
import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.eai.user.service.JWTService;
import com.eai.user.service.MyCustomeUserService;
import com.eai.user.utilities.ExceptionUtils;

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
    private ApplicationContext context;

    private static final String[] PUBLIC_URLS = { "/account/register",
             "/account/login","/v3/api-docs/", "/account/verify/",
            "/swagger-ui/",
            "/swagger-ui.html", "/actuator/", "/ws/", "/url/", "/url-conf/" };

    protected static final String TOKEN_PREFIX = "Bearer ";
    protected static final String TOKEN_KEY = "token";
    protected static final String USERNAME_KEY = "username";

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getHeader(AUTHORIZATION) == null
                || !request.getHeader(AUTHORIZATION).startsWith(TOKEN_PREFIX)
                ||  Arrays.asList(PUBLIC_URLS).contains(request.getServletPath())
                || request.getMethod().equalsIgnoreCase("OPTIONS");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException{
        try {
            Map<String, String> values = getRequestValues(request);
            String token = getToken(request);
            if (values.get(USERNAME_KEY) != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = context.getBean(MyCustomeUserService.class)
                        .loadUserByUsername(values.get(USERNAME_KEY));
                if (jwtService.validateToken(token, userDetails)) {
                    String[] roles = jwtService.extractAuthoritiesFromToken(values.get(TOKEN_KEY)).split(",");
                    List<GrantedAuthority> authorities = stream(roles).map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
                    Authentication authentication = jwtService.getAuthentication(values.get(USERNAME_KEY), authorities,
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

    private Map<String, String> getRequestValues(HttpServletRequest request) {
        return Map.of(USERNAME_KEY, jwtService.extractUserNameFromToken(getToken(request)), TOKEN_KEY,
                getToken(request));
    }

    private String getToken(HttpServletRequest request) {
        String token = null;
        String header = request.getHeader(AUTHORIZATION);
        if (header !=null && header.startsWith(TOKEN_PREFIX)) {
            token = header.replace(TOKEN_PREFIX, EMPTY);
        }
        return token;
    }

}
