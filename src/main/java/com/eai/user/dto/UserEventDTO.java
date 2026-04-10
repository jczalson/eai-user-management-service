package com.eai.user.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.eai.user.Application;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserEventDTO implements Serializable{

  private static final long serializableId = 1L;

  private Long id;

  private String ipAddress;

  private String device;  

  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonFormat(pattern = Application.DATE_TIME_FORMAT)
  private LocalDateTime crtAt;

  private UserDTO user;

  private EventDTO event;
}
