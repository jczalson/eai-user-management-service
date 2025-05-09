package com.eai.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eai.user.dto.AddressDTO;
import com.eai.user.entities.AddressEntity;
import com.eai.user.repository.AddressRepository;
import com.eai.user.utilities.AddressUtilities;

@Service
public class AddressServiceImpl implements AddressService {
	
	@Autowired
	private AddressRepository addressRepository;
	

	@Override
	public List<AddressDTO> getAllAddresses() {
		List<AddressDTO>  list = new ArrayList<AddressDTO>();
		List<AddressEntity> all = addressRepository.findAll();
		all.stream().forEach(address->{ list.add(AddressUtilities.fromAddressEntityToDto(address));});
		return list;
	}


	@Override
	public AddressDTO getAddressById(Long addressId) {
		Optional<AddressEntity> addressOne = addressRepository.findById(addressId);
		return AddressUtilities.fromAddressEntityToDto(addressOne.get());
	}

}
