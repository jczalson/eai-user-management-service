package com.eai.user.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eai.user.dto.CustomerDTO;
import com.eai.user.entities.HttpResponse;
import com.eai.user.service.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/customers")
public class UserController {
	private static Logger log =LoggerFactory.getLogger(UserController.class);

	@Autowired
	private CustomerService customerService;
	
	
	@GetMapping("/all")
	public ResponseEntity<List<CustomerDTO>> getAllCustomers(){
		List<CustomerDTO> all = customerService.getAllCustomer();
		log.info("All customers:{}",all);
		return ResponseEntity.ok(all);
	}

	@DeleteMapping("/del/{id}")
	public ResponseEntity<String> deleteCustomer(@PathVariable Long id){
       String deleteCustomer = customerService.deleteCustomer(id);
		return ResponseEntity.ok(deleteCustomer);
	}
	@GetMapping("/address/{id}")
	public ResponseEntity<HttpResponse> getAllCustomersByIdAddress(@PathVariable long id){
		List<CustomerDTO> all = customerService.getCustomerByIdAddress(id);
		log.info("All customers by Address Id:{}",all);
		return ResponseEntity.ok().body(
		HttpResponse.builder()
		.timeStamp(LocalDateTime.now().toString())
		.data(Map.of("customers", all))
		.message("All customers by Address")
		.status(HttpStatus.OK)
		.statusCode(HttpStatus.OK.value())
	    .build());
	}

	@GetMapping("/{id}")
	public ResponseEntity<HttpResponse> getCustomerById(@PathVariable long id){
		CustomerDTO customer = customerService.getCustomerId(id);
		log.info("customer:{}",customer);
		return ResponseEntity.ok().body(
		HttpResponse.builder()
		.timeStamp(LocalDateTime.now().toString())
		.data(Map.of("customer", customer))
		.message("Customer details")
		.status(HttpStatus.FOUND)
		.statusCode(HttpStatus.FOUND.value())
	    .build());
	}
	
	
	  @GetMapping("/auth") public Authentication getAuth(Authentication
	  authentication) { return authentication; }
	 
	@PostMapping("/createUser")
	  public ResponseEntity<HttpResponse> createUser(@RequestBody @Valid CustomerDTO customerDTO) throws Exception{
     TimeUnit.SECONDS.sleep(3);
		CustomerDTO customerDTO2 = customerService.savCustomerDTO(customerDTO);
	  log.info("customer:{}",customerDTO2);
	  return ResponseEntity.ok().body(
		HttpResponse.builder()
		.timeStamp(LocalDateTime.now().toString())
		.data(Map.of("user", customerDTO2))
		.message("customer created")
		.status(HttpStatus.CREATED)
		.statusCode(HttpStatus.CREATED.value())
	    .build());
	  }
}
