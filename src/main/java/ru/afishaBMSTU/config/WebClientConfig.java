package ru.afishaBMSTU.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig implements WebFluxConfigurer {

    @Bean
    public WebClient f5AIWebClient() {
        return WebClient.builder()
                .build();
    }

    @Bean
    public WebClient bmstuWebClient() {
        return WebClient.builder()
                .build();
    }
}
