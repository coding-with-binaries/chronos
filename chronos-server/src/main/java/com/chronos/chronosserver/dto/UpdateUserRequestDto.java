package com.chronos.chronosserver.dto;

public class UpdateUserRequestDto {
    private RoleType role;

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }
}
