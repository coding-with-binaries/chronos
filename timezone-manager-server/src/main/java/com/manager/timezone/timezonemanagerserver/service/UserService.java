package com.manager.timezone.timezonemanagerserver.service;

import com.manager.timezone.timezonemanagerserver.auth.AuthUserDetails;
import com.manager.timezone.timezonemanagerserver.dto.*;
import com.manager.timezone.timezonemanagerserver.exception.OperationForbiddenException;
import com.manager.timezone.timezonemanagerserver.exception.ResourceNotFoundException;
import com.manager.timezone.timezonemanagerserver.exception.UserExistsException;

import java.util.UUID;

public interface UserService {
    boolean isUsernameTaken(String username);

    UserDto getCurrentAuthenticatedUser();

    AuthenticateUserResponseDto authenticateUser(AuthenticateUserRequestDto authenticateUserRequestDto);

    RegisterUserResponseDto registerUser(RegisterUserRequestDto registerUserRequestDto)
            throws UserExistsException, OperationForbiddenException;

    RegisterUserResponseDto registerUserManager(RegisterUserRequestDto registerUserRequestDto)
            throws UserExistsException, OperationForbiddenException;

    RegisterUserResponseDto registerAdmin(RegisterUserRequestDto registerUserRequestDto)
            throws UserExistsException, OperationForbiddenException;

    void updateUser(UUID uid, UpdateUserRequestDto updateUserRequestDto)
            throws OperationForbiddenException, ResourceNotFoundException;

    void deleteUser(UUID uid) throws OperationForbiddenException, ResourceNotFoundException;
}
