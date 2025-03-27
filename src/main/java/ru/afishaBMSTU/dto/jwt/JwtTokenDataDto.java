package ru.afishaBMSTU.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtTokenDataDto {
    private UUID externalId;
    private List<String> roles;
}
