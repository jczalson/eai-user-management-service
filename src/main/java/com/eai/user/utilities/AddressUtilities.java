package com.eai.user.utilities;

import org.apache.commons.lang3.StringUtils;

import com.eai.user.dto.AddressDTO;
import com.eai.user.entities.AddressEntity;

public class AddressUtilities {
	
	public static AddressDTO fromAddressEntityToDto(AddressEntity addressEntity) {
		
		AddressDTO addressDTO = new AddressDTO();
		
		if(addressEntity.getIdAddress() !=null)
			addressDTO.setAddressId(addressEntity.getIdAddress());
		
		if(addressEntity.getZipCode() !=null)
			addressDTO.setZipCode(addressEntity.getZipCode());
		
		if(StringUtils.isNotBlank(addressEntity.getCity()))
			addressDTO.setCity(addressEntity.getCity());
		
		if(StringUtils.isNotBlank(addressEntity.getCountry()))
			addressDTO.setCountry(addressEntity.getCountry());
		
		if(StringUtils.isNotBlank(addressEntity.getStreetName()))
			addressDTO.setStreetName(addressEntity.getStreetName());
		
		return addressDTO;
		
	}

}
