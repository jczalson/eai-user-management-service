package com.eai.user.messaging.deserializer;

import java.nio.charset.StandardCharsets;
import org.apache.kafka.common.serialization.Deserializer;
import com.eai.user.dto.UserDTO;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserDtoDeserializer implements Deserializer<UserDTO> {

  @Override
  public UserDTO deserialize(String topic, byte[] data) {
    String message = new String(data, StandardCharsets.UTF_8);
    log.info("json received from {}", message);
    UserDTO userDTO = null;
    try {
      ObjectMapper mapper = new ObjectMapper();
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      // the same with above
      // mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
      userDTO = mapper.readValue(data, UserDTO.class);
    } catch (Exception e) {
      log.error("Error deserializing UserDTO", e);
    }
    return userDTO;
  }
}
