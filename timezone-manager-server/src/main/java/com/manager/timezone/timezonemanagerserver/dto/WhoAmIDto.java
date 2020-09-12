package com.manager.timezone.timezonemanagerserver.dto;

import java.util.Set;

public class WhoAmIDto {
    private String username;

    private Set<RoleType> roles;

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
