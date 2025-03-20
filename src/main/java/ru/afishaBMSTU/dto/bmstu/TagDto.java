package ru.afishaBMSTU.dto.bmstu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDto {
    private Long id;
    private String slug;
    private String title;
    private String color;
}
