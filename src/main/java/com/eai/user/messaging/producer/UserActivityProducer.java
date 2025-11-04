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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

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
            logger.info("UserDTO in JSON {}",convertToJson(userDTO));
            userActivityTemplate.send(producerRecord).get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Error occurred when produce UserDTO", e);
        }
    }


    public String convertToJson(Object obj){
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String result = null;
        try {
          result =  mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            result = "{\"error\":\"Can't convert Object to JSON\"}";
        }
     return result;
    }
}
