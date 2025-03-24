package ru.afishaBMSTU.controller.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.afishaBMSTU.dto.user.UserLoginDto;
import ru.afishaBMSTU.service.auth.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public String login(UserLoginDto userLoginDto) {
        return authService.login(userLoginDto);
    }
}
