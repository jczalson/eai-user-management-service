package com.eai.user.messaging.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.eai.user.dto.UserDTO;

@Service
public class UserActivityConsumer {

    private Logger logger =LoggerFactory.getLogger(UserActivityConsumer.class);

    @KafkaListener(topics="#{'${userActivity.kafka.topic}'}",containerFactory="kafkaUserActivityListenerContainerFactory")
    public void consumeUserActivity(UserDTO userDTO){
        logger.info("User DTO consumed {}",userDTO);
    }
}
