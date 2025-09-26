package com.eai.user.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConfigurationAttributesDTO implements Serializable {

 private static final long serialVersionUID = 1L;

 private String configurationAttribute;

 private Boolean configurationEnabled;

 private String configurationValue;

 
}
