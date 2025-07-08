package com.eai.user.messaging.producer;

import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.eai.user.dto.UserDTO;

@Service
public class UserActivityProducer {

    private Logger logger = LoggerFactory.getLogger(UserActivityProducer.class);

    @Value("${userActivity.kafka.topic}")
    private String userActivityTopic;

    @Autowired
    private KafkaTemplate<String, UserDTO> userActivityTemplate;

    public void sendUserActivityMessage(UserDTO userDTO) {
        if (userDTO == null) {
            logger.warn("UserDTO is empty or null");
            return;
        }

        
        ProducerRecord<String, UserDTO> producerRecord 
        = new ProducerRecord<String,UserDTO>(userActivityTopic,null,
                userDTO.getName(),userDTO,null);
        try {
            userActivityTemplate.send(producerRecord).get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Error occurred when produce UserDTO", e);
        }
    }
}
