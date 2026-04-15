package com.eai.user.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.eai.user.dto.PageDTO;
import com.eai.user.dto.UserEventDTO;
import com.eai.user.dto.UserEventInput;

public interface UserEventService {

  public UserEventDTO createUserEvent(UserEventDTO userEventDTO);

  public UserEventDTO addUserEventFromInputData(UserEventInput eventInput)throws Exception;

  public UserEventDTO getUserEventById(Long id);

  public List<UserEventDTO> getUserEventsByUserId(Long userId);

  public PageDTO<UserEventDTO> getUserEventsByUserId(Long userId,int page, int size);
}
