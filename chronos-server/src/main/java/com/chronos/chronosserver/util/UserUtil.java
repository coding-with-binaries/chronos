package com.chronos.chronosserver.util;

import com.chronos.chronosserver.dto.RoleDto;
import com.chronos.chronosserver.dto.RoleType;
import com.chronos.chronosserver.dto.UserDto;
import com.chronos.chronosserver.model.Role;
import com.chronos.chronosserver.model.User;
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
