package com.manager.timezone.timezonemanagerserver.service.impl;

import com.manager.timezone.timezonemanagerserver.auth.AuthUserDetails;
import com.manager.timezone.timezonemanagerserver.auth.CurrentUser;
import com.manager.timezone.timezonemanagerserver.auth.JwtTokenProvider;
import com.manager.timezone.timezonemanagerserver.dto.*;
import com.manager.timezone.timezonemanagerserver.exception.OperationForbiddenException;
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
        AuthenticateUserResponseDto authenticateUserResponseDto = new AuthenticateUserResponseDto(jwt);
        return authenticateUserResponseDto;

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
            if (!roles.contains(RoleType.user_manager) && !roles.contains(RoleType.admin)) {
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
            roleRepository.saveAndFlush(role);
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
}
