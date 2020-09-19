package com.chronos.chronosserver.controller;

import com.chronos.chronosserver.exception.InvalidResourceException;
import com.chronos.chronosserver.service.TimeZoneService;
import com.chronos.chronosserver.dto.ErrorResponseDto;
import com.chronos.chronosserver.dto.TimeZoneDto;
import com.chronos.chronosserver.exception.OperationForbiddenException;
import com.chronos.chronosserver.exception.ResourceNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
public class TimeZoneControllerTest {

    @InjectMocks
    private TimeZoneController timeZoneController = new TimeZoneController();

    @Mock
    private TimeZoneService timeZoneService;

    @Test
    public void test_getAllTimeZones_Success200OK() {
        TimeZoneDto bengaluruTimeZone = new TimeZoneDto();
        bengaluruTimeZone.setTimeZoneName("Bengaluru Time Zone");

        List<TimeZoneDto> timeZones = Collections.singletonList(bengaluruTimeZone);

        PowerMockito.when(timeZoneService.getAllAuthorizedTimeZones()).thenReturn(timeZones);

        var response = timeZoneController.getAllTimeZones(Optional.empty());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(timeZones, response.getBody());
    }

    @Test
    public void test_getAllTimeZonesWithName_Success200OK() {
        String timeZoneName = "Bengaluru Time Zone";
        TimeZoneDto bengaluruTimeZone = new TimeZoneDto();
        bengaluruTimeZone.setTimeZoneName(timeZoneName);

        List<TimeZoneDto> timeZones = Collections.singletonList(bengaluruTimeZone);

        PowerMockito.when(timeZoneService.getAllAuthorizedTimeZonesByName(timeZoneName)).thenReturn(timeZones);

        var response = timeZoneController.getAllTimeZones(Optional.of(timeZoneName));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(timeZones, response.getBody());
    }

