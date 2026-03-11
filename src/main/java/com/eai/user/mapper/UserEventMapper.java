package com.eai.user.mapper;

import org.mapstruct.Mapper;

import com.eai.user.dto.UserEventDTO;
import com.eai.user.entities.UserEventEntity;

@Mapper(componentModel = "spring")
public interface UserEventMapper {
 UserEventDTO toDto(UserEventEntity entity);
}
