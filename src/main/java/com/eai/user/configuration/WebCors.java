package com.eai.user.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * This works pnly if there is no security resource involved
 * Otherwise we have to use corsConfiguration
 */
@Configuration
public class WebCors{

    @Autowired
    ApplicationConfig applicationConfig;

    @Bean
    WebMvcConfigurer corsConfigure() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowCredentials(true)
                        .allowedHeaders("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedOrigins(applicationConfig.getUi().getApiUrl());

            }
        };
    }
}
