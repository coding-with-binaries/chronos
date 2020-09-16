package com.manager.timezone.timezonemanagerserver.dto;

import java.util.Set;

public class UpdateUserRequestDto {
    private Set<RoleType> roles;

    public Set<RoleType> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleType> roles) {
        this.roles = roles;
    }
}
