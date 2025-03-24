package ru.afishaBMSTU.service.auth;

import ru.afishaBMSTU.dto.user.UserLoginDto;

public interface AuthService {
    String login(UserLoginDto userLoginDto);
}
