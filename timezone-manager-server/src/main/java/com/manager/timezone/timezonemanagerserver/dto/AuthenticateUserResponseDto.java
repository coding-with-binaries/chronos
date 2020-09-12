package com.manager.timezone.timezonemanagerserver.dto;

import java.util.Set;

public class AuthenticateUserResponseDto {
    private String accessToken;

    private String username;

    private Set<RoleType> roles;

    public AuthenticateUserResponseDto(String accessToken, String username, Set<RoleType> roles) {
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
