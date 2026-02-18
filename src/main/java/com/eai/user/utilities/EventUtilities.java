package com.eai.user.utilities;

import org.springframework.beans.BeanUtils;

import com.eai.user.dto.EventDTO;
import com.eai.user.entities.EventEntity;

public class EventUtilities {

  public static EventDTO fromEntityToDto(EventEntity entity) {
    EventDTO event = new EventDTO();
    BeanUtils.copyProperties(entity, event);
    event.setDescription(entity.getType().getDescription());
    return event;
  }

  public static EventEntity fromDtoToEntity(EventDTO dto) {
    EventEntity entity = new EventEntity();
    BeanUtils.copyProperties(dto, entity);
     entity.setDescription(dto.getType().getDescription());
    return entity;
  }
}
