package com.manager.timezone.timezonemanagerserver.dto;

import java.util.Set;

public class UserDto extends BaseDto {
    private String username;

    private boolean isEnabled;

    private Set<RoleDto> roles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public Set<RoleDto> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDto> roles) {
        this.roles = roles;
    }
}
