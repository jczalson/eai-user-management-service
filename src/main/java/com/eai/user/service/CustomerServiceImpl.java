package com.eai.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eai.user.dto.AddressDTO;
import com.eai.user.dto.CustomerDTO;
import com.eai.user.entities.AddressEntity;
import com.eai.user.entities.CustomerEntity;
import com.eai.user.exception.InvalidateRequestException;
import com.eai.user.repository.AddressRepository;
import com.eai.user.repository.CustomerRepository;
import com.eai.user.utilities.AddressUtilities;
import com.eai.user.utilities.CustomerUtilities;

@Service
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private AddressRepository addressRepository;
	

	@Override
	public List<CustomerDTO> getAllCustomer() {
		List<CustomerDTO>  list = new ArrayList<CustomerDTO>();
		List<CustomerEntity> all = customerRepository.findAll();
		all.stream().forEach(customer->{ list.add(CustomerUtilities.fromCustomerEntityToDto(customer));});
		return list;
	}

	@Override
	public List<CustomerDTO> getCustomerByIdAddress(long idAddress) {
		List<CustomerDTO>  list = new ArrayList<CustomerDTO>();
		Optional<List<CustomerEntity>> customers = customerRepository.findCustomersByIdAddress(idAddress);
			customers.get().stream().forEach(customer->{ list.add(CustomerUtilities.fromCustomerEntityToDto(customer));});
		return list;
	}


	@Override
	public CustomerDTO getCustomerId(long id) {
		Optional<CustomerEntity> customer = customerRepository.findById(id);
		if(customer.isPresent()){
			return CustomerUtilities.fromCustomerEntityToDto(customer.get());
		}
		return null;
	}

	@Override
	public CustomerDTO savCustomerDTO(CustomerDTO customerDTO) {
	
	List<String> errors = new ArrayList<>();	
	if(StringUtils.isBlank(customerDTO.getEmail())){
		errors.add("Email address is required");
			throw new  InvalidateRequestException("Email address is required");
		}
		if(StringUtils.isBlank(customerDTO.getName())){
			errors.add("Customer name is required");
			throw new InvalidateRequestException("Customer name is required");
		}
         CustomerEntity save = customerRepository.save(CustomerUtilities.fromDtoToCustomerEntity(customerDTO));
		
	return CustomerUtilities.fromCustomerEntityToDto(save);
	}
}
