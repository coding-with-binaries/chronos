package com.manager.timezone.timezonemanagerserver.util;

import com.manager.timezone.timezonemanagerserver.dto.RoleDto;
import com.manager.timezone.timezonemanagerserver.dto.RoleType;
import com.manager.timezone.timezonemanagerserver.dto.UserDto;
import com.manager.timezone.timezonemanagerserver.model.Role;
import com.manager.timezone.timezonemanagerserver.model.User;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class UserUtil {
    public static UserDto convertUserToDto(User user) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(user, UserDto.class);
    }

    public static List<UserDto> convertUsersToDtoList(List<User> users) {
        return users.stream().map(UserUtil::convertUserToDto).collect(Collectors.toList());
    }

    public static boolean hasUserManagementAuthority(RoleDto role) {
        return role.getType().equals(RoleType.admin) || role.getType().equals(RoleType.user_manager);
    }

    public static boolean hasAdminAuthority(RoleDto role) {
        return role.getType().equals(RoleType.admin);
    }

    public static boolean hasAdminAuthority(Role role) {
        return role.getType().equals(RoleType.admin);
    }
}
