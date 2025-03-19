package ru.afishaBMSTU.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class BMSTUClient {

    private final WebClient bmstuWebClient;

    public String getEvents() {
        return bmstuWebClient.get()
                .uri("https://api.www.bmstu.ru/events?&limit=6&offset=0&isActual=1")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
