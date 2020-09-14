package com.manager.timezone.timezonemanagerserver.dto;

import java.util.Set;

public class UpdateUserRequestDto {
    private String password;

    private Set<RoleType> roles;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<RoleType> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleType> roles) {
        this.roles = roles;
    }
}
