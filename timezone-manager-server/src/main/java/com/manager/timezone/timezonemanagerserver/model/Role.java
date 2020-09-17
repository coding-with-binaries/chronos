package com.manager.timezone.timezonemanagerserver.model;

import com.manager.timezone.timezonemanagerserver.dto.RoleType;

import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "roles")
@Entity
public class Role extends BaseEntity {
    private RoleType type;

    public Role() {

    }

    public Role(RoleType type) {
        this.type = type;
    }

    public RoleType getType() {
        return type;
    }

    public void setType(RoleType type) {
        this.type = type;
    }
}
