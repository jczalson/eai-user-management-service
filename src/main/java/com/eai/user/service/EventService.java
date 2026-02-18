package com.eai.user.service;

import com.eai.user.dto.EventDTO;
import com.eai.user.dto.EventTypeEnum;

public interface EventService {

  public EventDTO createEvent(EventDTO eventDto);
  public EventDTO getEventById(Long eventId);

  public EventDTO getEventByType(EventTypeEnum type);
}
