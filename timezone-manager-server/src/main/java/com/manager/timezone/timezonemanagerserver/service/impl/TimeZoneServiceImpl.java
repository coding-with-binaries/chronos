package com.manager.timezone.timezonemanagerserver.service.impl;

import com.manager.timezone.timezonemanagerserver.constants.TimeZoneConstants;
import com.manager.timezone.timezonemanagerserver.dto.RoleDto;
import com.manager.timezone.timezonemanagerserver.dto.TimeZoneDto;
import com.manager.timezone.timezonemanagerserver.dto.UserDto;
import com.manager.timezone.timezonemanagerserver.exception.InvalidResourceException;
import com.manager.timezone.timezonemanagerserver.exception.OperationForbiddenException;
import com.manager.timezone.timezonemanagerserver.exception.ResourceNotFoundException;
import com.manager.timezone.timezonemanagerserver.model.TimeZone;
import com.manager.timezone.timezonemanagerserver.repository.TimeZoneRepository;
import com.manager.timezone.timezonemanagerserver.service.TimeZoneService;
import com.manager.timezone.timezonemanagerserver.service.UserService;
import com.manager.timezone.timezonemanagerserver.util.TimeZoneUtil;
import com.manager.timezone.timezonemanagerserver.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
    public List<TimeZoneDto> getAllAuthorizedTimeZones() {
        UserDto currentUser = userService.getCurrentAuthenticatedUser();
        if (currentUser != null) {
            if (UserUtil.hasAdminAuthority(currentUser.getRole())) {
                return getAllTimeZones();
            }
            return TimeZoneUtil
                    .convertTimeZonesToDtoList(timeZoneRepository.findAllByCreatedBy(currentUser.getUsername()));
        }
        return new ArrayList<>();
    }

    @Override
    public List<TimeZoneDto> getAllAuthorizedTimeZonesByName(String timeZoneName) {
        UserDto currentUser = userService.getCurrentAuthenticatedUser();
        if (currentUser != null) {
            if (UserUtil.hasAdminAuthority(currentUser.getRole())) {
                return TimeZoneUtil.convertTimeZonesToDtoList(timeZoneRepository.findAllByTimeZoneName(timeZoneName));
            }
            return TimeZoneUtil.convertTimeZonesToDtoList(
                    timeZoneRepository.findAllByCreatedByAndTimeZoneName(currentUser.getUsername(), timeZoneName));
        }
        return new ArrayList<>();
    }

    @Override
    public TimeZoneDto addTimeZone(TimeZoneDto timeZoneDto) throws InvalidResourceException {
        if (!TimeZoneConstants.VALID_TIME_ZONE_OFFSETS.contains(timeZoneDto.getDifferenceFromGmt())) {
            throw new InvalidResourceException(
                    "The time zone offset is not valid: " + timeZoneDto.getDifferenceFromGmt());
        }
        TimeZone timeZone = TimeZoneUtil.convertDtoToTimeZone(timeZoneDto);
        return TimeZoneUtil.convertTimeZoneToDto(timeZoneRepository.save(timeZone));
    }

    @Override
    public TimeZoneDto updateTimeZone(TimeZoneDto timeZoneDto)
            throws OperationForbiddenException, ResourceNotFoundException, InvalidResourceException {
        UserDto currentAuthenticatedUser = userService.getCurrentAuthenticatedUser();
        if (currentAuthenticatedUser == null) {
            throw new OperationForbiddenException("Operation cannot be performed unauthenticated");
        }
        Optional<TimeZone> optionalTimeZone = timeZoneRepository.findById(timeZoneDto.getUid());
        if (optionalTimeZone.isPresent()) {
            RoleDto role = currentAuthenticatedUser.getRole();
            TimeZone timeZone = optionalTimeZone.get();
            String owner = timeZone.getCreatedBy();
            if (currentAuthenticatedUser.getUsername().equals(owner) || UserUtil.hasAdminAuthority(role)) {
                if (!TimeZoneConstants.VALID_TIME_ZONE_OFFSETS.contains(timeZoneDto.getDifferenceFromGmt())) {
                    throw new InvalidResourceException(
                            "The time zone offset is not valid: " + timeZoneDto.getDifferenceFromGmt());
                }
                timeZone.setDifferenceFromGmt(timeZoneDto.getDifferenceFromGmt());
                timeZone.setLocationName(timeZoneDto.getLocationName());
                timeZone.setTimeZoneName(timeZoneDto.getTimeZoneName());
                TimeZone updatedTimeZone = timeZoneRepository.save(timeZone);
                return TimeZoneUtil.convertTimeZoneToDto(updatedTimeZone);
            } else {
                throw new OperationForbiddenException("User: " + currentAuthenticatedUser.getUsername() +
                        " does not have the authorization to update the timezone.");
            }
        } else {
            throw new ResourceNotFoundException("Time Zone not present with the ID: " + timeZoneDto.getUid());
        }
    }

    @Override
    public void deleteTimeZone(UUID uid) throws OperationForbiddenException, ResourceNotFoundException {
        UserDto currentAuthenticatedUser = userService.getCurrentAuthenticatedUser();
        if (currentAuthenticatedUser == null) {
            throw new OperationForbiddenException("Operation cannot be performed unauthenticated");
        }
        Optional<TimeZone> optionalTimeZone = timeZoneRepository.findById(uid);
        if (optionalTimeZone.isPresent()) {
            RoleDto role = currentAuthenticatedUser.getRole();
            String owner = optionalTimeZone.get().getCreatedBy();
            if (currentAuthenticatedUser.getUsername().equals(owner) || UserUtil.hasAdminAuthority(role)) {
                timeZoneRepository.delete(optionalTimeZone.get());
            } else {
                throw new OperationForbiddenException("User: " + currentAuthenticatedUser.getUsername() +
                        " does not have the authorization to delete the timezone.");
            }
        } else {
            throw new ResourceNotFoundException("Time Zone not present with the ID: " + uid);
        }
    }
}
