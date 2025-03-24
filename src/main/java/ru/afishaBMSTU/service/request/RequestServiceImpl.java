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
import ru.afishaBMSTU.model.user.User;
import ru.afishaBMSTU.repository.EventRepository;
import ru.afishaBMSTU.repository.RequestRepository;
import ru.afishaBMSTU.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        User user = getUserById(userId);
        Event event = getEventById(eventId);
        if (Objects.equals(event.getInitiator().getId(), userId)) {
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
        if (requestRepository.findByRequesterAndEvent(userId, eventId).isPresent()) {
            log.error("Request already exists");
            throw new DataIntegrityViolationException("Request already exists");
        }
        Request request;
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request = Request.builder()
                    .status(RequestStatus.CONFIRMED)
                    .created(LocalDateTime.now())
                    .event(event)
                    .user(user)
                    .build();
            requestRepository.save(request);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        } else {
            request = Request.builder()
                    .status(RequestStatus.PENDING)
                    .created(LocalDateTime.now())
                    .event(event)
                    .user(user)
                    .build();
            requestRepository.save(request);
        }
        log.info("Request created successful");
        return RequestMapper.toParticipationRequestDto(request);
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        Request request = getRequestByInitiatorAndId(userId, requestId);
        if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
            Event event = getEventById(request.getEvent().getId());
            event.setConfirmedRequests(event.getConfirmedRequests() - 1);
            eventRepository.save(event);
        }
        request.setStatus(RequestStatus.CANCELED);
        log.info("Request cancelled successful");
        return RequestMapper.toParticipationRequestDto(request);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getRequests(Long userId) {
        User user = getUserById(userId);
        List<Request> requests = requestRepository.findAllByUser(user);
        log.info("Get requests from user {}", userId);
        return requests.stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    private Event getEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> {
            log.error("Event with id {} not found", eventId);
            return new NotFoundException("Event with id " + eventId + " not found");
        });
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with id {} not found", userId);
            return new NotFoundException("User with id " + userId + " not found");
        });
    }

    private Request getRequestByInitiatorAndId(Long userId, Long requestId) {
        return requestRepository.findByIdAndUser(userId, requestId).orElseThrow(() -> {
            log.error("User {} is not owner of the request {}", userId, requestId);
            return new IncorrectParameterException("User is not owner of the request");
        });
    }
}
