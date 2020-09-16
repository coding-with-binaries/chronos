package com.manager.timezone.timezonemanagerserver.dto;

import java.util.Set;
import java.util.UUID;

public class WhoAmIDto {
    private UUID uid;

    private String username;

    private Set<RoleType> roles;

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
