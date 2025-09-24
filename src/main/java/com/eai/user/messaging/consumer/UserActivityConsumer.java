package com.eai.user.messaging.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.eai.user.dto.UserDTO;

@Service
public class UserActivityConsumer {

     private Logger logger =LoggerFactory.getLogger(UserActivityConsumer.class);

    @Value("${userActivity.stomp.topic}")
    private String userActivityStompTopic = null;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics="#{'${userActivity.kafka.topic}'}",containerFactory="kafkaUserActivityListenerContainerFactory")
    public void consumeUserActivity(UserDTO userDTO){
        logger.info("User DTO consumed {}",userDTO);

        if(userDTO.getUserName() != null){
           messagingTemplate.convertAndSend(userActivityStompTopic +userDTO.getUserName().split("@")[0], userDTO);
        logger.info("End: Message processed by UserDTO stomp consumer: {}", userDTO);
        }
    }
}
