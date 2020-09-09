package com.manager.timezone.timezonemanagerserver.service;

import com.manager.timezone.timezonemanagerserver.dto.TimeZoneDto;

import java.util.List;

public interface TimeZoneService {
    List<TimeZoneDto> getAllTimeZones();
}
