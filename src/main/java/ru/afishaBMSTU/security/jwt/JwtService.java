package ru.afishaBMSTU.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.afishaBMSTU.exceptions.IncorrectTokenException;

import java.security.Key;
import java.util.Date;

@Service
@Slf4j
public class JwtService {

    @Value("${security.jwt.secret}")
    private String secret;

    public String generateToken(String nickname) {
        return Jwts.builder()
                .setSubject(nickname)
                .setIssuedAt(new Date())
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUserNickname(String token) {
        if (token == null || !token.startsWith("Bearer")) {
            throw new IncorrectTokenException("Missing bearer prefix");
        }

        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token.substring(7))
                .getBody()
                .getSubject();
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}