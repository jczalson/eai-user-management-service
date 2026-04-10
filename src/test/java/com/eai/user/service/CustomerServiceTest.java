package com.eai.user.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.eai.user.dto.CustomerDTO;
import com.eai.user.entities.AddressEntity;
import com.eai.user.entities.CustomerEntity;
import com.eai.user.entities.CustomerType;
import com.eai.user.repository.CustomerRepository;
import com.eai.user.utilities.CustomerUtilities;

@ExtendWith(SpringExtension.class)
public class CustomerServiceTest {

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Test
    public void testGetCustomerByIdAddress() {
        List<CustomerEntity> customerList = new ArrayList<>();
        customerList.add(createCustomerEntity());
        Optional<List<CustomerEntity>> optional = Optional.of(customerList);
        when(customerRepository.findCustomersByIdAddress(1))
                .thenReturn(optional);
        List<CustomerDTO> list = customerService.getCustomerByIdAddress(1);
        assertNotNull(list);
        Assertions.assertEquals(list.get(0).getIdCustomer(), createCustomerEntity().getIdCustomer());
    }

    @Test
    public void testSaveCustomer() {
        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(createCustomerEntity());
        CustomerDTO savCustomerDTO = customerService
                .savCustomerDTO(CustomerUtilities.fromCustomerEntityToDto(createCustomerEntity()));
        assertNotNull(savCustomerDTO);
    }

    // @Test
    // public void testSaveCustomerEmailNull() {
    //     when(customerRepository.save(any(CustomerEntity.class))).thenReturn(createCustomerEmailNull());
    //    assertThrows(RestApiException.class, ()->customerService
    //             .savCustomerDTO(CustomerUtilities.fromCustomerEntityToDto(createCustomerEmailNull())));
    // }

     @Test
    public void testDeleteCustomer() {
       Optional<CustomerEntity> optional = Optional.of(createCustomerEntity());
       when(customerRepository.findById(1L)).thenReturn(optional);
       String deletedUserName = customerService.deleteCustomer(optional.get().getIdCustomer());
        Assertions.assertEquals(optional.get().getNm(),deletedUserName);
    }

    @Test
    public void testGetCustomerByEmail(){
        when(customerRepository.findByEmail("jc@gmail.com")).thenReturn(Optional.of(createCustomerEntity()));
        CustomerDTO customeByEmail = customerService.getCustomeByEmail("jc@gmail.com");
        assertNotNull(customeByEmail);
        Assertions.assertEquals("jc@gmail.com", customeByEmail.getEmail());
    }

    //  @Test
    // public void testGetCustomerByEmailAlreadyExist(){
    //     when(customerRepository.findByEmail("jc@gmail.com")).thenReturn(Optional.of(createCustomerEntity()));
    //    assertThrows(RestApiException.class, ()->customerService.savCustomerDTO(CustomerUtilities.fromCustomerEntityToDto(createCustomerEntity())));
    // }
    private CustomerEntity createCustomerEntity() {
        CustomerEntity cust = new CustomerEntity();
        cust.setIdCustomer(1L);
        cust.setEmail("jc@gmail.com");
        cust.setCustomerType(CustomerType.ACTIVE);
        cust.setNm("jc");
        AddressEntity address = new AddressEntity();
        address.setIdAddress(1L);
        address.setCity("FERRARA");
        cust.setAddress(address);
        return cust;
    }


     private CustomerEntity createCustomerEmailNull() {
        CustomerEntity cust = new CustomerEntity();
        cust.setIdCustomer(1L);
        // cust.setEmail("jc@mail.com");
        cust.setCustomerType(CustomerType.ACTIVE);
        cust.setNm("jc");
        AddressEntity address = new AddressEntity();
        address.setIdAddress(1L);
        cust.setAddress(address);
        return cust;
    }

}
