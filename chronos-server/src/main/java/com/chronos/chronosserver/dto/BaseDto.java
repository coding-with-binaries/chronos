package com.chronos.chronosserver.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

public class BaseDto {
    @ApiModelProperty(readOnly = true)
    private UUID uid = UUID.randomUUID();

    @ApiModelProperty(readOnly = true)
    private String createdBy;

    @ApiModelProperty(readOnly = true)
    private String lastModifiedBy;

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }
}
