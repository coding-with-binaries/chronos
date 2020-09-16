package com.manager.timezone.timezonemanagerserver.service.impl;

import com.manager.timezone.timezonemanagerserver.auth.AuthUserDetails;
import com.manager.timezone.timezonemanagerserver.auth.JwtTokenProvider;
import com.manager.timezone.timezonemanagerserver.dto.*;
import com.manager.timezone.timezonemanagerserver.exception.InvalidResourceException;
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
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

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
    public WhoAmIDto whoAmI() {
        UserDto currentAuthenticatedUser = getCurrentAuthenticatedUser();
        if (currentAuthenticatedUser == null) {
            return null;
        }
        WhoAmIDto whoAmI = new WhoAmIDto();
        whoAmI.setUid(currentAuthenticatedUser.getUid());
        whoAmI.setUsername(currentAuthenticatedUser.getUsername());
        Set<RoleType> roles =
                currentAuthenticatedUser.getRoles().stream().map(RoleDto::getType).collect(Collectors.toSet());
        whoAmI.setRoles(roles);
        return whoAmI;
    }

    @Override
    public AuthenticateUserResponseDto authenticateUser(AuthenticateUserRequestDto authenticateUserRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticateUserRequestDto.getUsername(),
                        authenticateUserRequestDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        UserDto currentAuthenticatedUser = getCurrentAuthenticatedUser();
        Set<RoleType> roles =
                currentAuthenticatedUser.getRoles().stream().map(RoleDto::getType).collect(Collectors.toSet());
        return new AuthenticateUserResponseDto(jwt, currentAuthenticatedUser.getUid(),
                currentAuthenticatedUser.getUsername(), roles);

    }

    @Override
    public UserDto registerUser(RegisterUserRequestDto registerUserRequestDto)
            throws UserExistsException, OperationForbiddenException, InvalidResourceException {
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
        user.setPassword(passwordEncoder.encode(registerUserRequestDto.getPassword()));

        if (ObjectUtils.isEmpty(registerUserRequestDto.getRoles())) {
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
        } else {
            if (registerUserRequestDto.getRoles().size() > 1) {
                throw new InvalidResourceException("User cannot have multiple roles currently.");
            }
            Set<Role> roles = new HashSet<>();
            for (RoleType roleType : registerUserRequestDto.getRoles()) {
                Optional<Role> optionalRole = roleRepository.findByType(roleType);
                Role role;
                if (optionalRole.isEmpty()) {
                    role = new Role();
                    role.setType(roleType);
                    roleRepository.save(role);
                } else {
                    role = optionalRole.get();
                }
                roles.add(role);
            }
            user.setRoles(roles);
        }

        userRepository.saveAndFlush(user);

        UserDto userDto = UserUtil.convertUserToDto(user);
        String createdBy = currentAuthenticatedUser == null ? "system" : currentAuthenticatedUser.getUsername();
        userDto.setCreatedBy(createdBy);
        return userDto;
    }

    @Override
    public List<UserDto> getAllUsers() throws OperationForbiddenException {
        UserDto currentAuthenticatedUser = getCurrentAuthenticatedUser();
        if (currentAuthenticatedUser == null) {
            throw new OperationForbiddenException("User must be authenticated to view all users.");
        }
        if (UserUtil.hasAdminAuthority(currentAuthenticatedUser.getRoles())) {
            List<User> users = userRepository.findAllByIsDeleted(false);
            return UserUtil.convertUsersToDtoList(users);
        }
        if (UserUtil.hasUserManagementAuthority(currentAuthenticatedUser.getRoles())) {
            List<User> users = userRepository.findAllByIsDeleted(false);
            return UserUtil.convertUsersToDtoList(users.stream()
                    .filter(user -> !UserUtil.hasAdminAuthority(UserUtil.convertRolesToDtoSet(user.getRoles())))
                    .collect(Collectors.toList()));
        }
        throw new OperationForbiddenException("User: " + currentAuthenticatedUser.getUsername() +
                " does not have authority to view all users");
    }

    @Override
    public UserDto getUser(UUID uid) throws OperationForbiddenException, ResourceNotFoundException {
        UserDto currentAuthenticatedUser = getCurrentAuthenticatedUser();
        if (currentAuthenticatedUser == null) {
            throw new OperationForbiddenException("Operation cannot be performed unauthenticated");
        }
        Optional<User> optionalUser = userRepository.findByUidAndIsDeleted(uid, false);
        if (optionalUser.isPresent()) {
            Set<RoleDto> roles = currentAuthenticatedUser.getRoles();
            User user = optionalUser.get();
            String username = user.getUsername();
            String owner = user.getCreatedBy();
            if (currentAuthenticatedUser.getUsername().equals(owner) ||
                    currentAuthenticatedUser.getUsername().equals(username) || UserUtil.hasAdminAuthority(roles)) {
                return UserUtil.convertUserToDto(user);
            }
        }
        throw new ResourceNotFoundException("User not present with the ID: " + uid);
    }

    @Override
    public UserDto updateUser(UUID uid, UpdateUserRequestDto updateUserRequestDto)
            throws OperationForbiddenException, ResourceNotFoundException, InvalidResourceException {
        UserDto currentAuthenticatedUser = getCurrentAuthenticatedUser();
        if (currentAuthenticatedUser == null) {
            throw new OperationForbiddenException("Operation cannot be performed unauthenticated");
        }
        Optional<User> optionalUser = userRepository.findById(uid);
        if (optionalUser.isPresent()) {
            Set<RoleDto> roles = currentAuthenticatedUser.getRoles();
            User user = optionalUser.get();
            String username = user.getUsername();
            if (currentAuthenticatedUser.getUsername().equals(username) || UserUtil.hasUserManagementAuthority(roles)) {
                if (!ObjectUtils.isEmpty(updateUserRequestDto.getRoles())) {
                    if (updateUserRequestDto.getRoles().size() > 1) {
                        throw new InvalidResourceException("User cannot have multiple roles currently.");
                    }
                    Set<Role> userRoles = new HashSet<>();
                    for (RoleType roleType : updateUserRequestDto.getRoles()) {
                        Optional<Role> optionalRole = roleRepository.findByType(roleType);
                        Role role;
                        if (optionalRole.isEmpty()) {
                            role = new Role();
                            role.setType(RoleType.user);
                            roleRepository.save(role);
                        } else {
                            role = optionalRole.get();
                        }
                        userRoles.add(role);
                    }
                    user.setRoles(userRoles);
                }
                return UserUtil.convertUserToDto(userRepository.save(user));
            } else {
                throw new OperationForbiddenException("User: " + currentAuthenticatedUser.getUsername() +
                        " does not have the authorization to update the user.");
            }
        } else {
            throw new ResourceNotFoundException("User not present with the ID: " + uid);
        }
    }

    @Override
    public void updateUserPassword(UUID uid, UpdatePasswordDto updatePasswordDto)
            throws OperationForbiddenException, ResourceNotFoundException {
        UserDto currentAuthenticatedUser = getCurrentAuthenticatedUser();
        if (currentAuthenticatedUser == null) {
            throw new OperationForbiddenException("Operation cannot be performed unauthenticated");
        }
        Optional<User> optionalUser = userRepository.findById(uid);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String currentPassword = updatePasswordDto.getCurrentPassword();
            if (passwordEncoder.matches(currentPassword, user.getPassword())) {
                user.setPassword(passwordEncoder.encode(updatePasswordDto.getNewPassword()));
                userRepository.save(user);
            } else {
                throw new OperationForbiddenException("Current password does not match!");
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
            if (currentAuthenticatedUser.getUsername().equals(username) || UserUtil.hasUserManagementAuthority(roles)) {
                user.setDeleted(true);
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
