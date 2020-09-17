package com.manager.timezone.timezonemanagerserver.controller;

import com.manager.timezone.timezonemanagerserver.dto.*;
import com.manager.timezone.timezonemanagerserver.exception.InvalidResourceException;
import com.manager.timezone.timezonemanagerserver.exception.OperationForbiddenException;
import com.manager.timezone.timezonemanagerserver.exception.ResourceNotFoundException;
import com.manager.timezone.timezonemanagerserver.exception.UserExistsException;
import com.manager.timezone.timezonemanagerserver.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
public class UserControllerTest {
    private static final String USERNAME = "varun";

    @InjectMocks
    private UserController userController = new UserController();

    @Mock
    private UserService userService;

    @Test
    public void test_getAllUsers_Success200OK() throws OperationForbiddenException {
        UserDto user = new UserDto();
        user.setUsername(USERNAME);

        List<UserDto> users = Collections.singletonList(user);
        PowerMockito.when(userService.getAllUsers()).thenReturn(users);

        var response = userController.getAllUsers();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    public void test_getAllUsers_Failed401Unauthorized() throws OperationForbiddenException {

        PowerMockito.when(userService.getAllUsers()).thenThrow(OperationForbiddenException.class);

        var response = userController.getAllUsers();

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponseDto);
        assertEquals(401, ((ErrorResponseDto) response.getBody()).getStatusCode());
    }

