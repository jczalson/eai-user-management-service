package com.eai.user.messaging.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.eai.user.dto.UserDTO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserActivityConsumer {

    @Value("${userActivity.stomp.topic}")
    private String userActivityStompTopic = null;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics="#{'${userActivity.kafka.topic}'}",containerFactory="kafkaUserActivityListenerContainerFactory")
    public void consumeUserActivity(UserDTO userDTO){
        log.info("User DTO consumed {}",userDTO);

        if(userDTO.getEmail() != null){
          // "50" must be the login user ID
          // and must be is String type
           messagingTemplate.convertAndSend(userActivityStompTopic+"50", userDTO);
        log.info("End: Message processed by UserDTO stomp consumer: {}", userDTO);
        }
    }
}
