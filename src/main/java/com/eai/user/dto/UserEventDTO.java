package com.eai.user.dto;

import java.io.Serializable;
import java.time.Instant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
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

  /**
   * Begin DTO projections fields for address DTO
   */
   private EventTypeEnum type;
   private String eventDesc;
  /**End */
  public UserEventDTO(Long id, String ipAddress, String device, Instant crtAt,EventTypeEnum type, String eventDresc) {
    this.id = id;
    this.ipAddress = ipAddress;
    this.device = device;
    this.crtAt = crtAt;
    this.type = type;
    this.eventDesc = eventDresc;
  }

}
