package com.chronos.chronosserver.controller;

import com.chronos.chronosserver.service.TimeZoneService;
import com.chronos.chronosserver.constants.ServerUri;
import com.chronos.chronosserver.dto.ErrorResponseDto;
import com.chronos.chronosserver.dto.TimeZoneDto;
import com.chronos.chronosserver.exception.InvalidResourceException;
import com.chronos.chronosserver.exception.OperationForbiddenException;
import com.chronos.chronosserver.exception.ResourceNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public ResponseEntity<?> getAllTimeZones(@RequestParam Optional<String> timeZoneName) {
        try {
            List<TimeZoneDto> timeZoneDtoList;
            if (timeZoneName.isPresent()) {
                timeZoneDtoList = timeZoneService.getAllAuthorizedTimeZonesByName(timeZoneName.get());
            } else {
                timeZoneDtoList = timeZoneService.getAllAuthorizedTimeZones();
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
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponseDto.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponseDto.class)
    })
    @PostMapping
    public ResponseEntity<?> addTimeZone(@Valid @RequestBody TimeZoneDto timeZoneDto) {
        try {
            TimeZoneDto addedTimeZoneDto = timeZoneService.addTimeZone(timeZoneDto);
            return new ResponseEntity<>(addedTimeZoneDto, HttpStatus.CREATED);
        } catch (InvalidResourceException e) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.BAD_REQUEST, e.getMessage());
            return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Update time zone")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = TimeZoneDto.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponseDto.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponseDto.class),
            @ApiResponse(code = 404, message = "Not Found", response = ErrorResponseDto.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponseDto.class)
    })
    @PutMapping(ServerUri.RESOURCE_UID_URI)
    public ResponseEntity<?> updateTimeZone(@PathVariable String uid, @Valid @RequestBody TimeZoneDto timeZoneDto) {
        try {
            timeZoneDto.setUid(UUID.fromString(uid));
            TimeZoneDto updatedTimeZoneDto = timeZoneService.updateTimeZone(timeZoneDto);
            return new ResponseEntity<>(updatedTimeZoneDto, HttpStatus.OK);
        } catch (InvalidResourceException e) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.BAD_REQUEST, e.getMessage());
            return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
        } catch (OperationForbiddenException e) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.UNAUTHORIZED, e.getMessage());
            return new ResponseEntity<>(errorResponseDto, HttpStatus.UNAUTHORIZED);
        } catch (ResourceNotFoundException e) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.NOT_FOUND, e.getMessage());
            return new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Delete time zone")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponseDto.class),
            @ApiResponse(code = 404, message = "Not Found", response = ErrorResponseDto.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponseDto.class)
    })
    @DeleteMapping(ServerUri.RESOURCE_UID_URI)
    public ResponseEntity<?> deleteTimeZone(@PathVariable String uid) {
        try {
            timeZoneService.deleteTimeZone(UUID.fromString(uid));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (OperationForbiddenException e) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.UNAUTHORIZED, e.getMessage());
            return new ResponseEntity<>(errorResponseDto, HttpStatus.UNAUTHORIZED);
        } catch (ResourceNotFoundException e) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.NOT_FOUND, e.getMessage());
            return new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
