package com.eai.user.dto;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class UserEventInput implements Serializable{

  private static final long serializableId = 1L;

  private String ipAddress;

  private String device;  

  private Instant createAt;

  private String  email;

  private EventTypeEnum type;
}
