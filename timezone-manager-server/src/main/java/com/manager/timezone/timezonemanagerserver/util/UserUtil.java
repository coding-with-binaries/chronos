package com.manager.timezone.timezonemanagerserver.util;

import com.manager.timezone.timezonemanagerserver.dto.RoleDto;
import com.manager.timezone.timezonemanagerserver.dto.RoleType;
import com.manager.timezone.timezonemanagerserver.dto.UserDto;
import com.manager.timezone.timezonemanagerserver.model.Role;
import com.manager.timezone.timezonemanagerserver.model.User;
import org.modelmapper.ModelMapper;

import java.util.Set;

public class UserUtil {
    public static UserDto convertUserToDto(User user) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(user, UserDto.class);
    }

    public static User convertDtoToUser(UserDto userDto) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(userDto, User.class);
    }

    public static boolean hasUserManagementAuthority(Set<RoleDto> roles) {
        return roles.stream()
                .anyMatch(r -> r.getType().equals(RoleType.admin) || r.getType().equals(RoleType.user_manager));
    }

    public static boolean hasAdminAuthority(Set<RoleDto> roles) {
        return roles.stream().anyMatch(r -> r.getType().equals(RoleType.admin));
    }
}
