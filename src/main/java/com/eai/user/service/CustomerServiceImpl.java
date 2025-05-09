package com.eai.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eai.user.dto.CustomerDTO;
import com.eai.user.entities.CustomerEntity;
import com.eai.user.repository.CustomerRepository;
import com.eai.user.utilities.CustomerUtilities;

@Service
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	private CustomerRepository customerRepository;
	

	@Override
	public List<CustomerDTO> getAllCustomer() {
		List<CustomerDTO>  list = new ArrayList<CustomerDTO>();
		List<CustomerEntity> all = customerRepository.findAll();
		all.stream().forEach(customer->{ list.add(CustomerUtilities.fromCustomerEntityToDto(customer));});
		return list;
	}

}
