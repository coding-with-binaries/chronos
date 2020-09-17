package com.chronos.chronosserver.dto;

import java.util.UUID;

public class AuthenticateUserResponseDto {
    private String accessToken;

    private UUID uid;

    private String username;

    private RoleType role;

    public AuthenticateUserResponseDto() {

    }

    public AuthenticateUserResponseDto(String accessToken, UUID uid, String username, RoleType role) {
        this.uid = uid;
        this.accessToken = accessToken;
        this.username = username;
        this.role = role;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

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
