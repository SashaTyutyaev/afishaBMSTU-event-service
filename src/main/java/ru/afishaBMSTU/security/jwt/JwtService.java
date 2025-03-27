package ru.afishaBMSTU.security.jwt;


import afishaBMSTU.auth_lib.security.BaseJwtService;
import org.springframework.stereotype.Service;
import ru.afishaBMSTU.dto.jwt.JwtTokenDataDto;

@Service
public class JwtService extends BaseJwtService<JwtTokenDataDto> {

    @Override
    protected Class<JwtTokenDataDto> getDataType() {
        return JwtTokenDataDto.class;
    }

}
