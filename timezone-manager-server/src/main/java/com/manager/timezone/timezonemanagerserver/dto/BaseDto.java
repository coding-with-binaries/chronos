package com.manager.timezone.timezonemanagerserver.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

public class BaseDto {
    @ApiModelProperty(readOnly = true)
    private UUID uid = UUID.randomUUID();

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }
}
