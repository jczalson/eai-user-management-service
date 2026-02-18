package com.eai.user.listener;

import java.net.http.HttpRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.eai.user.dto.UserEventInput;
import com.eai.user.event.NewUserEvent;
import com.eai.user.service.EventService;
import com.eai.user.service.UserEventService;
import com.eai.user.utilities.RequestUtilies;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewUserEventListener {

  @Autowired
  private EventService eventService;

  @Autowired
  private HttpServletRequest request;

  @Autowired
  private UserEventService userEventService;

  @EventListener
  public void onNewUserEventListener(NewUserEvent event) throws Exception {
   log.info("New User event is fired");
    UserEventInput inputEvent = new UserEventInput();
    inputEvent.setEmail(event.getEmail());
    inputEvent.setType(event.getType());
    inputEvent.setDevice(RequestUtilies.getDevice(request));
    inputEvent.setIpAddress(RequestUtilies.getIpAddress(request));
    userEventService.addUserEventFromInputData(inputEvent);
  }
}
