package com.eai.user.dto;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class EventDTO implements Serializable {
  
  private static final long serializableId = 1L;

  private Long id;

  private EventTypeEnum type;

  private String description;

}
