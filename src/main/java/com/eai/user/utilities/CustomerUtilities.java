package com.eai.user.utilities;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.apache.commons.lang3.StringUtils;

import com.eai.user.dto.AddressDTO;
import com.eai.user.dto.CustomerDTO;
import com.eai.user.entities.CustomerEntity;

public class CustomerUtilities {
	
	public static CustomerDTO fromCustomerEntityToDto(CustomerEntity customerEntity) {
		
		CustomerDTO customerDTO = new CustomerDTO();
		
		if(customerEntity.getCustomerId()!=null)
			customerDTO.setCustomerId(customerEntity.getCustomerId());
		
		if(StringUtils.isNotBlank(customerEntity.getName()))
			customerDTO.setName(customerEntity.getName());
		
		if(StringUtils.isNotBlank(customerEntity.getEmail()))
			customerDTO.setEmail(customerEntity.getEmail());
		
		if(StringUtils.isNotBlank(customerEntity.getCustomerType().name()))
			customerDTO.setCustomerType(customerEntity.getCustomerType());
		
		if(customerEntity.getCreatedDate() !=null) {
			customerDTO.setCreatedDate(customerEntity.getCreatedDate().toLocalDateTime());
		}
			
		if(customerEntity.getAddressEntity() !=null) {
			AddressDTO fromAddressEntityToDto = AddressUtilities.fromAddressEntityToDto(customerEntity.getAddressEntity());
			customerDTO.setAddressDTO(fromAddressEntityToDto);
		}
		
		return customerDTO;
		
	}

}
