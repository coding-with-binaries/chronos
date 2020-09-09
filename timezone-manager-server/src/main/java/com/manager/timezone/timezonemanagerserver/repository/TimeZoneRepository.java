package com.manager.timezone.timezonemanagerserver.repository;

import com.manager.timezone.timezonemanagerserver.model.TimeZone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TimeZoneRepository extends JpaRepository<TimeZone, UUID> {
}
