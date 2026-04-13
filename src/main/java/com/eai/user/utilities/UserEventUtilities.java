package com.eai.user.utilities;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.beans.BeanUtils;

import com.eai.user.dto.EventDTO;
import com.eai.user.dto.UserDTO;
import com.eai.user.dto.UserEventDTO;
import com.eai.user.entities.AppUser;
import com.eai.user.entities.EventEntity;
import com.eai.user.entities.UserEventEntity;

public class UserEventUtilities {

  public static UserEventDTO fromEntityToDto(UserEventEntity entity) {
    UserEventDTO userEventDTO = new UserEventDTO();
    EventDTO eventDTO = new EventDTO();
    UserDTO user = new UserDTO();
    BeanUtils.copyProperties(entity, userEventDTO);
     BeanUtils.copyProperties(entity.getUser(), user);
    BeanUtils.copyProperties(entity.getEvent(), eventDTO);
    userEventDTO.setCrtAt(entity.getCrtAt());
    userEventDTO.setEvent(eventDTO);
    userEventDTO.setUser(user);
    return userEventDTO;
  }

  public static UserEventEntity fromDtoToEntity(UserEventDTO dto) {
    UserEventEntity entity = new UserEventEntity();
    AppUser userEntity = new AppUser();
    EventEntity eventEntity = new EventEntity();
    BeanUtils.copyProperties(dto, entity);
    // LocalDateTime ldt = LocalDateTime.now();
    // ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
    // entity.setCrtAt();
    BeanUtils.copyProperties(dto.getUser(), userEntity);
    BeanUtils.copyProperties(dto.getEvent(), eventEntity);
    entity.setEvent(eventEntity);
    entity.setUser(userEntity);
    return entity;
  }
}
