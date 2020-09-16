package com.manager.timezone.timezonemanagerserver.dto;

import java.util.Set;
import java.util.UUID;

public class AuthenticateUserResponseDto {
    private String accessToken;

    private UUID uid;

    private String username;

    private Set<RoleType> roles;

    public AuthenticateUserResponseDto() {

    }

    public AuthenticateUserResponseDto(String accessToken, UUID uid, String username, Set<RoleType> roles) {
        this.uid = uid;
        this.accessToken = accessToken;
        this.username = username;
        this.roles = roles;
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

    public Set<RoleType> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleType> roles) {
        this.roles = roles;
    }
}
