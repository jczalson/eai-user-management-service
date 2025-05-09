package com.eai.user.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.eai.user.Application;
import com.eai.user.entities.CustomerType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@ToString
public class CustomerDTO implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	private Long customerId;
	
	private String name;
	
	private String email;
	
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonFormat(pattern = Application.DATE_TIME_FORMAT)
	private LocalDateTime createdDate;
	
	private CustomerType customerType;
	
	private AddressDTO addressDTO;
}
