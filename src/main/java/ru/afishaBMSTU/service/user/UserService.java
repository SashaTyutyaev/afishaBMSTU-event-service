package ru.afishaBMSTU.service.user;

import ru.afishaBMSTU.dto.user.UserDto;

import java.util.List;

public interface UserService {
    UserDto addUser(UserDto userDto);
    List<UserDto> getAllUsers(List<Long> ids, Integer from, Integer size);
    void deleteUser(Long id);
}
