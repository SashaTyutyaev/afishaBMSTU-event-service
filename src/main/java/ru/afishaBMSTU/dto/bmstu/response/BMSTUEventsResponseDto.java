package ru.afishaBMSTU.dto.bmstu.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.afishaBMSTU.dto.bmstu.BMSTUEventDto;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BMSTUEventsResponseDto extends BMSTUResponseDto {
    private BMSTUEventDto data;

    public BMSTUEventsResponseDto(String title, BMSTUEventDto data) {
        super(title);
        this.data = data;
    }
}
