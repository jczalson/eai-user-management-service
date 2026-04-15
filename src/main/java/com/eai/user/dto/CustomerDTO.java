package com.eai.user.dto;

import java.io.Serializable;
import java.time.Instant;

import com.eai.user.entities.CustomerType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
// @AllArgsConstructor
@EqualsAndHashCode
@ToString
public class CustomerDTO implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	private Long idCustomer;
	
	@NotEmpty(message = "Name cannot be empty")
	private String nm;
	
	@NotEmpty(message = "Email cannot be empty")
    @Email(message ="Invalid email. please insert the correct email")
	private String email;
	
	// @JsonSerialize(using = LocalDateTimeSerializer.class)
	// @JsonDeserialize(using = LocalDateTimeDeserializer.class)
	// @JsonFormat(pattern = Application.DATE_TIME_FORMAT)
	private Instant createdDt;
	
	private CustomerType customerType;
	
  // @JsonBackReference  with this address is ignored
	private AddressDTO address;

  /**
   * Begin DTO projections fields for address DTO
   */
  private Long idAddress;
  private String street;
  private String city;
  /**End DTO projections */

  public CustomerDTO(Long idCustomer, String nm, String email,
      Instant createdDt, CustomerType customerType, Long idAddress,String street, String city) {
    this.idCustomer = idCustomer;
    this.nm = nm;
    this.email = email;
    this.createdDt = createdDt;
    this.customerType = customerType;
    this.idAddress =  idAddress;
    this.street = street;
    this.city = city;
  }

  
}
