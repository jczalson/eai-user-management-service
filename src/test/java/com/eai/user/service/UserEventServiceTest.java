package com.eai.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.eai.user.dto.EventTypeEnum;
import com.eai.user.dto.UserEventDTO;
import com.eai.user.entities.AppUser;
import com.eai.user.entities.EventEntity;
import com.eai.user.entities.UserEventEntity;
import com.eai.user.mapper.UserEventMapper;
import com.eai.user.repository.UserEventRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class UserEventServiceTest {

  @Mock
  private UserEventRepository repo;

  @Mock
  private AccountServiceImplm accountService;

  @Mock
  private EventServiceImpl eventService;

  @Mock
  private UserEventMapper userEventMapper;

  @InjectMocks
  private UserEventServiceImpl service;

  @Test()
  public void testUnrecognizedPropertyException() throws JsonMappingException, JsonProcessingException {
  record Person(String name,String gender,int age){}
  String json = "{\"name\":\"Paul\","+
                 "\"gender\":\"M\","+
                 "\"age\":\"19\",\"name1\":\"unkown\"}";
     ObjectMapper mapper = new ObjectMapper();
    Person person = mapper.readValue(json, Person.class);     
     assertThrows(UnrecognizedPropertyException.class, ()->person.gender());  
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  @Getter
  @Setter
  private static class MyData {
    String name;
    String gender;
    int age;
  }

   @Test()
  public void testUnrecognizedPropertyExceptionIsTrue() throws JsonMappingException, JsonProcessingException {
  String json = "{\"name\":\"Paul\","+
                 "\"gender\":\"M\","+
                 "\"age\":\"19\",\"name1\":\"unknown\"}";
     ObjectMapper mapper = new ObjectMapper();
     mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,false);
    MyData data = mapper.readValue(json, MyData.class);     
    assertNotNull(data);
    assertEquals(data.gender, "M");
     assertEquals(data.name, "Paul");
  }

  @Test
  public void testGetUserEventsByUserId() {
    Optional<List<UserEventEntity>> userEvents = createUserEvents();
    when(repo.findUserEventsByUserId(1L)).thenReturn(userEvents);
    List<UserEventDTO> eventsList = service.getUserEventsByUserId(1L);
    assertEquals(userEvents.get().size(), eventsList.size());

    record Person(String name, int age) {
    }
    List<Person> list = List.of(new Person("Paul", 19),
        new Person("Rene", 30), new Person("Alyson", 9));
    List<String> adultNames = list.stream()
        .filter(p -> p.age() >= 18)
        .map(Person::name)
        .toList(); // no need for Collectors
    System.out.println("Adults: " + adultNames);
  }

  private Optional<List<UserEventEntity>> createUserEvents() {

    List<UserEventEntity> userEvents = new ArrayList<>();

    UserEventEntity one = new UserEventEntity();
    one.setIdUserEvent(1L);
    one.setDevice("Chrome");
    one.setIpAddress("120.0.0.1");
    one.setCreateAt(new Timestamp(System.currentTimeMillis()));
    EventEntity event = new EventEntity();
    event.setIdEvent(100L);
    event.setType(EventTypeEnum.LOGIN_ATTEMPT);
    one.setEvent(event);
    AppUser user = new AppUser();
    user.setIdUser(1L);
    user.setEmail("jc@gmail.com");
    one.setUser(user);
    userEvents.add(one);

    UserEventEntity two = new UserEventEntity();
    two.setIdUserEvent(2L);
    two.setDevice("Desktop");
    two.setIpAddress("127.0.0.2");
    two.setCreateAt(new Timestamp(System.currentTimeMillis()));
    EventEntity even = new EventEntity();
    even.setIdEvent(200L);
    even.setType(EventTypeEnum.LOGIN_ATTEMPT);
    two.setEvent(event);
    AppUser us = new AppUser();
    us.setIdUser(1L);
    us.setEmail("jc@gmail.com");
    two.setUser(us);
    userEvents.add(two);

    UserEventEntity three = new UserEventEntity();
    three.setIdUserEvent(3L);
    three.setDevice("Desktop");
    three.setIpAddress("127.0.0.5");
    three.setCreateAt(new Timestamp(System.currentTimeMillis()));
    EventEntity ev = new EventEntity();
    ev.setIdEvent(300L);
    ev.setType(EventTypeEnum.LOGIN_ATTEMPT_FAILED);
    three.setEvent(ev);
    AppUser u = new AppUser();
    u.setIdUser(1L);
    u.setEmail("jc@gmail.com");
    three.setUser(u);
    userEvents.add(three);
    return Optional.of(userEvents); 
  }
}
