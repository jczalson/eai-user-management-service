package com.eai.user.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name = "CUSTOMER")
@Entity(name = "CustomerEntity")
@Setter
@Getter
@NoArgsConstructor
@ToString
@Data
public class CustomerEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_customer")
     private Long customerId;
	
	@Column(name = "NM")
	private String name;
	
	@NotEmpty(message = "Email cannot be empty")
    @Email(message ="Invalid email. please insert the correct email")
	@Column(name = "EMAIL", unique = true)
	private String email;
	
	@Column(name = "CUSTOMER_TYPE") 
	private CustomerType customerType;
	
	@Column(name = "CREATE_DT")
	private Timestamp createdDate;
	
	@ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_ADDRESS")
	private AddressEntity address;

	@Column(name="config_json")
	@Lob
	private String configJson;
}