    @Test
    public void test_getAllTimeZonesWithName_Failed500InternalServerError() {

        PowerMockito.when(timeZoneService.getAllAuthorizedTimeZones()).thenThrow(RuntimeException.class);

        var response = timeZoneController.getAllTimeZones(Optional.empty());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponseDto);
        assertEquals(500, ((ErrorResponseDto) response.getBody()).getStatusCode());
    }

    @Test
    public void test_addTimeZone_Success201Created() throws InvalidResourceException {
        TimeZoneDto bengaluruTimeZone = new TimeZoneDto();
        bengaluruTimeZone.setTimeZoneName("Bengaluru Time Zone");

        PowerMockito.when(timeZoneService.addTimeZone(bengaluruTimeZone)).thenReturn(bengaluruTimeZone);

        var response = timeZoneController.addTimeZone(bengaluruTimeZone);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(bengaluruTimeZone, response.getBody());
    }

    @Test
    public void test_addTimeZone_Failed400BadRequest() throws InvalidResourceException {
        TimeZoneDto bengaluruTimeZone = new TimeZoneDto();
        bengaluruTimeZone.setTimeZoneName("");

        PowerMockito.when(timeZoneService.addTimeZone(bengaluruTimeZone)).thenThrow(InvalidResourceException.class);

        var response = timeZoneController.addTimeZone(bengaluruTimeZone);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponseDto);
        assertEquals(400, ((ErrorResponseDto) response.getBody()).getStatusCode());
    }

    @Test
    public void test_addTimeZone_Failed500InternalServerError() throws InvalidResourceException {
        TimeZoneDto bengaluruTimeZone = new TimeZoneDto();
        bengaluruTimeZone.setTimeZoneName("Bengaluru Time Zone");

        PowerMockito.when(timeZoneService.addTimeZone(bengaluruTimeZone)).thenThrow(RuntimeException.class);

        var response = timeZoneController.addTimeZone(bengaluruTimeZone);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponseDto);
        assertEquals(500, ((ErrorResponseDto) response.getBody()).getStatusCode());
    }

    @Test
    public void test_updateTimeZone_Success200OK()
            throws ResourceNotFoundException, OperationForbiddenException, InvalidResourceException {
        TimeZoneDto bengaluruTimeZone = new TimeZoneDto();
        String uid = UUID.randomUUID().toString();
        bengaluruTimeZone.setTimeZoneName("Bengaluru Time Zone");

        PowerMockito.when(timeZoneService.updateTimeZone(bengaluruTimeZone)).thenReturn(bengaluruTimeZone);

        var response = timeZoneController.updateTimeZone(uid, bengaluruTimeZone);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bengaluruTimeZone, response.getBody());
    }

    @Test
    public void test_updateTimeZone_Failed400BadRequest()
            throws ResourceNotFoundException, OperationForbiddenException, InvalidResourceException {
        TimeZoneDto bengaluruTimeZone = new TimeZoneDto();
        String uid = UUID.randomUUID().toString();
        bengaluruTimeZone.setTimeZoneName("Bengaluru Time Zone");

        PowerMockito.when(timeZoneService.updateTimeZone(bengaluruTimeZone))
                .thenThrow(InvalidResourceException.class);

        var response = timeZoneController.updateTimeZone(uid, bengaluruTimeZone);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponseDto);
        assertEquals(400, ((ErrorResponseDto) response.getBody()).getStatusCode());
    }

    @Test
    public void test_updateTimeZone_Failed401Unauthorized()
            throws ResourceNotFoundException, OperationForbiddenException, InvalidResourceException {
        TimeZoneDto bengaluruTimeZone = new TimeZoneDto();
        String uid = UUID.randomUUID().toString();
        bengaluruTimeZone.setTimeZoneName("Bengaluru Time Zone");

        PowerMockito.when(timeZoneService.updateTimeZone(bengaluruTimeZone))
                .thenThrow(OperationForbiddenException.class);

        var response = timeZoneController.updateTimeZone(uid, bengaluruTimeZone);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponseDto);
        assertEquals(401, ((ErrorResponseDto) response.getBody()).getStatusCode());
    }

    @Test
    public void test_updateTimeZone_Failed404NotFound()
            throws ResourceNotFoundException, OperationForbiddenException, InvalidResourceException {
        TimeZoneDto bengaluruTimeZone = new TimeZoneDto();
        String uid = UUID.randomUUID().toString();
        bengaluruTimeZone.setTimeZoneName("invalid-timezone");

        PowerMockito.when(timeZoneService.updateTimeZone(bengaluruTimeZone)).thenThrow(ResourceNotFoundException.class);

        var response = timeZoneController.updateTimeZone(uid, bengaluruTimeZone);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponseDto);
        assertEquals(404, ((ErrorResponseDto) response.getBody()).getStatusCode());
    }

    @Test
    public void test_updateTimeZone_Failed500InternalServerError()
            throws ResourceNotFoundException, OperationForbiddenException, InvalidResourceException {
        TimeZoneDto bengaluruTimeZone = new TimeZoneDto();
        String uid = UUID.randomUUID().toString();
        bengaluruTimeZone.setTimeZoneName("invalid-timezone");

        PowerMockito.when(timeZoneService.updateTimeZone(bengaluruTimeZone)).thenThrow(RuntimeException.class);

        var response = timeZoneController.updateTimeZone(uid, bengaluruTimeZone);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponseDto);
        assertEquals(500, ((ErrorResponseDto) response.getBody()).getStatusCode());
    }

    @Test
    public void test_deleteTimeZone_Success204NoContent()
            throws ResourceNotFoundException, OperationForbiddenException {
        UUID uid = UUID.randomUUID();

        PowerMockito.doNothing().when(timeZoneService).deleteTimeZone(uid);

        var response = timeZoneController.deleteTimeZone(uid.toString());

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void test_deleteTimeZone_Failed400BadRequest() {
        String uid = "invalid-uid";

        var response = timeZoneController.deleteTimeZone(uid);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponseDto);
        assertEquals(400, ((ErrorResponseDto) response.getBody()).getStatusCode());
    }

    @Test
    public void test_deleteTimeZone_Failed401Unauthorized()
            throws ResourceNotFoundException, OperationForbiddenException {
        UUID uid = UUID.randomUUID();

        PowerMockito.doThrow(new OperationForbiddenException()).when(timeZoneService).deleteTimeZone(uid);

        var response = timeZoneController.deleteTimeZone(uid.toString());

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponseDto);
        assertEquals(401, ((ErrorResponseDto) response.getBody()).getStatusCode());
    }

    @Test
    public void test_deleteTimeZone_Failed404NotFound() throws ResourceNotFoundException, OperationForbiddenException {
        UUID uid = UUID.randomUUID();

        PowerMockito.doThrow(new ResourceNotFoundException()).when(timeZoneService).deleteTimeZone(uid);

        var response = timeZoneController.deleteTimeZone(uid.toString());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponseDto);
        assertEquals(404, ((ErrorResponseDto) response.getBody()).getStatusCode());
    }

    @Test
    public void test_deleteTimeZone_Failed500InternalServerError()
            throws ResourceNotFoundException, OperationForbiddenException {
        UUID uid = UUID.randomUUID();

        PowerMockito.doThrow(new RuntimeException()).when(timeZoneService).deleteTimeZone(uid);

        var response = timeZoneController.deleteTimeZone(uid.toString());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponseDto);
        assertEquals(500, ((ErrorResponseDto) response.getBody()).getStatusCode());
    }
}
