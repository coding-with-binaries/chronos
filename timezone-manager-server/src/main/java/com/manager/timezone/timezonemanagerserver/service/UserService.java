package com.manager.timezone.timezonemanagerserver.service;

import com.manager.timezone.timezonemanagerserver.auth.AuthUserDetails;
import com.manager.timezone.timezonemanagerserver.dto.*;
import com.manager.timezone.timezonemanagerserver.exception.OperationForbiddenException;
import com.manager.timezone.timezonemanagerserver.exception.ResourceNotFoundException;
import com.manager.timezone.timezonemanagerserver.exception.UserExistsException;
import com.manager.timezone.timezonemanagerserver.model.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    boolean isUsernameTaken(String username);

    UserDto getCurrentAuthenticatedUser();

    WhoAmIDto whoAmI();

    AuthenticateUserResponseDto authenticateUser(AuthenticateUserRequestDto authenticateUserRequestDto);

    UserDto registerUser(RegisterUserRequestDto registerUserRequestDto)
            throws UserExistsException, OperationForbiddenException;

    List<UserDto> getAllUsers() throws OperationForbiddenException;

    UserDto getUser(UUID uid) throws OperationForbiddenException, ResourceNotFoundException;

    UserDto updateUser(UUID uid, UpdateUserRequestDto updateUserRequestDto)
            throws OperationForbiddenException, ResourceNotFoundException;

    void deleteUser(UUID uid) throws OperationForbiddenException, ResourceNotFoundException;
}
