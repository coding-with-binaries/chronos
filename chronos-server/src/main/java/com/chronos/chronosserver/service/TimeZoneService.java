package com.chronos.chronosserver.service;

import com.chronos.chronosserver.dto.TimeZoneDto;
import com.chronos.chronosserver.exception.InvalidResourceException;
import com.chronos.chronosserver.exception.OperationForbiddenException;
import com.chronos.chronosserver.exception.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;

public interface TimeZoneService {
    List<TimeZoneDto> getAllTimeZones();

    List<TimeZoneDto> getAllAuthorizedTimeZones();

    List<TimeZoneDto> getAllAuthorizedTimeZonesByName(String timeZoneName);

    TimeZoneDto addTimeZone(TimeZoneDto timeZoneDto) throws InvalidResourceException;

    TimeZoneDto updateTimeZone(TimeZoneDto timeZoneDto)
            throws OperationForbiddenException, ResourceNotFoundException, InvalidResourceException;

    void deleteTimeZone(UUID uid) throws OperationForbiddenException, ResourceNotFoundException;
}
