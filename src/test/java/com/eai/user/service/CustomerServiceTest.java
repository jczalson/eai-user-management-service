package com.eai.user.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import com.eai.user.exception.InvalidateRequestException;
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
        customerList.add(createCustomer());
        Optional<List<CustomerEntity>> optional = Optional.of(customerList);
        when(customerRepository.findCustomersByIdAddress(1))
                .thenReturn(optional);
        List<CustomerDTO> list = customerService.getCustomerByIdAddress(1);
        assertNotNull(list);
        Assertions.assertEquals(list.get(0).getCustomerId(), createCustomer().getCustomerId());
    }

    @Test
    public void testSaveCustomer() {
        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(createCustomer());
        CustomerDTO savCustomerDTO = customerService
                .savCustomerDTO(CustomerUtilities.fromCustomerEntityToDto(createCustomer()));
        assertNotNull(savCustomerDTO);
    }

    @Test
    public void testSaveCustomerEmailNull() {
        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(createCustomerEmailNull());
       assertThrows(InvalidateRequestException.class, ()->customerService
                .savCustomerDTO(CustomerUtilities.fromCustomerEntityToDto(createCustomerEmailNull())));
    }

     @Test
    public void testDeleteCustomer() {
       Optional<CustomerEntity> optional = Optional.of(createCustomer());
       when(customerRepository.findById(1L)).thenReturn(optional);
       String deletedUserName = customerService.deleteCustomer(optional.get().getCustomerId());
        Assertions.assertEquals(optional.get().getName(),deletedUserName);
    }

    private CustomerEntity createCustomer() {
        CustomerEntity cust = new CustomerEntity();
        cust.setCustomerId(1L);
        cust.setEmail("jc@mail.com");
        cust.setCustomerType(CustomerType.ACTIVE);
        cust.setName("jc");
        AddressEntity address = new AddressEntity();
        address.setIdAddress(1L);
        cust.setAddressEntity(address);
        return cust;
    }


     private CustomerEntity createCustomerEmailNull() {
        CustomerEntity cust = new CustomerEntity();
        cust.setCustomerId(1L);
        // cust.setEmail("jc@mail.com");
        cust.setCustomerType(CustomerType.ACTIVE);
        cust.setName("jc");
        AddressEntity address = new AddressEntity();
        address.setIdAddress(1L);
        cust.setAddressEntity(address);
        return cust;
    }

}
