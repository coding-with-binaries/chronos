package com.chronos.chronosserver.service;

import com.chronos.chronosserver.dto.*;
import com.chronos.chronosserver.exception.InvalidResourceException;
import com.chronos.chronosserver.exception.UserExistsException;
import com.chronos.chronosserver.exception.OperationForbiddenException;
import com.chronos.chronosserver.exception.ResourceNotFoundException;

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
            throws OperationForbiddenException, ResourceNotFoundException, InvalidResourceException;

    void updateUserPassword(UUID uid, UpdatePasswordDto updatePasswordDto)
            throws OperationForbiddenException, ResourceNotFoundException;

    void deleteUser(UUID uid) throws OperationForbiddenException, ResourceNotFoundException;
}
