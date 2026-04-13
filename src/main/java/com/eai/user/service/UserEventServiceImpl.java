package com.eai.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eai.user.dto.EventDTO;
import com.eai.user.dto.UserDTO;
import com.eai.user.dto.UserEventDTO;
import com.eai.user.dto.UserEventInput;
import com.eai.user.entities.UserEventEntity;
import com.eai.user.mapper.UserEventMapper;
import com.eai.user.repository.UserEventRepository;
import com.eai.user.utilities.UserEventUtilities;

@Service
@Transactional
public class UserEventServiceImpl implements UserEventService {

  @Autowired
  private UserEventRepository repo;

  @Autowired
  private AccountService accountService;

  @Autowired
  private EventService eventService;

  @Autowired
  private UserEventMapper userEventMapper;

  

  public UserEventDTO createUserEvent(UserEventDTO userEventDTO) {
    return UserEventUtilities.fromEntityToDto(repo.save(UserEventUtilities.fromDtoToEntity(userEventDTO)));
  }

  @Override
  public UserEventDTO addUserEventFromInputData(UserEventInput eventInput) throws Exception {
    UserEventDTO userEventDTO = new UserEventDTO();
    UserDTO user = accountService.loadUserByUsername(eventInput.getEmail());
    EventDTO event = eventService.getEventByType(eventInput.getType());
    userEventDTO.setDevice(eventInput.getDevice());
    userEventDTO.setIpAddress(eventInput.getIpAddress());
    userEventDTO.setUser(user);
    userEventDTO.setEvent(event);
    userEventDTO.setCrtAt(eventInput.getCreateAt());
    return this.createUserEvent(userEventDTO);
  }

  @Override
  public UserEventDTO getUserEventById(Long id) {
    if (repo.findUserEventEntityById(id).isPresent()) {
      return UserEventUtilities.fromEntityToDto(repo.findUserEventEntityById(id).get());
    }
    return null;
  }

  @Override
  public Page<UserEventDTO> getUserEventsByUserId(Long id,int page, int size) {
    Page<UserEventEntity> userEvents = repo.findUserEventEntityByUserId(id,PageRequest.of(page, size));
    // Page<UserEventDTO> userEvents = repo.findUserEventEntityByUserId(id,PageRequest.of(page, size));
    return userEvents.map(userEventMapper::toDto);
  }

  @Override
  public List<UserEventDTO> getUserEventsByUserId(Long userId) {
    List<UserEventDTO> events = new ArrayList<>();
    if (repo.findUserEventsByUserId(userId).isPresent()) {
      repo.findUserEventsByUserId(userId).get()
          .stream().forEach(event -> {
            events.add(UserEventUtilities.fromEntityToDto(event));
          });
    }
    return events;
  }
}
