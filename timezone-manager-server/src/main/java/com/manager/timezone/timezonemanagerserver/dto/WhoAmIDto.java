package com.manager.timezone.timezonemanagerserver.dto;

import java.util.UUID;

public class WhoAmIDto {
    private UUID uid;

    private String username;

    private RoleType role;

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

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }
}
