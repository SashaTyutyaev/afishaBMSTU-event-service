package ru.afishaBMSTU.service.bmstu;

import ru.afishaBMSTU.dto.bmstu.ParamsDto;
import ru.afishaBMSTU.dto.bmstu.response.BMSTUResponseDto;

public interface BMSTUDataService {
    BMSTUResponseDto getData(ParamsDto paramsDto);
}
