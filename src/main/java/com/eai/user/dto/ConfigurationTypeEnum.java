package com.eai.user.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ConfigurationTypeEnum {
    ACTIVITY_MONITOR("ActivityMonitor"),
    LOCATION_CONFIRMATION("locationConfirmation"),
    PDF_REPORT("PDFreport");

    private final String configType;

    ConfigurationTypeEnum(String config) {
        this.configType = config;
    }

   public String getConfigType(){
    return configType;
   }

    @JsonValue
    public String toValue() {
        return configType;
    }
}
