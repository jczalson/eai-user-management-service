package com.eai.user.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eai.user.configuration.ApplicationConfig;

@RestController
public class ConfigController {

	@Value("${eai.ui.api-url}")
	private String apiUrl;

	@Autowired
	private ApplicationConfig applicationConfig;

	@GetMapping("/url")
	public Map<String, String> getApiUrl() {
		return Map.of("API-URL", apiUrl);
	}
	
	@GetMapping("/url-conf")
	public String  getApiUrlConfig() {
		return applicationConfig.getUi().getApiUrl();
	}

}
