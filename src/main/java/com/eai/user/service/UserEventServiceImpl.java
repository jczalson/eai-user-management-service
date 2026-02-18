package com.eai.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eai.user.dto.EventDTO;
import com.eai.user.dto.UserDTO;
import com.eai.user.dto.UserEventDTO;
import com.eai.user.dto.UserEventInput;
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
    return this.createUserEvent(userEventDTO);
  }

  @Override
  public UserEventDTO getUserEventById(Long id) {
    if (repo.findUserEventEntityByidUserEvent(id).isPresent()) {
      return UserEventUtilities.fromEntityToDto(repo.findUserEventEntityByidUserEvent(id).get());
    }
    return null;
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
