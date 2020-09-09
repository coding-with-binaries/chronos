package com.manager.timezone.timezonemanagerserver.service.impl;

import com.manager.timezone.timezonemanagerserver.dto.TimeZoneDto;
import com.manager.timezone.timezonemanagerserver.repository.TimeZoneRepository;
import com.manager.timezone.timezonemanagerserver.service.TimeZoneService;
import com.manager.timezone.timezonemanagerserver.util.TimeZoneUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeZoneServiceImpl implements TimeZoneService {

    @Autowired
    private TimeZoneRepository timeZoneRepository;

    @Override
    public List<TimeZoneDto> getAllTimeZones() {
        return TimeZoneUtil.convertTimeZonesToDtoList(timeZoneRepository.findAll());
    }
}
