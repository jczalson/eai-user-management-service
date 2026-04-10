package com.eai.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.eai.user.dto.AddressDTO;
import com.eai.user.dto.CustomerDTO;
import com.eai.user.entities.CustomerEntity;
import com.eai.user.mapper.CustomerMapper;
import com.eai.user.repository.AddressRepository;
import com.eai.user.repository.CustomerRepository;
import com.eai.user.utilities.CustomerUtilities;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired(required = true)
  private AddressRepository addressRepository;

  @Autowired
  CustomerMapper customerMapper;

  @Override
  public List<CustomerDTO> getAllCustomer() {
    List<CustomerDTO> list = new ArrayList<CustomerDTO>();
    List<CustomerEntity> all = customerRepository.findAll();
    all.stream().forEach(customer -> {
      list.add(CustomerUtilities.fromCustomerEntityToDto(customer));
    });
    return list;
  }

  @Override
  public List<CustomerDTO> getCustomerByIdAddress(long idAddress) {
    List<CustomerDTO> list = new ArrayList<CustomerDTO>();
    Optional<List<CustomerEntity>> customers = customerRepository.findCustomersByIdAddress(idAddress);
    customers.get().parallelStream().forEach(customer -> {
      list.add(CustomerUtilities.fromCustomerEntityToDto(customer));
    });
    return list;
  }

  @Override
  public CustomerDTO getCustomerId(long id) {
    Optional<CustomerEntity> customer = customerRepository.findById(id);
    if (customer.isPresent()) {
      return CustomerUtilities.fromCustomerEntityToDto(customer.get());
    }
    return null;
  }

  @Override
  public CustomerDTO savCustomerDTO(CustomerDTO customerDTO) {

    List<String> errors = new ArrayList<>();
    // if (StringUtils.isNotBlank(customerDTO.getEmail())) {
    // Optional<CustomerEntity> emailExistsAlready =
    // customerRepository.findByEmail(customerDTO.getEmail());
    // if (emailExistsAlready.isPresent()) {
    // throw new InvalidateRequestException(
    // "This email " + customerDTO.getEmail() + " already exists please change it
    // ");
    // }
    // }

    // if (StringUtils.isBlank(customerDTO.getEmail())) {
    // errors.add("Email address is required");
    // throw new InvalidateRequestException("Email address is required");
    // }
    // if (StringUtils.isBlank(customerDTO.getName())) {
    // errors.add("Customer name is required");
    // throw new InvalidateRequestException("Customer name is required");
    // }
    CustomerEntity save = customerRepository.save(CustomerUtilities.fromDtoToCustomerEntity(customerDTO));
    return CustomerUtilities.fromCustomerEntityToDto(save);
  }

  @Override
  public String deleteCustomer(Long idCustomer) {
    CustomerDTO customer = getCustomerId(idCustomer);
    if (customer != null) {
      customerRepository.deleteById(customer.getIdCustomer());
      return customer.getNm();
    }
    return null;
  }

  @Override
  public CustomerDTO getCustomeByEmail(String email) {
    CustomerDTO dto = new CustomerDTO();
    AddressDTO addressDTO = new AddressDTO();
    Optional<CustomerEntity> entity = customerRepository.findByEmail(email);
    if (entity.isPresent()) {
      BeanUtils.copyProperties(entity.get(), dto);
      BeanUtils.copyProperties(entity.get().getAddress(), addressDTO);
      dto.setAddress(addressDTO);
      return dto;
    }
    return null;
  }

  // @Override
  // public Page<CustomerDTO> serachCustomers(String name, int page, int size) {
  //     // customerRepository.findByNameContaining(name, PageRequest.of(page, size));
  //   // .map(customerMapper::toDto);
  //   return null;
  // }

  @Override
  public Page<CustomerDTO> getAllPageCustomers(int page, int size) {
    Page<CustomerEntity> entities = customerRepository.findAll(PageRequest.of(page, size));
    Page<CustomerDTO> dtos = entities.map(customerMapper::toDto);
    return dtos;
  }
}
