package ru.afishaBMSTU.users.events.model.dto;

import ru.afishaBMSTU.admin.categories.model.dto.CategoryMapper;
import ru.afishaBMSTU.admin.users.model.UserMapper;
import ru.afishaBMSTU.users.events.model.Event;
import ru.afishaBMSTU.users.events.model.Location;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(FORMATTER.format(event.getEventDate()))
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(FORMATTER.format(event.getCreatedOn()))
                .description(event.getDescription())
                .eventDate(FORMATTER.format(event.getEventDate()))
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .location(buildLocation(event.getLat(), event.getLon()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn() != null ? FORMATTER.format(event.getPublishedOn()) : null)
                .requestModeration(event.getRequestModeration())
                .state(event.getState().toString())
                .build();
    }

    public static Event toEvent(NewEventDto newEventDto) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .description(newEventDto.getDescription())
                .eventDate(LocalDateTime.parse(newEventDto.getEventDate(), FORMATTER))
                .lat(newEventDto.getLocation().getLat())
                .lon(newEventDto.getLocation().getLon())
                .paid(newEventDto.getPaid())
                .title(newEventDto.getTitle())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .build();
    }

    private static Location buildLocation(Float lat, Float lon) {
        return Location.builder()
                .lat(lat)
                .lon(lon)
                .build();
    }
}
