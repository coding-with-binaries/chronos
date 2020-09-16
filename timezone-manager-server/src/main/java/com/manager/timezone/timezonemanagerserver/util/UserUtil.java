package com.manager.timezone.timezonemanagerserver.util;

import com.manager.timezone.timezonemanagerserver.dto.*;
import com.manager.timezone.timezonemanagerserver.model.Role;
import com.manager.timezone.timezonemanagerserver.model.User;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserUtil {
    public static UserDto convertUserToDto(User user) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(user, UserDto.class);
    }

    public static User convertDtoToUser(UserDto userDto) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(userDto, User.class);
    }

    public static RoleDto convertRoleToDto(Role role) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(role, RoleDto.class);
    }

    public static Set<RoleDto> convertRolesToDtoSet(Set<Role> roles) {
        return roles.stream().map(UserUtil::convertRoleToDto).collect(Collectors.toSet());
    }

    public static List<UserDto> convertUsersToDtoList(List<User> users) {
        return users.stream().map(UserUtil::convertUserToDto).collect(Collectors.toList());
    }

    public static boolean hasUserManagementAuthority(Set<RoleDto> roles) {
        return roles.stream()
                .anyMatch(r -> r.getType().equals(RoleType.admin) || r.getType().equals(RoleType.user_manager));
    }

    public static boolean hasAdminAuthority(Set<RoleDto> roles) {
        return roles.stream().anyMatch(r -> r.getType().equals(RoleType.admin));
    }
}
