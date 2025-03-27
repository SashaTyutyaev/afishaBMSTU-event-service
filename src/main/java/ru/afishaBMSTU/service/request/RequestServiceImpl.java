package ru.afishaBMSTU.service.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.afishaBMSTU.dto.request.ParticipationRequestDto;
import ru.afishaBMSTU.exceptions.IncorrectParameterException;
import ru.afishaBMSTU.exceptions.NotFoundException;
import ru.afishaBMSTU.mapper.RequestMapper;
import ru.afishaBMSTU.model.event.Event;
import ru.afishaBMSTU.model.event.State;
import ru.afishaBMSTU.model.request.Request;
import ru.afishaBMSTU.model.request.RequestStatus;
import ru.afishaBMSTU.repository.EventRepository;
import ru.afishaBMSTU.repository.RequestRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final RequestMapper requestMapper;

    @Override
    @Transactional
    public ParticipationRequestDto createRequest(UUID externalId, Long eventId) {
        Event event = getEventById(eventId);
        if (Objects.equals(event.getInitiatorExternalId(), externalId)) {
            log.error("Initiator cant sent request to his own event");
            throw new DataIntegrityViolationException("Initiator cant sent request to his own event");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            log.error("Event is not published");
            throw new DataIntegrityViolationException("Event is not published");
        }
        if (event.getParticipantLimit() != 0) {
            if (event.getParticipantLimit() <= event.getConfirmedRequests()) {
                log.error("Participant limit exceeded");
                throw new DataIntegrityViolationException("Participant limit exceeded");
            }
        }
        if (requestRepository.findByRequesterAndEvent(externalId, eventId).isPresent()) {
            log.error("Request already exists");
            throw new DataIntegrityViolationException("Request already exists");
        }
        Request request;
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request = Request.builder()
                    .status(RequestStatus.CONFIRMED)
                    .created(LocalDateTime.now())
                    .event(event)
                    .userExternalId(externalId)
                    .build();
            requestRepository.save(request);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        } else {
            request = Request.builder()
                    .status(RequestStatus.PENDING)
                    .created(LocalDateTime.now())
                    .event(event)
                    .userExternalId(externalId)
                    .build();
            requestRepository.save(request);
        }
        log.info("Request created successful");
        return requestMapper.toParticipationRequestDto(request);
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelRequest(UUID externalId, Long requestId) {
        Request request = getRequestByInitiatorAndId(externalId, requestId);
        if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
            Event event = getEventById(request.getEvent().getId());
            event.setConfirmedRequests(event.getConfirmedRequests() - 1);
            eventRepository.save(event);
        }
        request.setStatus(RequestStatus.CANCELED);
        log.info("Request cancelled successful");
        return requestMapper.toParticipationRequestDto(request);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getRequests(UUID externalId) {
        List<Request> requests = requestRepository.findAllByUserExternalId(externalId);
        log.info("Get requests from user {}", externalId);
        return requests.stream().map(requestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    private Event getEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> {
            log.error("Event with id {} not found", eventId);
            return new NotFoundException("Event with id " + eventId + " not found");
        });
    }

    private Request getRequestByInitiatorAndId(UUID externalId, Long requestId) {
        return requestRepository.findByIdAndUser(externalId, requestId).orElseThrow(() -> {
            log.error("User {} is not owner of the request {}", externalId, requestId);
            return new IncorrectParameterException("User is not owner of the request");
        });
    }
}
