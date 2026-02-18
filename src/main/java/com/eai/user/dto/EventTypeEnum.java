package com.eai.user.dto;

public enum EventTypeEnum {
LOGIN_ATTEMPT("You tried to login"),
LOGIN_ATTEMPT_SUCCESS("You login successfully"),
LOGIN_ATTEMPT_FAILED("Login attempted failed"),
PROFILE_UPDATE("Profile updated"),
PROFILE_IMAGE_UPDATED("Profile image updated"),
CODE_VERIFY_SUCCESS("Code verify success");

private final String description;
private EventTypeEnum(String description){
  this.description = description;
}
public String getDescription(){
  return this.description;
}
}
