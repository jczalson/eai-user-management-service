package com.eai.user.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eai.user.deserializer.UserConfigurationDeserializer;
import com.eai.user.dto.ConfigAttributeDTO;
import com.eai.user.repository.CustomerRepository;

@Service
public class UserConfigurationServiceImpl implements UserConfigurationService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<ConfigAttributeDTO> getUserConfigAtt(String email) {
       Optional<String> jsonFile = customerRepository.findJsonFile(email);
        if(jsonFile.isPresent()){
          List<ConfigAttributeDTO> config = UserConfigurationDeserializer.getJson(jsonFile.get());
        return config;
        }
       return null;
    }

}
