package com.eai.user.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

import com.eai.user.configuration.ApplicationConfig;
import com.eai.user.dto.ConfigurationDTO;

@RestController
@RequestScope
public class ConfigController {


	@Autowired
	private ApplicationConfig applicationConfig;

	@GetMapping("/url")
	public Map<String, String[]> getApiUrl() {
		return Map.of("API-URL", applicationConfig.getAllowedOrigins(),"UI-API",new String[]{applicationConfig.getUi().getApiUrl()});
	}
	
	@GetMapping("/url-conf")
	public String[]  getApiUrlConfig() {
		return applicationConfig.getAllowedOrigins();
	}

  @GetMapping("/configurations")
	public ResponseEntity<ConfigurationDTO>  getConfigurations() {
    if (applicationConfig.getUi() == null) {
            return ResponseEntity.internalServerError().build();
        }
   ConfigurationDTO dto = new ConfigurationDTO();
   dto.UMS_API_URL = applicationConfig.getUi().getApiUrl();
		return ResponseEntity.ok(dto);
	}
}
