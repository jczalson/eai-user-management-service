package com.eai.user.messaging.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.eai.user.configuration.ApplicationConfig;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  @Autowired
  private ApplicationConfig applicationConfig;

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    /**
     * Use this in production or development
     * 
     * registry.addEndpoint("/ws")
     * .setAllowedOrigins(applicationConfig.getAllowedOrigins())
     * .withSockJS();
     * 
     * registry.addEndpoint("/ws").setAllowedOriginPatterns(applicationConfig.getAllowedOrigins()).withSockJS();
     */
    /**
     * When using docker for more permissive (for development only)
     */
    registry.addEndpoint("/ws")
        .setAllowedOrigins("*")
        .withSockJS();

    registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker("/topic/message");
  }
}
