package com.manager.timezone.timezonemanagerserver.dto;

import java.util.UUID;

public class BaseDto {
    private UUID uid = UUID.randomUUID();

    private String createdBy;

    private String lastModifiedBy;

    private Long createdOn = System.currentTimeMillis();

    private Long lastModifiedOn = System.currentTimeMillis();

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

    public Long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Long createdOn) {
        this.createdOn = createdOn;
    }

    public Long getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(Long lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }
}
