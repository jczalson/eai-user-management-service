package com.eai.user.entities;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Table(name = "ADDRESS")
@Entity(name = "AddressEntity")
@Setter
@Getter
@NoArgsConstructor
@ToString
public class AddressEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long idAddress;
	
	@Column(name = "STREET_NM")
	private String streetName;
	
	@Column(name = "CITY")
	private String city;
	
	@Column(name = "COUNTRY")
	private String country;
	
	@Column(name = "ZIP_CD")
	private Long zipCode;

	@OneToMany(mappedBy = "address",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	private List<CustomerEntity> listCustomerEntity;
	

}
