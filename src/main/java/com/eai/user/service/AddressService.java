package com.eai.user.service;

import java.util.List;

import com.eai.user.dto.AddressDTO;

public interface AddressService {

	public List<AddressDTO> getAllAddresses();
	
	public AddressDTO getAddressById(Long addressId);
}
