package ru.practicum.ewmmainservice.user.model.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.ewmmainservice.user.model.User;

@UtilityClass
public class UserMapper {
    public static UserShortDto toUserShortDto(User userStorage) {
        return UserShortDto.builder()
                .id(userStorage.getId())
                .name(userStorage.getName())
                .build();
    }

    public static UserDto toUserDto(User userStorage) {
        return UserDto.builder()
                .id(userStorage.getId())
                .name(userStorage.getName())
                .email(userStorage.getEmail())
                .build();
    }

    public static User toUser(UserDto userDto) {
        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }
}
