package ru.afishaBMSTU.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig implements WebFluxConfigurer {

    @Value("${integration.f5-ai.key}")
    private String f5AIKey;

    @Bean
    public WebClient f5AIWebClient() {
        return WebClient.builder()
                .defaultHeader("X-Auth-Token", f5AIKey)
                .build();
    }
}
