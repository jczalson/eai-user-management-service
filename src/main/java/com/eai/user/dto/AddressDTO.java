package com.eai.user.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@ToString
public class AddressDTO implements Serializable {

	
	private static final long serialVersionUID = 1L;
		
	private Long addressId;
	
	private String streetName;
		
	private String city;
		
	private String country;
		
	private Long zipCode;
}
