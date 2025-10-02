package com.eai.user.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConfigAttributeDTO implements Serializable {

 private static final long serialVersionUID = 1L;

 private ConfigurationTypeEnum configurationType;

 private List<ConfigurationAttributesDTO> configurationAttributes;

 private String createdBy;

 private Date updatedDate;
 
}
