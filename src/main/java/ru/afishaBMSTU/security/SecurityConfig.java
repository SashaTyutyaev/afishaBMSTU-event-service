package ru.afishaBMSTU.security;

import afishaBMSTU.auth_lib.security.BaseAuthTokenFilter;
import afishaBMSTU.auth_lib.security.BaseSecurityConfig;
import afishaBMSTU.auth_lib.security.internal.InternalTokenFilter;
import afishaBMSTU.auth_lib.security.internal.InternalTokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.afishaBMSTU.dto.jwt.JwtTokenDataDto;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig extends BaseSecurityConfig {

    public SecurityConfig(BaseAuthTokenFilter<JwtTokenDataDto> authTokenFilter) {
        super(authTokenFilter);
    }

    @Bean
    public InternalTokenService internalTokenService() {
        return new InternalTokenService();
    }

    @Bean
    public InternalTokenFilter internalTokenFilter() {
        return new InternalTokenFilter(internalTokenService());
    }

    @Override
    protected void configureHttpSecurity(HttpSecurity http) throws Exception {
        http.addFilterBefore(internalTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/creator/**").hasAnyRole("ADMIN", "CREATOR")
                        .requestMatchers("/api/**").hasAnyRole("ADMIN", "CREATOR", "USER")
                        .requestMatchers("/actuator/**").permitAll()
                );
    }
}
