package ru.afishaBMSTU.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.afishaBMSTU.dto.bmstu.BMSTUEventDto;
import ru.afishaBMSTU.dto.bmstu.BMSTUNewsDto;

@Component
@RequiredArgsConstructor
public class BMSTUClient {

    @Value("${integration.bmstu.api-url}")
    private String bmstuApiUrl;

    @Value("${integration.bmstu.events-url}")
    private String eventsApiUrl;

    @Value("${integration.bmstu.news-url}")
    private String newsApiUrl;

    private final WebClient bmstuWebClient;

    public BMSTUEventDto getEvents(Integer isActual, Integer limit, Integer offset) {
        return bmstuWebClient.get()
                .uri(createBMSTUUrl(isActual, limit, offset, eventsApiUrl))
                .retrieve()
                .bodyToMono(BMSTUEventDto.class)
                .block();
    }

    public BMSTUNewsDto getNews(Integer isActual, Integer limit, Integer offset) {
        return bmstuWebClient.get()
                .uri(createBMSTUUrl(isActual, limit, offset, newsApiUrl))
                .retrieve()
                .bodyToMono(BMSTUNewsDto.class)
                .block();
    }

    private String createBMSTUUrl(Integer isActual, Integer limit, Integer offset, String urlType) {
        return bmstuApiUrl + urlType + "?limit=" + limit + "&offset=" + offset + "&isActual=" + isActual;
    }

}
