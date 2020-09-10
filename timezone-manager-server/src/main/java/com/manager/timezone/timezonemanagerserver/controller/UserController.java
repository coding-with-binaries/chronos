package com.manager.timezone.timezonemanagerserver.controller;

import com.manager.timezone.timezonemanagerserver.constants.ServerUri;
import com.manager.timezone.timezonemanagerserver.dto.*;
import com.manager.timezone.timezonemanagerserver.exception.OperationForbiddenException;
import com.manager.timezone.timezonemanagerserver.exception.UserExistsException;
import com.manager.timezone.timezonemanagerserver.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "User Operation APIs")
@RestController
@RequestMapping(value = ServerUri.USER_BASE_URI)
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Authenticate user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = AuthenticateUserResponseDto.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponseDto.class)
    })
    @PostMapping(value = ServerUri.SIGN_IN_URI)
    public ResponseEntity<?> authenticateUser(@RequestBody AuthenticateUserRequestDto requestDto) {
        try {
            AuthenticateUserResponseDto responseDto = userService.authenticateUser(requestDto);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (Exception e) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Register user")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = AuthenticateUserResponseDto.class),
            @ApiResponse(code = 409, message = "Conflict", response = ErrorResponseDto.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponseDto.class)
    })
    @PostMapping(value = ServerUri.SIGN_UP_URI)
    public ResponseEntity<?> registerUser(@RequestBody RegisterUserRequestDto requestDto) {
        try {
            RegisterUserResponseDto responseDto = userService.registerUser(requestDto);
            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        } catch (UserExistsException e) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.CONFLICT, e.getMessage());
            return new ResponseEntity<>(errorResponseDto, HttpStatus.CONFLICT);
        } catch (OperationForbiddenException e) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.FORBIDDEN, e.getMessage());
            return new ResponseEntity<>(errorResponseDto, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
