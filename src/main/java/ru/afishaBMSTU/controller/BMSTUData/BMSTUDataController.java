package ru.afishaBMSTU.controller.BMSTUData;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.afishaBMSTU.dto.bmstu.ParamsDto;
import ru.afishaBMSTU.dto.bmstu.response.BMSTUResponseDto;
import ru.afishaBMSTU.service.bmstu.BMSTUDataService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/bmstu-data")
@RequiredArgsConstructor
@Tag(name = "Получение информации с сайта МГТУ", description = "API для получения новостей/мероприятий с сайта МГТУ")
@SecurityRequirement(name = "bearerAuth")
public class BMSTUDataController {

    @Qualifier("eventDataService")
    private final BMSTUDataService eventsDataService;

    @Qualifier("newsDataService")
    private final BMSTUDataService newsDataService;

    @Operation(summary = "Получить события с сайт МГТУ", description = "Возвращает список событий с возможностью фильтрации по параметрам.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список событий успешно получен"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса"),
            @ApiResponse(responseCode = "401", description = "Неавторизованный доступ")
    })
    @GetMapping("/events")
    public BMSTUResponseDto getEvents(@ModelAttribute ParamsDto paramsDto) {
        return eventsDataService.getData(paramsDto);
    }

    @Operation(summary = "Получить новости с сайта МГТУ", description = "Возвращает список новостей с возможностью фильтрации по параметрам.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список новостей успешно получен"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса"),
            @ApiResponse(responseCode = "401", description = "Неавторизованный доступ")
    })
    @GetMapping("/news")
    public BMSTUResponseDto getNews(@ModelAttribute ParamsDto paramsDto) {
        return newsDataService.getData(paramsDto);
    }
}
