package ru.afishaBMSTU.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.afishaBMSTU.dto.user.UserLoginDto;
import ru.afishaBMSTU.security.jwt.JwtService;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public String login(UserLoginDto userLoginDto) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginDto.getNickname(),
                userLoginDto.getPassword()));

        String token = jwtService.generateToken(userLoginDto.getNickname());
        log.info("Successfully authenticated and created token");
        return token;
    }


}
