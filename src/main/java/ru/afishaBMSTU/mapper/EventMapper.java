package ru.afishaBMSTU.mapper;

import org.mapstruct.*;
import ru.afishaBMSTU.dto.event.EventFullDto;
import ru.afishaBMSTU.dto.event.EventShortDto;
import ru.afishaBMSTU.dto.event.NewEventDto;
import ru.afishaBMSTU.model.category.Category;
import ru.afishaBMSTU.model.event.Event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring",
        uses = {CategoryMapper.class},
        imports = {DateTimeFormatter.class, LocalDateTime.class})
public interface EventMapper {

    DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Mapping(target = "eventDate", expression = "java(FORMATTER.format(event.getEventDate()))")
    @Mapping(target = "imageDescription", source = "descriptionOfImage")
    EventShortDto toEventShortDto(Event event);

    @Mapping(target = "eventDate", expression = "java(FORMATTER.format(event.getEventDate()))")
    @Mapping(target = "createdOn", expression = "java(FORMATTER.format(event.getCreatedOn()))")
    @Mapping(target = "publishedOn", expression = "java(event.getPublishedOn() != null ? FORMATTER.format(event.getPublishedOn()) : null)")
    @Mapping(target = "state", expression = "java(event.getState().toString())")
    @Mapping(target = "imageDescription", source = "descriptionOfImage")
    EventFullDto toEventFullDto(Event event);

    @Mapping(target = "eventDate", expression = "java(LocalDateTime.parse(newEventDto.getEventDate(), FORMATTER))")
    @Mapping(target = "category", source = "category") // Указываем маппинг
    Event toEvent(NewEventDto newEventDto);

    default Category map(Long value) {
        if (value == null) {
            return null;
        }
        Category category = new Category();
        category.setId(value);
        return category;
    }
}