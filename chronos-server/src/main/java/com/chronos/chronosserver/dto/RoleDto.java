package com.chronos.chronosserver.dto;

public class RoleDto extends BaseDto {
    private RoleType type;

    public RoleType getType() {
        return type;
    }

    public void setType(RoleType type) {
        this.type = type;
    }
}