package com.manager.timezone.timezonemanagerserver.util;

import com.manager.timezone.timezonemanagerserver.dto.UserDto;
import com.manager.timezone.timezonemanagerserver.model.User;
import org.modelmapper.ModelMapper;

public class UserUtil {
    public static UserDto convertUserToDto(User user) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(user, UserDto.class);
    }

    public static User convertDtoToUser(UserDto userDto) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(userDto, User.class);
    }
}
