package ru.afishaBMSTU.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCategoryDto {

    @NotNull
    @NotBlank
    @NotEmpty
    @Size(min = 1, max = 50)
    private String name;
}
