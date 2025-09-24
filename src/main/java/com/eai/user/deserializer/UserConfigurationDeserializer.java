package com.eai.user.deserializer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eai.user.dto.ConfigAttributeDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserConfigurationDeserializer {
    private static Logger logger = LoggerFactory.getLogger(UserConfigurationDeserializer.class);

    public static List<ConfigAttributeDTO> getJson(String json) {
        List<ConfigAttributeDTO> configList = null;
        logger.info("Config JSON {}",json);
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            configList = mapper.readValue(json, new TypeReference<List<ConfigAttributeDTO>>() {
            });
            return configList;
        } catch (Exception ex) {
            logger.warn("Error occured during deserialization of json", ex);
        }
        return null;
    }
}
