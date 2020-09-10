package com.manager.timezone.timezonemanagerserver.controller;

import com.manager.timezone.timezonemanagerserver.auth.AuthUserDetails;
import com.manager.timezone.timezonemanagerserver.auth.CurrentUser;
import com.manager.timezone.timezonemanagerserver.constants.ServerUri;
import com.manager.timezone.timezonemanagerserver.dto.ErrorResponseDto;
import com.manager.timezone.timezonemanagerserver.dto.TimeZoneDto;
import com.manager.timezone.timezonemanagerserver.service.TimeZoneService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Api(tags = "Time Zone Operation APIs")
@RestController
@RequestMapping(value = ServerUri.TIME_ZONE_BASE_URI)
public class TimeZoneController {

    @Autowired
    private TimeZoneService timeZoneService;

    @ApiOperation(value = "Get all time zones")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = TimeZoneDto.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponseDto.class)
    })
    @GetMapping
    public ResponseEntity<?> getAllTimeZones(@RequestParam Optional<Boolean> showAll) {
        try {
            List<TimeZoneDto> timeZoneDtoList;
            if(showAll.isPresent()&&showAll.get()){
                timeZoneDtoList = timeZoneService.getAllTimeZones();
            } else {
                timeZoneDtoList = timeZoneService.getAllTimeZonesOfCurrentUser();
            }
            return new ResponseEntity<>(timeZoneDtoList, HttpStatus.OK);
        } catch (Exception e) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Add time zone")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = TimeZoneDto.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponseDto.class)
    })
    @PostMapping
    public ResponseEntity<?> addTimeZone(@RequestBody TimeZoneDto timeZoneDto) {
        try {
            TimeZoneDto addedTimeZoneDto = timeZoneService.addTimeZone(timeZoneDto);
            return new ResponseEntity<>(addedTimeZoneDto, HttpStatus.CREATED);
        } catch (Exception e) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
