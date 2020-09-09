package com.manager.timezone.timezonemanagerserver.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "roles")
@Entity
public class Role extends BaseEntity {
    public enum RoleType {
        admin, user_manager, user
    }

    private RoleType type;

    public RoleType getType() {
        return type;
    }

    public void setType(RoleType type) {
        this.type = type;
    }
}
