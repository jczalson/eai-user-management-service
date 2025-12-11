package com.eai.user;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;

import com.eai.user.configuration.ApplicationConfig;
import com.eai.user.dto.ConfigAttributeDTO;
import com.eai.user.repository.CustomerRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
@EnableDiscoveryClient
@RefreshScope
@EnableConfigurationProperties(ApplicationConfig.class)
public class Application {

	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private Logger logger = LoggerFactory.getLogger(Application.class);
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	// @Bean
	// PasswordEncoder passwordEncoder(){
	// return new BCryptPasswordEncoder();
	// }

	@Bean
	CommandLineRunner CommandLineRunner(@Autowired CustomerRepository customerRepository) {

		return args -> {
			// AppRole appRole1 = new AppRole();
			// appRole1.setRoleName("USER");
			// accountService.addRole(appRole1);

			// AppRole appRole2 = new AppRole();
			// appRole2.setRoleName("ADMIN");
			// accountService.addRole(appRole2);

			// AppUser appUser1 = new AppUser();
			// appUser1.setEmail("koko@gmail.com");
			// appUser1.setPassword("1234");
			// appUser1.setUserStatusEnum(UserStatusEnum.CREATED);
			// accountService.addUser(appUser1);

			// AppUser appUser2 = new AppUser();
			// appUser2.setEmail("zale@gmail.com");
			// appUser2.setPassword("1234");
			// appUser2.setUserStatusEnum(UserStatusEnum.ACTIVE);
			// accountService.addUser(appUser2);

			// AppUser appUser3 = new AppUser();
			// appUser3.setEmail("jc@gmail.com");
			// appUser3.setPassword("1234");
			// appUser3.setUserStatusEnum(UserStatusEnum.CREATED);
			// accountService.addUser(appUser3);

			// accountService.addRoleToUser(appUser1.getEmail(), "USER");
			// accountService.addRoleToUser(appUser1.getEmail(), "ADMIN");

			// accountService.addRoleToUser(appUser2.getEmail(), "USER");
			// accountService.addRoleToUser(appUser3.getEmail(), "ADMIN");
			// getJsonFile(customerRepository);
		};
	}

	private List<ConfigAttributeDTO> getJsonFile(CustomerRepository customerRepository) {
		Optional<String> jsonFile = customerRepository.findJsonFile("jc@gmail.com");
		if (jsonFile.isPresent()) {
			List<ConfigAttributeDTO> config = null;
			try{
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
			 config = mapper.readValue(jsonFile.get(),new TypeReference<List<ConfigAttributeDTO>>(){});
			}catch(Exception ex){
logger.warn("Exception while deserializing Configuration Object, Disregard this exception", ex);
			}
			return config;
		}
		return null;
	}

}
