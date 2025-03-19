package ru.afishaBMSTU.service.event.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.afishaBMSTU.dto.event.EventAdminFilterDto;
import ru.afishaBMSTU.dto.event.EventFullDto;
import ru.afishaBMSTU.dto.event.StateActionAdmin;
import ru.afishaBMSTU.dto.event.UpdateEventAdminRequest;
import ru.afishaBMSTU.exceptions.IncorrectParameterException;
import ru.afishaBMSTU.exceptions.NotFoundException;
import ru.afishaBMSTU.mapper.EventMapper;
import ru.afishaBMSTU.model.category.Category;
import ru.afishaBMSTU.model.event.Event;
import ru.afishaBMSTU.model.event.State;
import ru.afishaBMSTU.repository.CategoryRepository;
import ru.afishaBMSTU.repository.EventRepository;
import ru.afishaBMSTU.specification.EventSpecification;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminEventServiceImpl implements AdminEventService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateEvent) {
        Event event = getEventById(eventId);
        if (updateEvent.getAnnotation() != null) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getDescription() != null) {
            event.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getRequestModeration() != null) {
            event.setRequestModeration(updateEvent.getRequestModeration());
        }
        if (updateEvent.getEventDate() != null) {
            if (LocalDateTime.parse(updateEvent.getEventDate(), FORMATTER).isBefore(LocalDateTime.now())) {
                log.error("Event date is before current date");
                throw new IncorrectParameterException("Event date is before current date");
            }
            event.setEventDate(LocalDateTime.parse(updateEvent.getEventDate(), FORMATTER));
        }
        if (updateEvent.getTitle() != null) {
            event.setTitle(updateEvent.getTitle());
        }
        if (updateEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getLocation() != null) {
            event.setLat(updateEvent.getLocation().getLat());
            event.setLon(updateEvent.getLocation().getLon());
        }
        if (updateEvent.getCategory() != null) {
            Category category = getCategoryById(updateEvent.getCategory());
            event.setCategory(category);
        }
        if (updateEvent.getStateAction() != null) {
            if (updateEvent.getStateAction().equals(StateActionAdmin.PUBLISH_EVENT)
                    && event.getState().equals(State.PUBLISHED)) {
                log.error("Event is already published");
                throw new DataIntegrityViolationException("Event is already published");
            }
            if (updateEvent.getStateAction().equals(StateActionAdmin.PUBLISH_EVENT)
                    && event.getState().equals(State.CANCELED)) {
                log.error("Event is canceled");
                throw new DataIntegrityViolationException("Event is canceled");
            }
            if (updateEvent.getStateAction().equals(StateActionAdmin.REJECT_EVENT)
                    && event.getState().equals(State.PUBLISHED)) {
                log.error("Event is already published");
                throw new DataIntegrityViolationException("Event is already published");
            }
            if (updateEvent.getStateAction().equals(StateActionAdmin.REJECT_EVENT)) {
                event.setState(State.CANCELED);
            } else {
                event.setState(State.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            }
        }
        eventRepository.save(event);
        log.info("Event updated by admin successful");
        return EventMapper.toEventFullDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> getEvents(EventAdminFilterDto filterDto,
                                        Integer from,
                                        Integer size) {
        Pageable pageable = validatePageable(from, size);

        Specification<Event> spec = EventSpecification.withAdminFilters(filterDto);
        List<Event> events = eventRepository.findAll(spec, pageable).getContent();
        if (!CollectionUtils.isEmpty(events)) {
            return events.stream()
                    .map(EventMapper::toEventFullDto)
                    .toList();
        } else {
            return Collections.emptyList();
        }
    }

    private Event getEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> {
            log.error("Event with id {} not found", eventId);
            return new NotFoundException("Event with id " + eventId + " not found");
        });
    }

    private Category getCategoryById(Integer categoryId) {
        return categoryRepository.findById(Long.valueOf(categoryId)).orElseThrow(() -> {
            log.error("Category with id {} not found", categoryId);
            return new NotFoundException("Category with id " + categoryId + " not found");
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
}
