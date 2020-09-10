package com.manager.timezone.timezonemanagerserver.service.impl;

import com.manager.timezone.timezonemanagerserver.auth.AuthUserDetails;
import com.manager.timezone.timezonemanagerserver.auth.CurrentUser;
import com.manager.timezone.timezonemanagerserver.dto.TimeZoneDto;
import com.manager.timezone.timezonemanagerserver.dto.UserDto;
import com.manager.timezone.timezonemanagerserver.model.TimeZone;
import com.manager.timezone.timezonemanagerserver.repository.TimeZoneRepository;
import com.manager.timezone.timezonemanagerserver.service.TimeZoneService;
import com.manager.timezone.timezonemanagerserver.service.UserService;
import com.manager.timezone.timezonemanagerserver.util.TimeZoneUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TimeZoneServiceImpl implements TimeZoneService {

    @Autowired
    private TimeZoneRepository timeZoneRepository;

    @Autowired
    private UserService userService;

    @Override
    public List<TimeZoneDto> getAllTimeZones() {
        return TimeZoneUtil.convertTimeZonesToDtoList(timeZoneRepository.findAll());
    }

    @Override
    public List<TimeZoneDto> getAllTimeZonesOfCurrentUser() {
        UserDto currentUser = userService.getCurrentAuthenticatedUser();
        if (currentUser != null) {
            return TimeZoneUtil
                    .convertTimeZonesToDtoList(timeZoneRepository.findAllByCreatedBy(currentUser.getUsername()));
        }
        return new ArrayList<>();
    }

    @Override
    public TimeZoneDto addTimeZone(TimeZoneDto timeZoneDto) {
        TimeZone timeZone = TimeZoneUtil.convertDtoToTimeZone(timeZoneDto);
        return TimeZoneUtil.convertTimeZoneToDto(timeZoneRepository.save(timeZone));
    }
}
