package com.chronos.chronosserver.repository;

import com.chronos.chronosserver.model.TimeZone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TimeZoneRepository extends JpaRepository<TimeZone, UUID> {
    List<TimeZone> findAllByCreatedBy(String createdBy);

    List<TimeZone> findAllByTimeZoneName(String timeZoneName);

    List<TimeZone> findAllByCreatedByAndTimeZoneName(String createdBy, String timeZoneName);
}
