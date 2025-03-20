package ru.afishaBMSTU.dto.bmstu.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.afishaBMSTU.dto.bmstu.BMSTUNewsDto;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BMSTUNewsResponseDto extends BMSTUResponseDto {
    private BMSTUNewsDto data;

    public BMSTUNewsResponseDto(String title, BMSTUNewsDto data) {
        super(title);
        this.data = data;
    }
}
