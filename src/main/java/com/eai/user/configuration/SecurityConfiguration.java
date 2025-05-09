/* package com.eai.user.configuration;

// import org.springframework.security.config.Customizer;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.Customizer;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    @Autowired
    ApplicationConfig applicationConfig;

    // @Bean
    // SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    // http.csrf().disable()
    // .authorizeHttpRequests(ar->ar.requestMatchers("/h2/**","/actuator/**","/css/**").permitAll())
    // .oauth2Login(Customizer.withDefaults())
    // .cors(cors->cors.configurationSource(null));
    // return http.build();

    // }






    // @Bean
    // CorsConfigurationSource corsConfigurationSource(){
    // CorsConfiguration configuration = new CorsConfiguration();
    // configuration.setAllowCredentials(true);
    // configuration.setAllowedHeaders(Collections.singletonList("*"));
    // configuration.setAllowedMethods(Collections.singletonList("*"));
    // configuration.setAllowedOrigins(Arrays.asList(applicationConfig.getUi().getApiUrl()));
    // final UrlBasedCorsConfigurationSource basedCorsConfigurationSource = new
    // UrlBasedCorsConfigurationSource();
    // basedCorsConfigurationSource.registerCorsConfiguration("/**",configuration);
    // return basedCorsConfigurationSource;
    // }

}
 */