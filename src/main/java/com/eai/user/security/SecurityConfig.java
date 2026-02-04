package com.eai.user.security;

import java.util.Arrays;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.eai.user.configuration.ApplicationConfig;
import com.eai.user.filter.CustomAuthorizationFilter;
import com.eai.user.handler.CustomAccessDeniedHandler;
import com.eai.user.handler.CustomAuthEntryPoint;
import com.eai.user.service.MyCustomeUserService;
import com.nimbusds.jose.jwk.source.ImmutableSecret;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    
    @Autowired
    ApplicationConfig applicationConfig;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Autowired
    private  MyCustomeUserService myCustomeUserService;
    
    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private CustomAuthEntryPoint customAuthEntryPoint;

    @Autowired
    private CustomAuthorizationFilter customAuthorizationFilter;

    /**
     * /account/refresh/token/ is whitelisted because 
     * don't need to be filtererd as it doesn't have authorities
     */
    private static final String [] PUBLIC_URLS = {"/account/register","/account/register",
                   "/account/login","/v3/api-docs/**",
                   "/swagger-ui/**","/account/verify/code/**","/account/user/image/**",
                    "/swagger-ui.html", "/actuator/**",
                    "/ws/**","/ws/info/**",
                    "/url/**","/url-conf/**","account/refresh/token/**"};

                    // private static final String [] PUBLIC_URLS = {"/**"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(customizer -> customizer.disable())
                .authorizeHttpRequests(request -> request
                .requestMatchers(PUBLIC_URLS)
                .permitAll())
                .authorizeHttpRequests(request -> request.anyRequest().authenticated())
                .headers(headers -> headers.frameOptions(op -> op.disable()))
                .cors(cors -> cors.configurationSource(configurerCorsConfigurer()))
                // .oauth2ResourceServer(oaut ->oaut.jwt(Customizer.withDefaults())) 
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> 
                   exception.accessDeniedHandler(accessDeniedHandler)
                   .authenticationEntryPoint(customAuthEntryPoint))
                   .addFilterBefore(customAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                 .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtEncoder jwtAccessTokenEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(secretKey.getBytes()));
    }

    @Bean
    public JwtDecoder jwtAccessTokenDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "RSA");
        return NimbusJwtDecoder.withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS256).build();
    }

   
    @SuppressWarnings("deprecation")
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(myCustomeUserService);
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public CorsConfigurationSource configurerCorsConfigurer() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowCredentials(true);
        corsConfig.setAllowedMethods(Arrays.asList("*"));
        corsConfig.setAllowedOrigins(List.of(applicationConfig.getAllowedOrigins()));
        //This part is added to solve CORS issue for Docker
        corsConfig.addAllowedOriginPattern("*");
        corsConfig.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }
}
