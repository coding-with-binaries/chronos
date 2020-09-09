package com.manager.timezone.timezonemanagerserver.controller;

import com.manager.timezone.timezonemanagerserver.constants.ServerUri;
import com.manager.timezone.timezonemanagerserver.dto.ErrorResponseDto;
import com.manager.timezone.timezonemanagerserver.dto.TimeZoneDto;
import com.manager.timezone.timezonemanagerserver.service.TimeZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = ServerUri.TIME_ZONE_BASE_URI)
public class TimeZoneController {

    @Autowired
    private TimeZoneService timeZoneService;

    @GetMapping
    public ResponseEntity<?> getAllTimeZones() {
        try {
            List<TimeZoneDto> timeZoneDtoList = timeZoneService.getAllTimeZones();
            return new ResponseEntity<>(timeZoneDtoList, HttpStatus.OK);
        } catch (Exception e) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
