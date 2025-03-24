package ru.afishaBMSTU.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.afishaBMSTU.model.user.CustomUserDetails;
import ru.afishaBMSTU.security.CustomUserDetailsService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String[] EXCLUDED_PATHS = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/api/users/signup",
            "/api/auth"
    };

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        for (String path : EXCLUDED_PATHS) {
            if (pathMatcher.match(path, request.getRequestURI())) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        try {
            String authHeader = request.getHeader("Authorization");

            String nickname = jwtService.extractUserNickname(authHeader);
            CustomUserDetails userDetails = userDetailsService.loadUserByUsername(nickname);

            List<String> roles = parseRoles(userDetails);
            List<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, authorities);
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
            log.info("User from jwt : {}", userDetails.toString());
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private List<String> parseRoles(CustomUserDetails userInfo) {
        if (userInfo != null) {
            return userInfo.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();
        }
        return Collections.emptyList();
    }
}