package com.eai.user.utilities;

import java.sql.Timestamp;

import org.apache.commons.lang3.StringUtils;

import com.eai.user.dto.AddressDTO;
import com.eai.user.dto.CustomerDTO;
import com.eai.user.entities.AddressEntity;
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
			
		if(customerEntity.getAddress() !=null) {
			AddressDTO fromAddressEntityToDto = AddressUtilities.fromAddressEntityToDto(customerEntity.getAddress());
			customerDTO.setAddress(fromAddressEntityToDto);
		}
		
		return customerDTO;
		
	}


	public static CustomerEntity fromDtoToCustomerEntity(CustomerDTO dto) {
		
		CustomerEntity entity = new CustomerEntity();

		if(dto.getCustomerId() != null)
		entity.setCustomerId(dto.getCustomerId());
		
		if(StringUtils.isNotBlank(dto.getName()))
			entity.setName(dto.getName());
		
		if(StringUtils.isNotBlank(dto.getEmail()))
			entity.setEmail(dto.getEmail());
		
		if(StringUtils.isNotBlank(dto.getCustomerType().name()))
			entity.setCustomerType(dto.getCustomerType());
		
		if(dto.getCreatedDate() !=null) {
			entity.setCreatedDate(Timestamp.valueOf(dto.getCreatedDate()));
		}
			
		if(dto.getAddress() !=null) {
			AddressEntity addressEntity = AddressUtilities.fromAddressDtoToEntity(dto.getAddress());
			entity.setAddress(addressEntity);
		}
		
		return entity;
		
	}


}
