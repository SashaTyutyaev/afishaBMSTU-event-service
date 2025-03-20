package ru.afishaBMSTU.dto.bmstu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventItemDto extends ItemDto {
    private String location;
    private List<DateTimeDto> datetime;
}
