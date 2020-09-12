package com.manager.timezone.timezonemanagerserver.controller;

import com.manager.timezone.timezonemanagerserver.constants.ServerUri;
import com.manager.timezone.timezonemanagerserver.dto.*;
import com.manager.timezone.timezonemanagerserver.exception.OperationForbiddenException;
import com.manager.timezone.timezonemanagerserver.exception.ResourceNotFoundException;
import com.manager.timezone.timezonemanagerserver.exception.UserExistsException;
import com.manager.timezone.timezonemanagerserver.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Api(tags = "User Operation APIs")
@RestController
@RequestMapping(value = ServerUri.USER_BASE_URI)
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Who Am I?")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = WhoAmIDto.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponseDto.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponseDto.class)
    })
    @GetMapping(value = ServerUri.WHO_AM_I_URI)
    public ResponseEntity<?> whoAmI() {
        try {
            WhoAmIDto whoAmIDto = userService.whoAmI();
            if (whoAmIDto == null) {
                ErrorResponseDto errorResponseDto =
                        new ErrorResponseDto(HttpStatus.UNAUTHORIZED, "User is unauthenticated");
                return new ResponseEntity<>(errorResponseDto, HttpStatus.UNAUTHORIZED);
            }
            return new ResponseEntity<>(whoAmIDto, HttpStatus.OK);
        } catch (Exception e) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponseDto.class),
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
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.UNAUTHORIZED, e.getMessage());
            return new ResponseEntity<>(errorResponseDto, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Register user manager")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = AuthenticateUserResponseDto.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponseDto.class),
            @ApiResponse(code = 409, message = "Conflict", response = ErrorResponseDto.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponseDto.class)
    })
    @PostMapping(value = ServerUri.USER_MANAGER_SIGN_UP_URI)
    public ResponseEntity<?> registerUserManager(@RequestBody RegisterUserRequestDto requestDto) {
        try {
            RegisterUserResponseDto responseDto = userService.registerUserManager(requestDto);
            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        } catch (UserExistsException e) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.CONFLICT, e.getMessage());
            return new ResponseEntity<>(errorResponseDto, HttpStatus.CONFLICT);
        } catch (OperationForbiddenException e) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.UNAUTHORIZED, e.getMessage());
            return new ResponseEntity<>(errorResponseDto, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Register admin")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = AuthenticateUserResponseDto.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponseDto.class),
            @ApiResponse(code = 409, message = "Conflict", response = ErrorResponseDto.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponseDto.class)
    })
    @PostMapping(value = ServerUri.ADMIN_SIGN_UP_URI)
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterUserRequestDto requestDto) {
        try {
            RegisterUserResponseDto responseDto = userService.registerAdmin(requestDto);
            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        } catch (UserExistsException e) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.CONFLICT, e.getMessage());
            return new ResponseEntity<>(errorResponseDto, HttpStatus.CONFLICT);
        } catch (OperationForbiddenException e) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.UNAUTHORIZED, e.getMessage());
            return new ResponseEntity<>(errorResponseDto, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Update user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = UserDto.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponseDto.class),
            @ApiResponse(code = 404, message = "Not Found", response = ErrorResponseDto.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponseDto.class)
    })
    @PutMapping(ServerUri.RESOURCE_UID_URI)
    public ResponseEntity<?> updateUser(@PathVariable String uid,
            @RequestBody UpdateUserRequestDto updateUserRequestDto) {
        try {
            UserDto updatedUser = userService.updateUser(UUID.fromString(uid), updateUserRequestDto);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (OperationForbiddenException e) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.UNAUTHORIZED, e.getMessage());
            return new ResponseEntity<>(errorResponseDto, HttpStatus.UNAUTHORIZED);
        } catch (ResourceNotFoundException e) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.NOT_FOUND, e.getMessage());
            return new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponseDto.class),
            @ApiResponse(code = 404, message = "Not Found", response = ErrorResponseDto.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponseDto.class)
    })
    @DeleteMapping(ServerUri.RESOURCE_UID_URI)
    public ResponseEntity<?> deleteUser(@PathVariable String uid) {
        try {
            userService.deleteUser(UUID.fromString(uid));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (OperationForbiddenException e) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.UNAUTHORIZED, e.getMessage());
            return new ResponseEntity<>(errorResponseDto, HttpStatus.UNAUTHORIZED);
        } catch (ResourceNotFoundException e) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.NOT_FOUND, e.getMessage());
            return new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
