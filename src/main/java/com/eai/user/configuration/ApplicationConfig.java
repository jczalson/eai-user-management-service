package com.eai.user.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@RefreshScope
@ConfigurationProperties(prefix = "eai")
public class ApplicationConfig {

	private UI ui;
	
	
	  @Getter
	  @Setter
	public static class UI {
		private String apiUrl;
	}

}
