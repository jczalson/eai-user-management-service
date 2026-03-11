package com.eai.user.mapper;

import org.mapstruct.Mapper;

import com.eai.user.dto.CustomerDTO;
import com.eai.user.entities.CustomerEntity;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
 CustomerDTO toDto(CustomerEntity entity);
}
