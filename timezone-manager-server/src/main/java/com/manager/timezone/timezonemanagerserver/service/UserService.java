package com.manager.timezone.timezonemanagerserver.service;

import com.manager.timezone.timezonemanagerserver.auth.AuthUserDetails;
import com.manager.timezone.timezonemanagerserver.dto.*;
import com.manager.timezone.timezonemanagerserver.exception.OperationForbiddenException;
import com.manager.timezone.timezonemanagerserver.exception.UserExistsException;

public interface UserService {
    boolean isUsernameTaken(String username);

    UserDto getCurrentAuthenticatedUser();

    AuthenticateUserResponseDto authenticateUser(AuthenticateUserRequestDto authenticateUserRequestDto);

    RegisterUserResponseDto registerUser(RegisterUserRequestDto registerUserRequestDto)
            throws UserExistsException, OperationForbiddenException;
}
