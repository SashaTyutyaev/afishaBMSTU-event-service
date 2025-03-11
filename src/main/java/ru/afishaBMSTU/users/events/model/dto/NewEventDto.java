package ru.afishaBMSTU.users.events.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.afishaBMSTU.users.events.model.Location;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {

    @NotNull
    @NotEmpty
    @NotBlank
    @Length(min = 20, max = 2000)
    private String annotation;

    @NotNull
    private Integer category;

    @NotNull
    @NotEmpty
    @NotBlank
    @Length(min = 20, max = 7000)
    private String description;

    @NotNull
    @NotEmpty
    private String eventDate;

    @NotNull
    private Location location;

    private Boolean paid;

    @PositiveOrZero
    private Integer participantLimit;

    private Boolean requestModeration;

    @NotNull
    @NotEmpty
    @Length(min = 3, max = 120)
    private String title;
}
