package ru.afishaBMSTU.security.jwt;

import afishaBMSTU.auth_lib.security.BaseAuthTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.afishaBMSTU.dto.jwt.JwtTokenDataDto;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends BaseAuthTokenFilter<JwtTokenDataDto> {

    private final JwtService jwtService;

    @Override
    protected List<String> parseRoles(JwtTokenDataDto userInfo) {
        return userInfo.getRoles();
    }

    @Override
    protected JwtTokenDataDto retrieveUserInfo(String token) {
        return jwtService.extractUserInfo(token);
    }

}
