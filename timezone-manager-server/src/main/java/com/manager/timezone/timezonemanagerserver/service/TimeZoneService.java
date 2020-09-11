package com.manager.timezone.timezonemanagerserver.service;

import com.manager.timezone.timezonemanagerserver.auth.AuthUserDetails;
import com.manager.timezone.timezonemanagerserver.dto.TimeZoneDto;
import com.manager.timezone.timezonemanagerserver.exception.OperationForbiddenException;
import com.manager.timezone.timezonemanagerserver.exception.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;

public interface TimeZoneService {
    List<TimeZoneDto> getAllTimeZones();

    List<TimeZoneDto> getAllAuthorizedTimeZones();

    TimeZoneDto addTimeZone(TimeZoneDto timeZoneDto);

    TimeZoneDto updateTimeZone(TimeZoneDto timeZoneDto) throws OperationForbiddenException, ResourceNotFoundException;

    void deleteTimeZone(UUID uid) throws OperationForbiddenException, ResourceNotFoundException;
}
