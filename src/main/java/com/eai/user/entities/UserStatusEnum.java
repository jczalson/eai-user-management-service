package com.eai.user.entities;

public enum UserStatusEnum {
    CREATED(0),ACTIVE(1), BLOCKED(2),;

    public int statusCode;

    UserStatusEnum(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
