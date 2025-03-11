package ru.afishaBMSTU.admin.users.model;

import ru.afishaBMSTU.admin.users.model.dto.UserDto;
import ru.afishaBMSTU.admin.users.model.dto.UserShortDto;

public class UserMapper {

    public static User toUser(UserDto userDto) {
        return User.builder()
                .email(userDto.getEmail())
                .name(userDto.getName())
                .build();
    }

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public static UserShortDto toUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public static UserShortDto toUserShortDto(UserDto userDto) {
        return UserShortDto.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .build();
    }


}
