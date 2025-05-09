package com.eai.user.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.eai.user.dto.AddressDTO;
import com.eai.user.dto.CustomerDTO;
import com.eai.user.service.AddressService;
import com.eai.user.service.CustomerService;

@RestController
public class UserController {
	private static Logger log =LoggerFactory.getLogger(UserController.class);
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private AddressService addressService;
	
	
	@GetMapping("/customers")
	public ResponseEntity<List<CustomerDTO>> getAllCustomers(){
		List<CustomerDTO> all = customerService.getAllCustomer();
		log.info("All customers:{}",all);
		return ResponseEntity.ok(all);
	}
	
	@GetMapping("/addresses")
	public ResponseEntity<List<AddressDTO>> getAllAddresses(){
		List<AddressDTO> all = addressService.getAllAddresses();
		log.info("All addresses:{}",all);
		return ResponseEntity.ok(all);
		
	}
	
	@GetMapping("/addresse/{id}")
	public ResponseEntity<AddressDTO> getAddressByID(@PathVariable Long id){
		AddressDTO dto = addressService.getAddressById(id);
		log.info("The address:{}",dto);
		return ResponseEntity.ok(dto);
		
	}
	
	
	  @GetMapping("/auth") public Authentication getAuth(Authentication
	  authentication) { return authentication; }
	 
}
