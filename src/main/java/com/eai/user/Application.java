package com.eai.user;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import com.eai.user.configuration.ApplicationConfig;
import com.eai.user.entities.AddressEntity;
import com.eai.user.entities.CustomerEntity;
import com.eai.user.entities.CustomerType;
import com.eai.user.repository.AddressRepository;
import com.eai.user.repository.CustomerRepository;

@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties(ApplicationConfig.class)
public class Application {

	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	//   @Bean CommandLineRunner CommandLineRunner(CustomerRepository
	//   customerRepository, AddressRepository addressRepository) {
	  
	//   return args ->{ AddressEntity address = new AddressEntity();
	//   address.setCity("Brussels"); address.setCountry("Belgium");
	//   address.setStreetName("rue Dome 2"); address.setZipCode(7090L);
	//   addressRepository.save(address);
	  
	//   AddressEntity address1 = new AddressEntity();
	//   address1.setCity("Brussels"); address1.setCountry("Belgium");
	//   address1.setStreetName("rue sainte barbe 108"); address1.setZipCode(1400L);
	//   addressRepository.save(address1);
	  
	//   CustomerEntity customerEntity = new CustomerEntity();
	//   customerEntity.setCreatedDate(Timestamp.valueOf(LocalDateTime.now(ZoneId.of(
	//   "UTC")))); customerEntity.setCustomerType(CustomerType.CREATED);
	//   customerEntity.setEmail("zale@gmail.com"); customerEntity.setName("ZZ");
	//   customerEntity.setAddressEntity(address);
	//   customerRepository.save(customerEntity);
	  
	//   CustomerEntity customerEntity1 = new CustomerEntity();
	//   customerEntity1.setCreatedDate(Timestamp.valueOf(LocalDateTime.now(ZoneId.of(
	//   "UTC")))); customerEntity1.setCustomerType(CustomerType.ACTIVE);
	//   customerEntity1.setEmail("jc@gmail.com"); customerEntity1.setName("JC");
	//   customerEntity1.setAddressEntity(address1);
	//   customerRepository.save(customerEntity1);
	  
	//   }; }
	 
	  
}
