package com.eai.user.service;

import java.util.List;

import com.eai.user.dto.CustomerDTO;

public interface CustomerService {

	public List<CustomerDTO> getAllCustomer();

	public CustomerDTO getCustomerId(long id);

	public List<CustomerDTO> getCustomerByIdAddress(long idAddress);

	public CustomerDTO savCustomerDTO(CustomerDTO customerDTO);

	public String deleteCustomer(Long idCustomer);
}
