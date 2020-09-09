package com.manager.timezone.timezonemanagerserver.service;

import com.manager.timezone.timezonemanagerserver.dto.AuthenticateUserRequestDto;
import com.manager.timezone.timezonemanagerserver.dto.AuthenticateUserResponseDto;
import com.manager.timezone.timezonemanagerserver.dto.RegisterUserRequestDto;
import com.manager.timezone.timezonemanagerserver.dto.RegisterUserResponseDto;
import com.manager.timezone.timezonemanagerserver.exception.UserExistsException;

public interface UserService {
    boolean isUsernameTaken(String username);

    AuthenticateUserResponseDto authenticateUser(AuthenticateUserRequestDto authenticateUserRequestDto);

    RegisterUserResponseDto registerUser(RegisterUserRequestDto registerUserRequestDto) throws UserExistsException;
}
