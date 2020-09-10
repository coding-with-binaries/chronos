package com.manager.timezone.timezonemanagerserver.service;

import com.manager.timezone.timezonemanagerserver.auth.AuthUserDetails;
import com.manager.timezone.timezonemanagerserver.dto.TimeZoneDto;

import java.util.List;

public interface TimeZoneService {
    List<TimeZoneDto> getAllTimeZones();

    List<TimeZoneDto> getAllTimeZonesOfCurrentUser();

    TimeZoneDto addTimeZone(TimeZoneDto timeZoneDto);
}
