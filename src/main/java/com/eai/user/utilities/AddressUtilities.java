package com.eai.user.utilities;

import org.apache.commons.lang3.StringUtils;

import com.eai.user.dto.AddressDTO;
import com.eai.user.entities.AddressEntity;

public class AddressUtilities {
	
	public static AddressDTO fromAddressEntityToDto(AddressEntity entity) {
		
		AddressDTO addressDTO = new AddressDTO();
		
		if(entity.getIdAddress() !=null)
			addressDTO.setIdAddress(entity.getIdAddress());
		
		if(entity.getZipCode() !=null)
			addressDTO.setZipCode(entity.getZipCode());
		
		if(StringUtils.isNotBlank(entity.getCity()))
			addressDTO.setCity(entity.getCity());
		
		if(StringUtils.isNotBlank(entity.getCountry()))
			addressDTO.setCountry(entity.getCountry());
		
		if(StringUtils.isNotBlank(entity.getStreetName()))
			addressDTO.setStreetName(entity.getStreetName());
		
		return addressDTO;
		
	}


	public static AddressEntity fromAddressDtoToEntity(AddressDTO dto) {
		
		AddressEntity entity = new AddressEntity();
		
		if(dto.getIdAddress() !=null)
			entity.setIdAddress(dto.getIdAddress());
		
		if(dto.getZipCode() !=null)
			entity.setZipCode(dto.getZipCode());
		
		if(StringUtils.isNotBlank(dto.getCity()))
			entity.setCity(dto.getCity());
		
		if(StringUtils.isNotBlank(dto.getCountry()))
			entity.setCountry(dto.getCountry());
		
		if(StringUtils.isNotBlank(dto.getStreetName()))
			entity.setStreetName(dto.getStreetName());
		
		return entity;
		
	}

}
