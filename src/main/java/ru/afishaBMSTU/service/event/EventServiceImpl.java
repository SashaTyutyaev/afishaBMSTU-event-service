package ru.afishaBMSTU.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.afishaBMSTU.client.F5AIClient;
import ru.afishaBMSTU.dto.event.EventFullDto;
import ru.afishaBMSTU.dto.event.EventShortDto;
import ru.afishaBMSTU.dto.event.NewEventDto;
import ru.afishaBMSTU.dto.event.StateAction;
import ru.afishaBMSTU.dto.event.UpdateEventUserRequest;
import ru.afishaBMSTU.dto.request.EventRequestStatusUpdateRequest;
import ru.afishaBMSTU.dto.request.EventRequestStatusUpdateResult;
import ru.afishaBMSTU.dto.request.ParticipationRequestDto;
import ru.afishaBMSTU.exceptions.IncorrectParameterException;
import ru.afishaBMSTU.exceptions.NotFoundException;
import ru.afishaBMSTU.mapper.EventMapper;
import ru.afishaBMSTU.mapper.RequestMapper;
import ru.afishaBMSTU.model.category.Category;
import ru.afishaBMSTU.model.event.Event;
import ru.afishaBMSTU.model.event.State;
import ru.afishaBMSTU.model.request.Request;
import ru.afishaBMSTU.model.request.RequestStatus;
import ru.afishaBMSTU.model.user.User;
import ru.afishaBMSTU.repository.CategoryRepository;
import ru.afishaBMSTU.repository.EventRepository;
import ru.afishaBMSTU.repository.RequestRepository;
import ru.afishaBMSTU.repository.UserRepository;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    @Value("${integration.s3.bucket-name}")
    private String bucketName;

    @Value("${integration.s3.s3-endpoint}")
    private String s3Endpoint;

    @Value("${integration.s3.package-name}")
    private String s3PackageName;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final S3Client s3Client;
    private final F5AIClient f5AIClient;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;

    @Override
    @Transactional
    public EventFullDto createEvent(NewEventDto newEventDto, Long userId) {
        validateEventDate(newEventDto.getEventDate());
        if (newEventDto.getPaid() == null) {
            newEventDto.setPaid(false);
        }
        if (newEventDto.getRequestModeration() == null) {
            newEventDto.setRequestModeration(true);
        }
        if (newEventDto.getParticipantLimit() == null) {
            newEventDto.setParticipantLimit(0);
        }
        Event event = EventMapper.toEvent(newEventDto);
        User user = getUserById(userId);
        event.setInitiator(user);
        Category category = getCategoryById(newEventDto.getCategory());
        event.setCategory(category);
        event.setState(State.PENDING);
        event.setCreatedOn(LocalDateTime.now());
        event.setConfirmedRequests(0);
        event.setViews(0);
        log.info("Successfully created new event: {}", event);
        Event savedEvent = eventRepository.save(event);
        return EventMapper.toEventFullDto(savedEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getAllEventsByUserId(Long userId, Integer from, Integer size) {
        getUserById(userId);
        Pageable pageable = validatePageable(from, size);
        List<Event> events = eventRepository.findAllByInitiatorId(userId, pageable);
        log.info("Successfully retrieved event by userId: {}", userId);
        return events.stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getEventByUserIdAndEventId(Long userId, Long eventId) {
        getUserById(userId);
        getEventById(eventId);
        Event event = getEventByInitiatorAndEventId(userId, eventId);
        log.info("Successfully retrieved event: {}", event);
        return EventMapper.toEventFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Long eventId, UpdateEventUserRequest updatedEvent, Long userId) {
        getUserById(userId);
        Event event = getEventById(eventId);
        if (Objects.equals(event.getInitiator().getId(), userId)) {
            if (event.getState().equals(State.PUBLISHED)) {
                log.error("Event {} already published", eventId);
                throw new DataIntegrityViolationException("Event already published");
            }
            if (updatedEvent.getAnnotation() != null) {
                event.setAnnotation(updatedEvent.getAnnotation());
            }
            if (updatedEvent.getCategory() != null) {
                event.setCategory(getCategoryById(updatedEvent.getCategory()));
            }
            if (updatedEvent.getDescription() != null) {
                event.setDescription(updatedEvent.getDescription());
            }
            if (updatedEvent.getEventDate() != null) {
                if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                    log.error("Event starting soon, too late do changes");
                    throw new IncorrectParameterException("Event starting soon, too late do changes");
                }
                if (event.getEventDate().isBefore(LocalDateTime.now())) {
                    log.error("Event is finished");
                    throw new IncorrectParameterException("Event is finished");
                }
                LocalDateTime updateEventDate = LocalDateTime.parse(updatedEvent.getEventDate(), FORMATTER);
                if (updateEventDate.isBefore(LocalDateTime.now()) ||
                        updateEventDate.isBefore(LocalDateTime.now().plusHours(2))) {
                    log.error("Cant set event date which is before now or before now + 2 hours");
                    throw new IncorrectParameterException("Cant set event date which is before now + 2 hours");
                }
                event.setEventDate(updateEventDate);
            }
            if (updatedEvent.getLocation() != null) {
                event.setLat(updatedEvent.getLocation().getLat());
                event.setLon(updatedEvent.getLocation().getLon());
            }
            if (updatedEvent.getPaid() != null) {
                event.setPaid(updatedEvent.getPaid());
            }
            if (updatedEvent.getParticipantLimit() != null) {
                event.setParticipantLimit(updatedEvent.getParticipantLimit());
            }
            if (updatedEvent.getRequestModeration() != null) {
                event.setRequestModeration(updatedEvent.getRequestModeration());
            }
            if (updatedEvent.getStateAction() != null) {
                if (updatedEvent.getStateAction().equals(StateAction.CANCEL_REVIEW)) {
                    event.setState(State.CANCELED);
                } else {
                    event.setState(State.PENDING);
                }
            }
            if (updatedEvent.getTitle() != null) {
                event.setTitle(updatedEvent.getTitle());
            }
            return EventMapper.toEventFullDto(eventRepository.save(event));
        } else {
            log.error("User is not owner of event {}", eventId);
            throw new IncorrectParameterException("User is not owner of event " + eventId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getRequests(Long userId, Long eventId) {
        getUserById(userId);
        Event event = getEventById(eventId);
        List<Request> requests = requestRepository.findAllByEvent(event);
        return requests.stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId,
                                                              EventRequestStatusUpdateRequest updateRequest) {
        getUserById(userId);
        Event event = getEventById(eventId);
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        List<ParticipationRequestDto> confirmedReqs = new ArrayList<>();
        List<ParticipationRequestDto> canceledReqs = new ArrayList<>();
        for (Long id : updateRequest.getRequestIds()) {
            Request request = getRequestById(id);
            if (!request.getStatus().equals(RequestStatus.PENDING)) {
                if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
                    log.error("Request {} already confirmed", id);
                    throw new DataIntegrityViolationException("Request already confirmed");
                } else {
                    log.error("Request {} is not pending", id);
                    throw new IncorrectParameterException("Request " + id + " is not pending");
                }
            }
            if (updateRequest.getStatus().equals("CONFIRMED")) {
                if (event.getConfirmedRequests() >= event.getParticipantLimit() && event.getParticipantLimit() != 0) {
                    log.error("The participant limit is reached");
                    request.setStatus(RequestStatus.CANCELED);
                    confirmedReqs.add(RequestMapper.toParticipationRequestDto(request));
                    requestRepository.save(request);
                    throw new DataIntegrityViolationException("The participant limit is reached");
                }
                request.setStatus(RequestStatus.CONFIRMED);
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                confirmedReqs.add(RequestMapper.toParticipationRequestDto(request));
                requestRepository.save(request);
                log.info("Successfully confirmed request {}", id);
            } else {
                request.setStatus(RequestStatus.REJECTED);
                canceledReqs.add(RequestMapper.toParticipationRequestDto(request));
                requestRepository.save(request);
                log.info("Successfully rejected request {}", id);
            }
        }
        result.setConfirmedRequests(confirmedReqs);
        result.setRejectedRequests(canceledReqs);
        eventRepository.save(event);
        return result;
    }

    @Override
    @Transactional
    public String uploadImage(MultipartFile file, Long userId, Long eventId) throws IOException {
        Event event = getEventByInitiatorAndEventId(userId, eventId);

        if (event.getImageKey() != null && event.getImageUrl() != null) {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(event.getImageKey())
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        }

        String fileName = s3PackageName + UUID.randomUUID() + file.getOriginalFilename();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(file.getContentType())
                .build();

        event.setImageKey(fileName);
        event.setImageUrl(s3Endpoint + "/" + bucketName + "/" + fileName);
        event.setDescriptionOfImage(f5AIClient.getImageDescription(file));

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
        eventRepository.save(event);
        log.info("Image successfully uploaded");
        return s3Endpoint + "/" + bucketName + "/" + fileName;
    }

    private void validateEventDate(String eventDate) {
        LocalDateTime date = LocalDateTime.parse(eventDate, FORMATTER);
        if (date.isBefore(LocalDateTime.now())) {
            log.error("Date must be in future");
            throw new IncorrectParameterException("Date must be in future");
        }

        if (date.isBefore(LocalDateTime.now().plusHours(2))) {
            log.error("Date must be in future more than 2 hours");
            throw new IncorrectParameterException("Date must be in future more than 2 hours");
        }
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with id {} not found", userId);
            return new NotFoundException("User with id " + userId + " not found");
        });
    }

    private Category getCategoryById(Integer categoryId) {
        return categoryRepository.findById(Long.valueOf(categoryId)).orElseThrow(() -> {
            log.error("Category with id {} not found", categoryId);
            return new NotFoundException("Category with id " + categoryId + " not found");
        });
    }

    private Event getEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> {
            log.error("Event with id {} not found", eventId);
            return new NotFoundException("Event with id " + eventId + " not found");
        });
    }

    private Event getEventByInitiatorAndEventId(Long initiatorId, Long eventId) {
        return eventRepository.findByInitiatorIdAndId(initiatorId, eventId).orElseThrow(() -> {
            log.error("Event with id {} and initiator id {} not found", eventId, initiatorId);
            return new NotFoundException("Event with id " + eventId + " and initiator id " + initiatorId + " not found");
        });
    }

    private PageRequest validatePageable(Integer from, Integer size) {
        if (from == null || from < 0) {
            log.error("Params must be greater than 0");
            throw new IncorrectParameterException("Params must be greater than 0");
        }
        if (size == null || size < 0) {
            log.error("Params must be greater than 0");
            throw new IncorrectParameterException("Params must be greater than 0");
        }

        return PageRequest.of(from / size, size);
    }

    private Request getRequestById(Long requestId) {
        return requestRepository.findById(requestId).orElseThrow(() -> {
            log.error("Request with id {} not found", requestId);
            return new NotFoundException("Request with id " + requestId + " not found");
        });
    }
}
