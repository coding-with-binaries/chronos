package com.chronos.chronosserver.model;

import com.chronos.chronosserver.dto.RoleType;

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
