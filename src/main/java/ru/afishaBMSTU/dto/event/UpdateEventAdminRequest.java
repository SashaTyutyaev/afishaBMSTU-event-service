package ru.afishaBMSTU.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.afishaBMSTU.model.event.Location;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventAdminRequest {

    @Length(min = 20, max = 2000)
    private String annotation;

    private Integer category;

    @Length(min = 20, max = 7000)
    private String description;

    private String eventDate;

    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private StateActionAdmin stateAction;

    @Length(min = 3, max = 120)
    private String title;

}
