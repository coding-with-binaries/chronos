package com.manager.timezone.timezonemanagerserver.dto;

import java.util.UUID;

public class RegisterUserResponseDto {

    private UUID uid;

    private String username;

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
