package com.manager.timezone.timezonemanagerserver.service.impl;

import com.manager.timezone.timezonemanagerserver.auth.AuthUserDetails;
import com.manager.timezone.timezonemanagerserver.auth.JwtTokenProvider;
import com.manager.timezone.timezonemanagerserver.dto.*;
import com.manager.timezone.timezonemanagerserver.exception.OperationForbiddenException;
import com.manager.timezone.timezonemanagerserver.exception.ResourceNotFoundException;
import com.manager.timezone.timezonemanagerserver.exception.UserExistsException;
import com.manager.timezone.timezonemanagerserver.model.Role;
import com.manager.timezone.timezonemanagerserver.model.User;
import com.manager.timezone.timezonemanagerserver.repository.RoleRepository;
import com.manager.timezone.timezonemanagerserver.repository.UserRepository;
import com.manager.timezone.timezonemanagerserver.service.UserService;
import com.manager.timezone.timezonemanagerserver.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public boolean isUsernameTaken(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public UserDto getCurrentAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            AuthUserDetails authUserDetails = ((AuthUserDetails) principal);
            User user = authUserDetails.getUser();
            return UserUtil.convertUserToDto(user);
        }
        return null;
    }

    @Override
    public AuthenticateUserResponseDto authenticateUser(AuthenticateUserRequestDto authenticateUserRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticateUserRequestDto.getUsername(),
                        authenticateUserRequestDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return new AuthenticateUserResponseDto(jwt);

    }

    @Override
    public RegisterUserResponseDto registerUser(RegisterUserRequestDto registerUserRequestDto)
            throws UserExistsException, OperationForbiddenException {
        if (isUsernameTaken(registerUserRequestDto.getUsername())) {
            throw new UserExistsException("User exists with username: " + registerUserRequestDto.getUsername());
        }
        UserDto currentAuthenticatedUser = getCurrentAuthenticatedUser();
        if (currentAuthenticatedUser != null) {
            Set<RoleDto> roles = currentAuthenticatedUser.getRoles();
            if (!UserUtil.hasUserManagementAuthority(roles)) {
                throw new OperationForbiddenException("User: " + currentAuthenticatedUser.getUsername() +
                        " does not have authority to create another user");
            }
        }
        User user = new User();
        user.setUsername(registerUserRequestDto.getUsername());
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(registerUserRequestDto.getPassword()));

        Optional<Role> optionalRole = roleRepository.findByType(RoleType.user);
        Role role;
        if (optionalRole.isEmpty()) {
            role = new Role();
            role.setType(RoleType.user);
            roleRepository.save(role);
        } else {
            role = optionalRole.get();
        }

        user.setRoles(Collections.singleton(role));

        userRepository.save(user);

        RegisterUserResponseDto registerUserResponseDto = new RegisterUserResponseDto();
        registerUserResponseDto.setUid(user.getUid());
        registerUserResponseDto.setUsername(user.getUsername());

        return registerUserResponseDto;
    }

    @Override
    public RegisterUserResponseDto registerUserManager(RegisterUserRequestDto registerUserRequestDto)
            throws UserExistsException, OperationForbiddenException {
        if (isUsernameTaken(registerUserRequestDto.getUsername())) {
            throw new UserExistsException("User exists with username: " + registerUserRequestDto.getUsername());
        }
        UserDto currentAuthenticatedUser = getCurrentAuthenticatedUser();
        if (currentAuthenticatedUser != null) {
            Set<RoleDto> roles = currentAuthenticatedUser.getRoles();
            if (!UserUtil.hasUserManagementAuthority(roles)) {
                throw new OperationForbiddenException("User: " + currentAuthenticatedUser.getUsername() +
                        " does not have authority to create another user");
            }
        }
        User user = new User();
        user.setUsername(registerUserRequestDto.getUsername());
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(registerUserRequestDto.getPassword()));

        Optional<Role> optionalRole = roleRepository.findByType(RoleType.user_manager);
        Role role;
        if (optionalRole.isEmpty()) {
            role = new Role();
            role.setType(RoleType.user_manager);
            roleRepository.save(role);
        } else {
            role = optionalRole.get();
        }

        user.setRoles(Collections.singleton(role));

        userRepository.save(user);

        RegisterUserResponseDto registerUserResponseDto = new RegisterUserResponseDto();
        registerUserResponseDto.setUid(user.getUid());
        registerUserResponseDto.setUsername(user.getUsername());

        return registerUserResponseDto;
    }

    @Override
    public RegisterUserResponseDto registerAdmin(RegisterUserRequestDto registerUserRequestDto)
            throws UserExistsException, OperationForbiddenException {
        if (isUsernameTaken(registerUserRequestDto.getUsername())) {
            throw new UserExistsException("User exists with username: " + registerUserRequestDto.getUsername());
        }
        UserDto currentAuthenticatedUser = getCurrentAuthenticatedUser();
        if (currentAuthenticatedUser != null) {
            Set<RoleDto> roles = currentAuthenticatedUser.getRoles();
            if (!UserUtil.hasUserManagementAuthority(roles)) {
                throw new OperationForbiddenException("User: " + currentAuthenticatedUser.getUsername() +
                        " does not have authority to create another user");
            }
        }
        User user = new User();
        user.setUsername(registerUserRequestDto.getUsername());
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(registerUserRequestDto.getPassword()));

        Optional<Role> optionalRole = roleRepository.findByType(RoleType.admin);
        Role role;
        if (optionalRole.isEmpty()) {
            role = new Role();
            role.setType(RoleType.admin);
            roleRepository.save(role);
        } else {
            role = optionalRole.get();
        }

        user.setRoles(Collections.singleton(role));

        userRepository.save(user);

        RegisterUserResponseDto registerUserResponseDto = new RegisterUserResponseDto();
        registerUserResponseDto.setUid(user.getUid());
        registerUserResponseDto.setUsername(user.getUsername());

        return registerUserResponseDto;
    }

    @Override
    public void updateUser(UUID uid, UpdateUserRequestDto updateUserRequestDto)
            throws OperationForbiddenException, ResourceNotFoundException {
        UserDto currentAuthenticatedUser = getCurrentAuthenticatedUser();
        if (currentAuthenticatedUser == null) {
            throw new OperationForbiddenException("Operation cannot be performed unauthenticated");
        }
        Optional<User> optionalUser = userRepository.findById(uid);
        if (optionalUser.isPresent()) {
            Set<RoleDto> roles = currentAuthenticatedUser.getRoles();
            User user = optionalUser.get();
            String username = user.getUsername();
            String owner = user.getCreatedBy();
            if (currentAuthenticatedUser.getUsername().equals(owner) ||
                    currentAuthenticatedUser.getUsername().equals(username) || UserUtil.hasAdminAuthority(roles)) {
                user.setPassword(updateUserRequestDto.getPassword());
                userRepository.save(user);
            } else {
                throw new OperationForbiddenException("User: " + currentAuthenticatedUser.getUsername() +
                        " does not have the authorization to update the user.");
            }
        } else {
            throw new ResourceNotFoundException("User not present with the ID: " + uid);
        }
    }

    @Override
    public void deleteUser(UUID uid) throws OperationForbiddenException, ResourceNotFoundException {
        UserDto currentAuthenticatedUser = getCurrentAuthenticatedUser();
        if (currentAuthenticatedUser == null) {
            throw new OperationForbiddenException("Operation cannot be performed unauthenticated");
        }
        Optional<User> optionalUser = userRepository.findById(uid);
        if (optionalUser.isPresent()) {
            Set<RoleDto> roles = currentAuthenticatedUser.getRoles();
            User user = optionalUser.get();
            String username = user.getUsername();
            String owner = user.getCreatedBy();
            if (currentAuthenticatedUser.getUsername().equals(owner) ||
                    currentAuthenticatedUser.getUsername().equals(username) || UserUtil.hasAdminAuthority(roles)) {
                user.setEnabled(false);
                userRepository.save(user);
            } else {
                throw new OperationForbiddenException("User: " + currentAuthenticatedUser.getUsername() +
                        " does not have the authorization to delete the user.");
            }
        } else {
            throw new ResourceNotFoundException("User not present with the ID: " + uid);
        }
    }
}
