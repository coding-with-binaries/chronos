package com.manager.timezone.timezonemanagerserver.repository;

import com.manager.timezone.timezonemanagerserver.model.TimeZone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TimeZoneRepository extends JpaRepository<TimeZone, UUID> {
    List<TimeZone> findAllByCreatedBy(String createdBy);

    List<TimeZone> findAllByTimeZoneName(String timeZoneName);

    List<TimeZone> findAllByCreatedByAndTimeZoneName(String createdBy, String timeZoneName);
}
