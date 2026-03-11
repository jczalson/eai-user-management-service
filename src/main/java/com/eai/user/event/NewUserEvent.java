package com.eai.user.event;

import org.springframework.context.ApplicationEvent;
import com.eai.user.dto.EventTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewUserEvent extends ApplicationEvent{

  private EventTypeEnum type;
  private String email;

  public NewUserEvent(EventTypeEnum type, String email) {
    super(email);
    this.type = type;
    this.email = email;
  }
}
