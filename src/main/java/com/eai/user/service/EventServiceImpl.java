package com.eai.user.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eai.user.dto.EventDTO;
import com.eai.user.dto.EventTypeEnum;
import com.eai.user.entities.EventEntity;
import com.eai.user.repository.EventRepository;
import com.eai.user.utilities.EventUtilities;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class EventServiceImpl implements EventService {

  @Autowired
  private EventRepository repo;

  public EventDTO createEvent(EventDTO eventDto) {
    return EventUtilities.fromEntityToDto(repo.save(EventUtilities.fromDtoToEntity(eventDto)));
  }

  public EventDTO getEventById(Long eventId) {
    return EventUtilities.fromEntityToDto(repo.findById(eventId).get());
  }

  @Override
  public EventDTO getEventByType(EventTypeEnum type) {
    Optional<EventEntity> eventEntityByType = repo.findEventEntityByType(type);
    if (eventEntityByType.isPresent()) {
      return EventUtilities.fromEntityToDto(eventEntityByType.get());
    }
    return null;
  }
}
