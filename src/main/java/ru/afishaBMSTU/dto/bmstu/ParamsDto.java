package ru.afishaBMSTU.dto.bmstu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParamsDto {
    private Integer isActual;
    private Integer limit;
    private Integer offset;
}
