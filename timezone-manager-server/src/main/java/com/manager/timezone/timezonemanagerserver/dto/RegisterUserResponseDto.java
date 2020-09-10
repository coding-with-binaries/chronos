package com.manager.timezone.timezonemanagerserver.dto;

import java.util.UUID;

public class RegisterUserResponseDto extends BaseDto {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
