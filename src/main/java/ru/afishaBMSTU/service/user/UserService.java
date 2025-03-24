package ru.afishaBMSTU.service.user;

import ru.afishaBMSTU.dto.user.UserFullDto;
import ru.afishaBMSTU.dto.user.UserRegisterRequest;
import ru.afishaBMSTU.dto.user.UserShortDto;

import java.util.List;

public interface UserService {
    UserFullDto registerUser(UserRegisterRequest userRegisterRequest);

    UserFullDto getUser(Long userId);

    void deleteUser(Long id);
}