    @Test
    public void test_getAllUsers_Failed500InternalServerError() throws OperationForbiddenException {

        PowerMockito.when(userService.getAllUsers()).thenThrow(RuntimeException.class);

        var response = userController.getAllUsers();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponseDto);
        assertEquals(500, ((ErrorResponseDto) response.getBody()).getStatusCode());
    }

    @Test
    public void test_getUser_Success200OK() throws ResourceNotFoundException, OperationForbiddenException {
        String uid = UUID.randomUUID().toString();
        UserDto updatedUser = new UserDto();
        updatedUser.setUsername(USERNAME);

        PowerMockito.when(userService.getUser(UUID.fromString(uid))).thenReturn(updatedUser);

        var response = userController.getUser(uid);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
    }

    @Test
    public void test_getUser_Failed401Unauthorized() throws ResourceNotFoundException, OperationForbiddenException {
        String uid = UUID.randomUUID().toString();
        UserDto updatedUser = new UserDto();
        updatedUser.setUsername(USERNAME);

        PowerMockito.when(userService.getUser(UUID.fromString(uid))).thenThrow(OperationForbiddenException.class);

        var response = userController.getUser(uid);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponseDto);
        assertEquals(401, ((ErrorResponseDto) response.getBody()).getStatusCode());
    }

    @Test
    public void test_getUser_Failed404NotFound() throws ResourceNotFoundException, OperationForbiddenException {
        String uid = UUID.randomUUID().toString();
        UserDto updatedUser = new UserDto();
        updatedUser.setUsername(USERNAME);

        PowerMockito.when(userService.getUser(UUID.fromString(uid))).thenThrow(ResourceNotFoundException.class);

        var response = userController.getUser(uid);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponseDto);
        assertEquals(404, ((ErrorResponseDto) response.getBody()).getStatusCode());
    }

    @Test
    public void test_getUser_Failed500InternalServerError()
            throws ResourceNotFoundException, OperationForbiddenException {
        String uid = UUID.randomUUID().toString();
        UserDto updatedUser = new UserDto();
        updatedUser.setUsername(USERNAME);

        PowerMockito.when(userService.getUser(UUID.fromString(uid))).thenThrow(RuntimeException.class);

        var response = userController.getUser(uid);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponseDto);
        assertEquals(500, ((ErrorResponseDto) response.getBody()).getStatusCode());
    }

    @Test
    public void test_whoAmI_Success200OK() {
        WhoAmIDto whoAmIDto = new WhoAmIDto();
        whoAmIDto.setUsername(USERNAME);

        PowerMockito.when(userService.whoAmI()).thenReturn(whoAmIDto);

        var response = userController.whoAmI();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(whoAmIDto, response.getBody());
    }

    @Test
    public void test_whoAmI_Failed401Unauthorized() {

        PowerMockito.when(userService.whoAmI()).thenReturn(null);

        var response = userController.whoAmI();
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponseDto);
        assertEquals(401, ((ErrorResponseDto) response.getBody()).getStatusCode());
    }

    @Test
    public void test_whoAmI_Failed500InternalServerError() {

        PowerMockito.when(userService.whoAmI()).thenThrow(RuntimeException.class);

        var response = userController.whoAmI();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponseDto);
        assertEquals(500, ((ErrorResponseDto) response.getBody()).getStatusCode());
    }

    @Test
    public void test_authenticateUser_Success200OK() {
        AuthenticateUserRequestDto requestDto = new AuthenticateUserRequestDto();
        requestDto.setUsername(USERNAME);

        AuthenticateUserResponseDto responseDto = new AuthenticateUserResponseDto();
        responseDto.setUsername(USERNAME);

        PowerMockito.when(userService.authenticateUser(requestDto)).thenReturn(responseDto);

        var response = userController.authenticateUser(requestDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
    }

    @Test
    public void test_authenticateUser_Failed500InternalServerError() {
        AuthenticateUserRequestDto requestDto = new AuthenticateUserRequestDto();
        requestDto.setUsername(USERNAME);
        PowerMockito.when(userService.authenticateUser(requestDto)).thenThrow(RuntimeException.class);

        var response = userController.authenticateUser(requestDto);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponseDto);
        assertEquals(500, ((ErrorResponseDto) response.getBody()).getStatusCode());
    }

    @Test
    public void test_registerUser_Success200OK() throws OperationForbiddenException, UserExistsException {
        RegisterUserRequestDto requestDto = new RegisterUserRequestDto();
        requestDto.setUsername(USERNAME);

        UserDto userDto = new UserDto();
        userDto.setUsername(USERNAME);

        PowerMockito.when(userService.registerUser(requestDto)).thenReturn(userDto);

        var response = userController.registerUser(requestDto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userDto, response.getBody());
    }

    @Test
    public void test_registerUser_Failed401Unauthorized() throws OperationForbiddenException, UserExistsException {
        RegisterUserRequestDto requestDto = new RegisterUserRequestDto();
        requestDto.setUsername(USERNAME);
        PowerMockito.when(userService.registerUser(requestDto)).thenThrow(OperationForbiddenException.class);

        var response = userController.registerUser(requestDto);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponseDto);
        assertEquals(401, ((ErrorResponseDto) response.getBody()).getStatusCode());
    }

    @Test
    public void test_registerUser_Failed409Conflict() throws OperationForbiddenException, UserExistsException {
        RegisterUserRequestDto requestDto = new RegisterUserRequestDto();
        requestDto.setUsername(USERNAME);
        PowerMockito.when(userService.registerUser(requestDto)).thenThrow(UserExistsException.class);

        var response = userController.registerUser(requestDto);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponseDto);
        assertEquals(409, ((ErrorResponseDto) response.getBody()).getStatusCode());
    }

    @Test
    public void test_registerUser_Failed500InternalServerError()
            throws OperationForbiddenException, UserExistsException {
        RegisterUserRequestDto requestDto = new RegisterUserRequestDto();
        requestDto.setUsername(USERNAME);
        PowerMockito.when(userService.registerUser(requestDto)).thenThrow(RuntimeException.class);

        var response = userController.registerUser(requestDto);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponseDto);
        assertEquals(500, ((ErrorResponseDto) response.getBody()).getStatusCode());
    }

    @Test
    public void test_updateUser_Success200OK()
            throws InvalidResourceException, OperationForbiddenException, ResourceNotFoundException {
        UUID uid = UUID.randomUUID();
        UpdateUserRequestDto requestDto = new UpdateUserRequestDto();

        UserDto userDto = new UserDto();
        userDto.setUid(uid);

        PowerMockito.when(userService.updateUser(uid, requestDto)).thenReturn(userDto);

        var response = userController.updateUser(uid.toString(), requestDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());
    }

    @Test
    public void test_updateUser_Failed400BadRequest()
            throws ResourceNotFoundException, OperationForbiddenException, InvalidResourceException {
        UUID uid = UUID.randomUUID();
        UpdateUserRequestDto requestDto = new UpdateUserRequestDto();
        PowerMockito.when(userService.updateUser(uid, requestDto)).thenThrow(InvalidResourceException.class);

        var response = userController.updateUser(uid.toString(), requestDto);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponseDto);
        assertEquals(400, ((ErrorResponseDto) response.getBody()).getStatusCode());
    }

    @Test
    public void test_updateUser_Failed401Unauthorized()
            throws ResourceNotFoundException, OperationForbiddenException, InvalidResourceException {
        UUID uid = UUID.randomUUID();
        UpdateUserRequestDto requestDto = new UpdateUserRequestDto();
        PowerMockito.when(userService.updateUser(uid, requestDto)).thenThrow(OperationForbiddenException.class);

        var response = userController.updateUser(uid.toString(), requestDto);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponseDto);
        assertEquals(401, ((ErrorResponseDto) response.getBody()).getStatusCode());
    }

    @Test
    public void test_updateUser_Failed404NotFound()
            throws ResourceNotFoundException, OperationForbiddenException, InvalidResourceException {
        UUID uid = UUID.randomUUID();
        UpdateUserRequestDto requestDto = new UpdateUserRequestDto();
        PowerMockito.when(userService.updateUser(uid, requestDto)).thenThrow(ResourceNotFoundException.class);

        var response = userController.updateUser(uid.toString(), requestDto);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponseDto);
        assertEquals(404, ((ErrorResponseDto) response.getBody()).getStatusCode());
    }

    @Test
    public void test_updateUser_Failed500InternalServerError()
            throws InvalidResourceException, OperationForbiddenException, ResourceNotFoundException {
        UUID uid = UUID.randomUUID();
        UpdateUserRequestDto requestDto = new UpdateUserRequestDto();
        PowerMockito.when(userService.updateUser(uid, requestDto)).thenThrow(RuntimeException.class);

        var response = userController.updateUser(uid.toString(), requestDto);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponseDto);
        assertEquals(500, ((ErrorResponseDto) response.getBody()).getStatusCode());
    }

    @Test
    public void test_updateUserPassword_Success200OK() throws OperationForbiddenException, ResourceNotFoundException {
        UUID uid = UUID.randomUUID();
        UpdatePasswordDto updatePasswordDto = new UpdatePasswordDto();

        PowerMockito.doNothing().when(userService).updateUserPassword(uid, updatePasswordDto);

        var response = userController.updateUserPassword(uid.toString(), updatePasswordDto);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void test_updateUserPassword_Failed401Unauthorized()
            throws ResourceNotFoundException, OperationForbiddenException {
        UUID uid = UUID.randomUUID();
        UpdatePasswordDto updatePasswordDto = new UpdatePasswordDto();
        PowerMockito.doThrow(new OperationForbiddenException()).when(userService)
                .updateUserPassword(uid, updatePasswordDto);

        var response = userController.updateUserPassword(uid.toString(), updatePasswordDto);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponseDto);
        assertEquals(401, ((ErrorResponseDto) response.getBody()).getStatusCode());
    }

    @Test
    public void test_updateUserPassword_Failed404NotFound()
            throws ResourceNotFoundException, OperationForbiddenException {
        UUID uid = UUID.randomUUID();
        UpdatePasswordDto updatePasswordDto = new UpdatePasswordDto();
        PowerMockito.doThrow(new ResourceNotFoundException()).when(userService)
                .updateUserPassword(uid, updatePasswordDto);

        var response = userController.updateUserPassword(uid.toString(), updatePasswordDto);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponseDto);
        assertEquals(404, ((ErrorResponseDto) response.getBody()).getStatusCode());
    }

    @Test
    public void test_updateUserPassword_Failed500InternalServerError()
            throws ResourceNotFoundException, OperationForbiddenException {
        UUID uid = UUID.randomUUID();
        UpdatePasswordDto updatePasswordDto = new UpdatePasswordDto();
        PowerMockito.doThrow(new RuntimeException()).when(userService)
                .updateUserPassword(uid, updatePasswordDto);

        var response = userController.updateUserPassword(uid.toString(), updatePasswordDto);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponseDto);
        assertEquals(500, ((ErrorResponseDto) response.getBody()).getStatusCode());
    }

    @Test
    public void test_deleteUser_Success200OK() throws OperationForbiddenException, ResourceNotFoundException {
        UUID uid = UUID.randomUUID();

        PowerMockito.doNothing().when(userService).deleteUser(uid);

        var response = userController.deleteUser(uid.toString());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }


    @Test
    public void test_deleteUser_Failed401Unauthorized() throws ResourceNotFoundException, OperationForbiddenException {
        UUID uid = UUID.randomUUID();

        PowerMockito.doThrow(new OperationForbiddenException()).when(userService).deleteUser(uid);

        var response = userController.deleteUser(uid.toString());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponseDto);
        assertEquals(401, ((ErrorResponseDto) response.getBody()).getStatusCode());
    }

    @Test
    public void test_deleteUser_Failed404NotFound() throws ResourceNotFoundException, OperationForbiddenException {
        UUID uid = UUID.randomUUID();

        PowerMockito.doThrow(new ResourceNotFoundException()).when(userService).deleteUser(uid);

        var response = userController.deleteUser(uid.toString());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponseDto);
        assertEquals(404, ((ErrorResponseDto) response.getBody()).getStatusCode());
    }

    @Test
    public void test_deleteUser_Failed500InternalServerError()
            throws OperationForbiddenException, ResourceNotFoundException {
        UUID uid = UUID.randomUUID();

        PowerMockito.doThrow(new RuntimeException()).when(userService).deleteUser(uid);

        var response = userController.deleteUser(uid.toString());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponseDto);
        assertEquals(500, ((ErrorResponseDto) response.getBody()).getStatusCode());
    }
}
