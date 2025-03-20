package ru.afishaBMSTU.service.bmstu;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.afishaBMSTU.client.BMSTUClient;
import ru.afishaBMSTU.dto.bmstu.BMSTUEventDto;
import ru.afishaBMSTU.dto.bmstu.EventItemDto;
import ru.afishaBMSTU.dto.bmstu.ParamsDto;
import ru.afishaBMSTU.dto.bmstu.response.BMSTUEventsResponseDto;
import ru.afishaBMSTU.dto.bmstu.response.BMSTUResponseDto;

import java.util.List;

@Service("eventDataService")
@RequiredArgsConstructor
@Slf4j
public class BMSTUEventsService implements BMSTUDataService {

    @Value("${integration.bmstu.host}")
    private String bmstuHost;

    private final BMSTUClient bmstuClient;

    @Override
    public BMSTUResponseDto getData(ParamsDto paramsDto) {
        log.info("Getting events from BMSTU...");

        BMSTUEventDto bmstuEventDto = bmstuClient.getEvents(paramsDto.getIsActual(),
                paramsDto.getLimit(), paramsDto.getOffset());

        List<EventItemDto> items = bmstuEventDto.getItems().stream()
                .peek(eventItemDto ->
                        eventItemDto.setPageUrl(bmstuHost + eventItemDto.getPageUrl()))
                .toList();

        bmstuEventDto.setItems(items);

        return new BMSTUEventsResponseDto("Анонсы МГТУ им. Н.Э.Баумана", bmstuEventDto);
    }
}
