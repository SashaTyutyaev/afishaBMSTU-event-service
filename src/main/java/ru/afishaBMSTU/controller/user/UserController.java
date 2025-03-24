package ru.afishaBMSTU.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.afishaBMSTU.dto.user.UserFullDto;
import ru.afishaBMSTU.dto.user.UserRegisterRequest;
import ru.afishaBMSTU.model.user.CustomUserDetails;
import ru.afishaBMSTU.service.user.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public UserFullDto register(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
        return userService.registerUser(userRegisterRequest);
    }

    @GetMapping
    public UserFullDto getUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return userService.getUser(userDetails.getId());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.deleteUser(userDetails.getId());
    }
}
