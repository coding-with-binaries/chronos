package com.chronos.chronosserver.service;

import com.chronos.chronosserver.dto.RoleDto;
import com.chronos.chronosserver.dto.RoleType;
import com.chronos.chronosserver.dto.UserDto;
import com.chronos.chronosserver.exception.InvalidResourceException;
import com.chronos.chronosserver.model.TimeZone;
import com.chronos.chronosserver.repository.TimeZoneRepository;
import com.chronos.chronosserver.dto.TimeZoneDto;
import com.chronos.chronosserver.exception.OperationForbiddenException;
import com.chronos.chronosserver.exception.ResourceNotFoundException;
import com.chronos.chronosserver.service.impl.TimeZoneServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class TimeZoneServiceTest {
    private static final String BENGALURU_TIME_ZONE = "Bengaluru";

    private static final String NEPAL_TIME_ZONE = "Nepal";

    private static final String USERNAME = "varun";

    @TestConfiguration
    static class TimeZoneServiceTestContextConfiguration {

        @Bean
        public TimeZoneService timeZoneService() {
            return new TimeZoneServiceImpl();
        }
    }

    @Autowired
    private TimeZoneService timeZoneService;

    @MockBean
    private TimeZoneRepository timeZoneRepository;

    @MockBean
    private UserService userService;

    @Before
    public void setUp() {
        com.chronos.chronosserver.model.TimeZone bengaluruTimeZone = new com.chronos.chronosserver.model.TimeZone();
        bengaluruTimeZone.setTimeZoneName(BENGALURU_TIME_ZONE);
        bengaluruTimeZone.setCreatedBy(USERNAME);

        com.chronos.chronosserver.model.TimeZone nepalTimeZone = new com.chronos.chronosserver.model.TimeZone();
        nepalTimeZone.setTimeZoneName(NEPAL_TIME_ZONE);
        nepalTimeZone.setCreatedBy("user");

        List<com.chronos.chronosserver.model.TimeZone> timeZones = Arrays.asList(bengaluruTimeZone, nepalTimeZone);

        PowerMockito.when(timeZoneRepository.findAll()).thenReturn(timeZones);
        PowerMockito.when(timeZoneRepository.findAllByTimeZoneName(BENGALURU_TIME_ZONE))
                .thenReturn(Collections.singletonList(bengaluruTimeZone));
        PowerMockito.when(timeZoneRepository.findAllByCreatedBy("user"))
                .thenReturn(Collections.singletonList(nepalTimeZone));
        PowerMockito.when(timeZoneRepository.findById(any()))
                .thenReturn(Optional.of(bengaluruTimeZone));
        PowerMockito.when(timeZoneRepository.save(any(TimeZone.class))).thenReturn(bengaluruTimeZone);

        PowerMockito.doNothing().when(timeZoneRepository).delete(any());

        UserDto currentAuthenticatedUser = new UserDto();
        currentAuthenticatedUser.setUsername(USERNAME);
        RoleDto roleDto = new RoleDto();
        roleDto.setType(RoleType.admin);
        currentAuthenticatedUser.setRole(roleDto);
        PowerMockito.when(userService.getCurrentAuthenticatedUser()).thenReturn(currentAuthenticatedUser);
    }

    @Test
    public void test_getAllTimeZones() {
        List<TimeZoneDto> timeZones = timeZoneService.getAllTimeZones();
        assertEquals(2, timeZones.size());
    }

    @Test
    public void test_getAllAuthorizedTimeZones_WithAdmin() {
        List<TimeZoneDto> timeZones = timeZoneService.getAllAuthorizedTimeZones();
        assertEquals(2, timeZones.size());
    }

    @Test
    public void test_getAllAuthorizedTimeZones_WithUser() {
        UserDto currentAuthenticatedUser = new UserDto();
        currentAuthenticatedUser.setUsername("user");
        RoleDto roleDto = new RoleDto();
        roleDto.setType(RoleType.user);
        currentAuthenticatedUser.setRole(roleDto);
        PowerMockito.when(userService.getCurrentAuthenticatedUser()).thenReturn(currentAuthenticatedUser);

        List<TimeZoneDto> timeZones = timeZoneService.getAllAuthorizedTimeZones();
        assertEquals(1, timeZones.size());
    }

    @Test
    public void test_getAllAuthorizedTimeZones_Unauthenticated() {
        PowerMockito.when(userService.getCurrentAuthenticatedUser()).thenReturn(null);

        List<TimeZoneDto> timeZones = timeZoneService.getAllAuthorizedTimeZones();
        assertEquals(0, timeZones.size());
    }

    @Test
    public void test_getAllTimeZonesByName() {
        List<TimeZoneDto> timeZones = timeZoneService.getAllAuthorizedTimeZonesByName(BENGALURU_TIME_ZONE);
        assertEquals(1, timeZones.size());
    }

    @Test
    public void test_getAllAuthorizedTimeZonesByName_WithAdmin() {
        List<TimeZoneDto> timeZones = timeZoneService.getAllAuthorizedTimeZonesByName(BENGALURU_TIME_ZONE);
        assertEquals(1, timeZones.size());
    }

    @Test
    public void test_getAllAuthorizedTimeZonesByName_WithUser() {
        UserDto currentAuthenticatedUser = new UserDto();
        currentAuthenticatedUser.setUsername("user");
        RoleDto roleDto = new RoleDto();
        roleDto.setType(RoleType.user);
        currentAuthenticatedUser.setRole(roleDto);
        PowerMockito.when(userService.getCurrentAuthenticatedUser()).thenReturn(currentAuthenticatedUser);

        List<TimeZoneDto> timeZones = timeZoneService.getAllAuthorizedTimeZonesByName(BENGALURU_TIME_ZONE);
        assertEquals(0, timeZones.size());
    }

    @Test
    public void test_getAllAuthorizedTimeZonesByName_Unauthenticated() {
        PowerMockito.when(userService.getCurrentAuthenticatedUser()).thenReturn(null);

        List<TimeZoneDto> timeZones = timeZoneService.getAllAuthorizedTimeZonesByName(BENGALURU_TIME_ZONE);
        assertEquals(0, timeZones.size());
    }

    @Test
    public void test_addTimeZone() throws InvalidResourceException {
        TimeZoneDto givenTimeZone = new TimeZoneDto();
        givenTimeZone.setTimeZoneName(BENGALURU_TIME_ZONE);
        givenTimeZone.setDifferenceFromGmt("+05:30");
        TimeZoneDto timeZone = timeZoneService.addTimeZone(givenTimeZone);
        assertEquals(BENGALURU_TIME_ZONE, timeZone.getTimeZoneName());
    }

    @Test(expected = InvalidResourceException.class)
    public void test_addTimeZone_Invalid() throws InvalidResourceException {
        TimeZoneDto givenTimeZone = new TimeZoneDto();
        givenTimeZone.setDifferenceFromGmt("+18:30");
        timeZoneService.addTimeZone(givenTimeZone);
    }

    @Test
    public void test_updateTimeZone()
            throws InvalidResourceException, ResourceNotFoundException, OperationForbiddenException {
        TimeZoneDto givenTimeZone = new TimeZoneDto();
        givenTimeZone.setTimeZoneName(BENGALURU_TIME_ZONE);
        givenTimeZone.setDifferenceFromGmt("+05:30");
        TimeZoneDto timeZone = timeZoneService.updateTimeZone(givenTimeZone);
        assertEquals(BENGALURU_TIME_ZONE, timeZone.getTimeZoneName());
    }

    @Test(expected = InvalidResourceException.class)
    public void test_updateTimeZone_Invalid()
            throws InvalidResourceException, ResourceNotFoundException, OperationForbiddenException {
        TimeZoneDto givenTimeZone = new TimeZoneDto();
        givenTimeZone.setDifferenceFromGmt("+18:30");
        timeZoneService.updateTimeZone(givenTimeZone);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void test_updateTimeZone_NotFound()
            throws InvalidResourceException, ResourceNotFoundException, OperationForbiddenException {
        PowerMockito.when(timeZoneRepository.findById(any())).thenReturn(Optional.empty());
        TimeZoneDto givenTimeZone = new TimeZoneDto();
        givenTimeZone.setDifferenceFromGmt("+05:30");
        timeZoneService.updateTimeZone(givenTimeZone);
    }

    @Test(expected = OperationForbiddenException.class)
    public void test_updateTimeZone_Unauthenticated()
            throws InvalidResourceException, ResourceNotFoundException, OperationForbiddenException {
        PowerMockito.when(userService.getCurrentAuthenticatedUser()).thenReturn(null);

        TimeZoneDto givenTimeZone = new TimeZoneDto();
        givenTimeZone.setDifferenceFromGmt("+05:30");
        timeZoneService.updateTimeZone(givenTimeZone);
    }

    @Test(expected = OperationForbiddenException.class)
    public void test_updateTimeZone_Forbidden()
            throws InvalidResourceException, ResourceNotFoundException, OperationForbiddenException {
        UserDto currentAuthenticatedUser = new UserDto();
        currentAuthenticatedUser.setUsername("user");
        RoleDto roleDto = new RoleDto();
        roleDto.setType(RoleType.user);
        currentAuthenticatedUser.setRole(roleDto);
        PowerMockito.when(userService.getCurrentAuthenticatedUser()).thenReturn(currentAuthenticatedUser);

        TimeZoneDto givenTimeZone = new TimeZoneDto();
        givenTimeZone.setDifferenceFromGmt("+05:30");
        timeZoneService.updateTimeZone(givenTimeZone);
    }

    @Test
    public void test_deleteTimeZone() throws ResourceNotFoundException, OperationForbiddenException {
        TimeZoneDto givenTimeZone = new TimeZoneDto();
        givenTimeZone.setTimeZoneName(BENGALURU_TIME_ZONE);
        givenTimeZone.setDifferenceFromGmt("+05:30");
        timeZoneService.deleteTimeZone(givenTimeZone.getUid());

        verify(timeZoneRepository, times(1)).delete(any());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void test_deleteTimeZone_NotFound() throws ResourceNotFoundException, OperationForbiddenException {
        PowerMockito.when(timeZoneRepository.findById(any())).thenReturn(Optional.empty());
        TimeZoneDto givenTimeZone = new TimeZoneDto();
        givenTimeZone.setDifferenceFromGmt("+05:30");
        timeZoneService.deleteTimeZone(givenTimeZone.getUid());
    }

    @Test(expected = OperationForbiddenException.class)
    public void test_deleteTimeZone_Unauthenticated() throws ResourceNotFoundException, OperationForbiddenException {
        PowerMockito.when(userService.getCurrentAuthenticatedUser()).thenReturn(null);

        TimeZoneDto givenTimeZone = new TimeZoneDto();
        givenTimeZone.setDifferenceFromGmt("+05:30");
        timeZoneService.deleteTimeZone(givenTimeZone.getUid());
    }

    @Test(expected = OperationForbiddenException.class)
    public void test_deleteTimeZone_Forbidden() throws ResourceNotFoundException, OperationForbiddenException {
        UserDto currentAuthenticatedUser = new UserDto();
        currentAuthenticatedUser.setUsername("user");
        RoleDto roleDto = new RoleDto();
        roleDto.setType(RoleType.user);
        currentAuthenticatedUser.setRole(roleDto);
        PowerMockito.when(userService.getCurrentAuthenticatedUser()).thenReturn(currentAuthenticatedUser);

        TimeZoneDto givenTimeZone = new TimeZoneDto();
        givenTimeZone.setDifferenceFromGmt("+05:30");
        timeZoneService.deleteTimeZone(givenTimeZone.getUid());
    }
}
