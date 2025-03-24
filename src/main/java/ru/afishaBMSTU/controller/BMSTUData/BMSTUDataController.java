package ru.afishaBMSTU.controller.BMSTUData;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.afishaBMSTU.dto.bmstu.ParamsDto;
import ru.afishaBMSTU.dto.bmstu.response.BMSTUResponseDto;
import ru.afishaBMSTU.service.bmstu.BMSTUDataService;

@RestController
@RequestMapping("/api/users/bmstu-data")
@RequiredArgsConstructor
public class BMSTUDataController {

    @Qualifier("eventDataService")
    private final BMSTUDataService eventsDataService;

    @Qualifier("newsDataService")
    private final BMSTUDataService newsDataService;

    @GetMapping("/events")
    public BMSTUResponseDto getEvents(@ModelAttribute ParamsDto paramsDto) {
        return eventsDataService.getData(paramsDto);
    }

    @GetMapping("/news")
    public BMSTUResponseDto getNews(@ModelAttribute ParamsDto paramsDto) {
        return newsDataService.getData(paramsDto);
    }
}
