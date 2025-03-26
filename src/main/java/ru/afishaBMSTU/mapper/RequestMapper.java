package ru.afishaBMSTU.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.afishaBMSTU.dto.request.ParticipationRequestDto;
import ru.afishaBMSTU.model.request.Request;

import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface RequestMapper {


    DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Mapping(target = "created", expression = "java(FORMATTER.format(request.getCreated()))")
    @Mapping(target = "requester", source = "userExternalId")
    @Mapping(target = "event", source = "event.id")
    @Mapping(target = "status", expression = "java(request.getStatus().toString())")
    ParticipationRequestDto toParticipationRequestDto(Request request);
}