package com.eai.user.messaging.deserializer;

import java.nio.charset.StandardCharsets;

import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eai.user.dto.UserDTO;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserDtoDeserializer implements Deserializer<UserDTO> {

    private Logger logger = LoggerFactory.getLogger(UserDtoDeserializer.class);

    @Override
    public UserDTO deserialize(String topic, byte[] data) {
        String message  = new String(data,StandardCharsets.UTF_8);
        logger.info("json received from {}",message);
        UserDTO userDTO = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            userDTO = mapper.readValue(data, UserDTO.class);
        } catch (Exception e) {
            logger.error("Error deserializing UserDTO", e);
        }
        return userDTO;
    }

}
