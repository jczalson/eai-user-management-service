package com.eai.user.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

import com.eai.user.configuration.ApplicationConfig;

@RestController
@RequestScope
public class ConfigController {


	@Autowired
	private ApplicationConfig applicationConfig;

	@GetMapping("/url")
	public Map<String, String[]> getApiUrl() {
		return Map.of("API-URL", applicationConfig.getAllowedOrigins());
	}
	
	@GetMapping("/url-conf")
	public String[]  getApiUrlConfig() {
		return applicationConfig.getAllowedOrigins();
	}

}
