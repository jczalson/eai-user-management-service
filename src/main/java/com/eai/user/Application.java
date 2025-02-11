package com.eai.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import com.eai.user.configuration.ApplicationConfig;

@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties(ApplicationConfig.class)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
CommandLineRunner runner = new CommandLineRunner() {
	
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		
	}
};
}
