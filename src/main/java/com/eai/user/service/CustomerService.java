package com.eai.user.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.eai.user.dto.CustomerDTO;
import com.eai.user.dto.PageDTO;

public interface CustomerService {

	public List<CustomerDTO> getAllCustomer();

	public CustomerDTO getCustomerId(long id);

	public List<CustomerDTO> getCustomerByIdAddress(long idAddress);

	public CustomerDTO savCustomerDTO(CustomerDTO customerDTO);

	public String deleteCustomer(Long idCustomer);

	public CustomerDTO getCustomeByEmail(String email);

  // public Page<CustomerDTO> serachCustomers(String name,int page, int size);

  public PageDTO<CustomerDTO> getAllPageCustomers(int page, int size);
}
