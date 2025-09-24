package com.eai.user.service;

import java.util.List;

import com.eai.user.dto.ConfigAttributeDTO;

public interface UserConfigurationService {

    public List<ConfigAttributeDTO> getUserConfigAtt(String email);
}
