package ru.afishaBMSTU.users.requests.model.dto;

import ru.afishaBMSTU.users.requests.model.Request;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RequestMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public static Request toRequest(ParticipationRequestDto participationRequestDto) {
        return Request.builder()
                .created(LocalDateTime.now())
                .build();
    }

    public static ParticipationRequestDto toParticipationRequestDto(Request request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .created(FORMATTER.format(request.getCreated()))
                .requester(request.getUser().getId())
                .event(request.getEvent().getId())
                .status(request.getStatus().toString())
                .build();
    }
}
