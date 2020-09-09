package com.manager.timezone.timezonemanagerserver.controller;

import com.manager.timezone.timezonemanagerserver.constants.ServerUri;
import com.manager.timezone.timezonemanagerserver.dto.*;
import com.manager.timezone.timezonemanagerserver.exception.UserExistsException;
import com.manager.timezone.timezonemanagerserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = ServerUri.USER_BASE_URI)
public class UserController {

    @Autowired
    private UserService userService;

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

    @PostMapping(value = ServerUri.SIGN_UP_URI)
    public ResponseEntity<?> registerUser(@RequestBody RegisterUserRequestDto requestDto) {
        try {
            RegisterUserResponseDto responseDto = userService.registerUser(requestDto);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (UserExistsException e) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.CONFLICT, e.getMessage());
            return new ResponseEntity<>(errorResponseDto, HttpStatus.CONFLICT);
        } catch (Exception e) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
