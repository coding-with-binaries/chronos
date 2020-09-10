package com.manager.timezone.timezonemanagerserver.repository;

import com.manager.timezone.timezonemanagerserver.dto.RoleType;
import com.manager.timezone.timezonemanagerserver.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByType(RoleType type);
}
