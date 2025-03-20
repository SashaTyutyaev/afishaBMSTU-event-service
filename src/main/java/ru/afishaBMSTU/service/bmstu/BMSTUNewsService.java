package ru.afishaBMSTU.service.bmstu;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.afishaBMSTU.client.BMSTUClient;
import ru.afishaBMSTU.dto.bmstu.BMSTUNewsDto;
import ru.afishaBMSTU.dto.bmstu.NewsItemDto;
import ru.afishaBMSTU.dto.bmstu.ParamsDto;
import ru.afishaBMSTU.dto.bmstu.response.BMSTUNewsResponseDto;
import ru.afishaBMSTU.dto.bmstu.response.BMSTUResponseDto;

import java.util.List;

@Service("newsDataService")
@RequiredArgsConstructor
@Slf4j
public class BMSTUNewsService implements BMSTUDataService {

    @Value("${integration.bmstu.host}")
    private String bmstuHost;

    private final BMSTUClient bmstuClient;

    @Override
    public BMSTUResponseDto getData(ParamsDto paramsDto) {
        log.info("Getting news from BMSTU...");

        BMSTUNewsDto bmstuNewsDto = bmstuClient.getNews(paramsDto.getIsActual(),
                paramsDto.getLimit(), paramsDto.getOffset());

        List<NewsItemDto> items = bmstuNewsDto.getItems().stream()
                .peek(newsItemDto ->
                        newsItemDto.setPageUrl(bmstuHost + newsItemDto.getPageUrl()))
                .toList();

        bmstuNewsDto.setItems(items);

        return new BMSTUNewsResponseDto("Новости МГТУ им. Н.Э.Баумана", bmstuNewsDto);
    }
}
