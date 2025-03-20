package ru.afishaBMSTU.dto.bmstu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BMSTUEventDto {
    private List<EventItemDto> items;
}
