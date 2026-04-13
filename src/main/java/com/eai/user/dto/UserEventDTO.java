package com.eai.user.dto;

import java.io.Serializable;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserEventDTO implements Serializable {

  private static final long serializableId = 1L;

  private Long id;

  private String ipAddress;

  private String device;

  // @JsonSerialize(using = LocalDateTimeSerializer.class)
  // @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  // @JsonFormat(pattern = Application.DATE_TIME_FORMAT)
  private Instant crtAt;

  private UserDTO user;

  private EventDTO event;

}
