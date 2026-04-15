package com.eai.user.utilities;

import org.apache.commons.lang3.StringUtils;

import com.eai.user.dto.AddressDTO;
import com.eai.user.dto.CustomerDTO;
import com.eai.user.entities.AddressEntity;
import com.eai.user.entities.CustomerEntity;

public class CustomerUtilities {
	
	public static CustomerDTO fromCustomerEntityToDto(CustomerEntity customerEntity) {
		
		CustomerDTO customerDTO = new CustomerDTO();
		
		if(customerEntity.getIdCustomer()!=null)
			customerDTO.setIdCustomer(customerEntity.getIdCustomer());
		
		if(StringUtils.isNotBlank(customerEntity.getNm()))
			customerDTO.setNm(customerEntity.getNm());
		
		if(StringUtils.isNotBlank(customerEntity.getEmail()))
			customerDTO.setEmail(customerEntity.getEmail());
		
		if(StringUtils.isNotBlank(customerEntity.getCustomerType().name()))
			customerDTO.setCustomerType(customerEntity.getCustomerType());
		
		if(customerEntity.getCreatedDt() !=null) {
			customerDTO.setCreatedDt(customerEntity.getCreatedDt());
		}
			
		if(customerEntity.getAddress() !=null) {
			AddressDTO fromAddressEntityToDto = AddressUtilities.fromAddressEntityToDto(customerEntity.getAddress());
			customerDTO.setAddress(fromAddressEntityToDto);
		}
		
		return customerDTO;
		
	}


	public static CustomerEntity fromDtoToCustomerEntity(CustomerDTO dto) {
		
		CustomerEntity entity = new CustomerEntity();

		if(dto.getIdCustomer() != null)
		entity.setIdCustomer(dto.getIdCustomer());
		
		if(StringUtils.isNotBlank(dto.getNm()))
			entity.setNm(dto.getNm());
		
		if(StringUtils.isNotBlank(dto.getEmail()))
			entity.setEmail(dto.getEmail());
		
		if(StringUtils.isNotBlank(dto.getCustomerType().name()))
			entity.setCustomerType(dto.getCustomerType());
		
		if(dto.getCreatedDt() !=null) {
			entity.setCreatedDt(dto.getCreatedDt());
		}
			
		if(dto.getAddress() !=null) {
			AddressEntity addressEntity = AddressUtilities.fromAddressDtoToEntity(dto.getAddress());
			entity.setAddress(addressEntity);
		}
		
		return entity;
		
	}


}
