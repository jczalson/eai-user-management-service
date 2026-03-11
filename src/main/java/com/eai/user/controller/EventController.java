package com.eai.user.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eai.user.dto.EventDTO;
import com.eai.user.dto.UserDTO;
import com.eai.user.dto.UserEventDTO;
import com.eai.user.dto.UserEventInput;
import com.eai.user.entities.HttpResponse;
import com.eai.user.service.AccountService;
import com.eai.user.service.EventService;
import com.eai.user.service.UserEventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/event")
@Slf4j
public class EventController {

  @Autowired
  private EventService eventService;

  @Autowired
  private UserEventService userEventService;

  @Autowired
  private AccountService accountService;

  @PostMapping("/event/add")
  public ResponseEntity<HttpResponse> saveEvent(@RequestBody EventDTO eventInput) throws Exception {
    log.info("Event input DTO {}", eventInput);
    EventDTO event = eventService.createEvent(eventInput);
    return ResponseEntity.ok().body(HttpResponse.builder()
        .data(Map.of("event", event))
        .message("Event created successfully")
        .developerMessage("Event created succefully")
        .status(HttpStatus.OK)
        .statusCode(HttpStatus.OK.value())
        .build());
  }

  @PostMapping("/user/events/add")
  public ResponseEntity<HttpResponse> saveUserEvent(@RequestBody UserEventInput eventInput) throws Exception {
    log.info("User Event Input {}", eventInput);
    // UserEventDTO event = userEventService.createUserEvent(userEventDTO);
    UserEventDTO event = userEventService.addUserEventFromInputData(eventInput);
    return ResponseEntity.ok().body(HttpResponse.builder()
        .data(Map.of("user-event", event))
        .message("User Event created successfully")
        .developerMessage("User Event created succefully")
        .status(HttpStatus.OK)
        .statusCode(HttpStatus.OK.value())
        .build());
  }

   @GetMapping("/user/events/list")
  public ResponseEntity<HttpResponse> getUserEvent(@AuthenticationPrincipal UserDTO user,@RequestParam Optional<Integer> page,@RequestParam Optional<Integer> size) throws Exception {
    Page<UserEventDTO> userEvents =  userEventService.getUserEventsByUserId(user.getIdUser(), page.orElse(0), size.orElse(10));
    return ResponseEntity.ok().body(HttpResponse.builder()
        .data(Map.of("userEvents", userEvents,"user",user))
        .message("User Events retrieved successfully")
        .developerMessage("User Events retrieved successfully")
        .status(HttpStatus.OK)
        .statusCode(HttpStatus.OK.value())
        .build());
  }

  @GetMapping("/user/event/{id}")
  public ResponseEntity<HttpResponse> getUserEvent(@PathVariable("id") Long id) throws Exception {
    UserEventDTO userEventDTO = userEventService.getUserEventById(id);
    log.info("User Event {}", convertToJSON(userEventDTO));
    if (userEventDTO != null) {
      return ResponseEntity.ok().body(
          HttpResponse.builder()
              .data(Map.of("user-event", userEventDTO))
              .developerMessage("User Event retrieved")
              .message("User Event retrieved")
              .status(HttpStatus.OK)
              .statusCode(HttpStatus.OK.value())
              .build());
    } else {
      return ResponseEntity.badRequest().body(
          HttpResponse.builder()
              .developerMessage("No User Event retrieved")
              .message("No User Event retrieved")
              .status(HttpStatus.BAD_REQUEST)
              .statusCode(HttpStatus.BAD_REQUEST.value())
              .build());
    }
  }

  private String convertToJSON(Object obj) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(obj);
  }

  @GetMapping("/user/events/{email}")
  public ResponseEntity<HttpResponse> getUserEvents(@PathVariable("email") String email) throws Exception {
    UserDTO user = accountService.loadUserByUsername(email);
    if (user != null) {
      List<UserEventDTO> events = userEventService.getUserEventsByUserId(user.getIdUser());
      log.info("User events {}", convertToJSON(events));
      return ResponseEntity.ok().body(
          HttpResponse.builder()
              .data(Map.of("user-events", events))
              .developerMessage("User Events retrieved")
              .message("User Events retrieved")
              .status(HttpStatus.OK)
              .statusCode(HttpStatus.OK.value())
              .build());
    } else {
      return ResponseEntity.badRequest().body(
          HttpResponse.builder()
              .developerMessage("No user events retrieved,  try again")
              .message("No User Events retrieved for that User")
              .status(HttpStatus.BAD_REQUEST)
              .statusCode(HttpStatus.BAD_REQUEST.value())
              .build());
    }
  }

}
