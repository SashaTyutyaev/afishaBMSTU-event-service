package ru.afishaBMSTU.admin.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.afishaBMSTU.admin.categories.CategoryRepository;
import ru.afishaBMSTU.admin.categories.model.Category;
import ru.afishaBMSTU.admin.events.model.UpdateEventAdminRequest;
import ru.afishaBMSTU.exceptions.IncorrectParameterException;
import ru.afishaBMSTU.exceptions.NotFoundException;
import ru.afishaBMSTU.users.events.EventRepository;
import ru.afishaBMSTU.users.events.model.Event;
import ru.afishaBMSTU.users.events.model.State;
import ru.afishaBMSTU.users.events.model.dto.EventFullDto;
import ru.afishaBMSTU.users.events.model.dto.EventMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminEventService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

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

    public List<EventFullDto> getEvents(List<Long> users,
                                        List<String> states,
                                        List<Long> categories,
                                        String rangeStart,
                                        String rangeEnd,
                                        Integer from,
                                        Integer size) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Pageable pageable = validatePageable(from, size);

        List<Event> eventsList;
        if (rangeEnd == null) {
            if (users == null) {
                if (states == null) {
                    if (categories == null) {
                        eventsList = eventRepository.getEventsWithoutTimes(LocalDateTime.now(),
                                pageable);
                    } else {
                        eventsList = eventRepository.getEventsWithCategory(categories,
                                LocalDateTime.now(),
                                pageable);
                    }
                } else {
                    List<State> stateList = new ArrayList<>();
                    for (String str : states) {
                        stateList.add(State.valueOf(str));
                    }
                    if (categories == null) {
                        eventsList = eventRepository.getEventsWithState(stateList,
                                LocalDateTime.now(),
                                pageable);
                    } else {
                        eventsList = eventRepository.getEventsWithStateAndCategories(stateList,
                                categories,
                                LocalDateTime.now(),
                                pageable);
                    }
                }
            } else {
                if (states == null) {
                    if (categories == null) {
                        eventsList = eventRepository.getEventsWithUsers(users,
                                LocalDateTime.now(),
                                pageable);
                    } else {
                        eventsList = eventRepository.getEventsWithUsersAndCategories(users,
                                categories,
                                LocalDateTime.now(),
                                pageable);
                    }
                } else {
                    List<State> stateList = new ArrayList<>();
                    for (String str : states) {
                        stateList.add(State.valueOf(str));
                    }
                    if (categories == null) {
                        eventsList = eventRepository.getEventsWithUsersAndState(users,
                                stateList,
                                LocalDateTime.now(),
                                pageable);
                    } else {
                        eventsList = eventRepository.getEventsWithUsersAndStatesAndCategoriesWithoutTimes(users,
                                stateList,
                                categories,
                                LocalDateTime.now(),
                                pageable);
                    }
                }
            }
        } else {
            if (users == null) {
                if (states == null) {
                    if (categories == null) {
                        eventsList = eventRepository.getEventsWithTimes(LocalDateTime.parse(rangeStart, formatter),
                                LocalDateTime.parse(rangeEnd, formatter),
                                pageable);

                    } else {
                        eventsList = eventRepository.getEventsWithCategoryAndTimes(categories,
                                LocalDateTime.parse(rangeStart, formatter),
                                LocalDateTime.parse(rangeEnd, formatter),
                                pageable);
                    }
                } else {
                    List<State> stateList = new ArrayList<>();
                    for (String str : states) {
                        stateList.add(State.valueOf(str));
                    }
                    if (categories == null) {
                        System.out.println("Мы сюда попадаем?");
                        eventsList = eventRepository.getEventsWithStateAndTimes(stateList,
                                LocalDateTime.parse(rangeStart, formatter),
                                LocalDateTime.parse(rangeEnd, formatter),
                                pageable);
                    } else {
                        eventsList = eventRepository.getEventsWithStateAndCategoriesAndTimes(stateList,
                                categories,
                                LocalDateTime.parse(rangeStart, formatter),
                                LocalDateTime.parse(rangeEnd, formatter),
                                pageable);
                    }
                }
            } else {
                if (states == null) {
                    if (categories == null) {
                        eventsList = eventRepository.getEventsWithUsersAndTimes(users,
                                LocalDateTime.parse(rangeStart, formatter),
                                LocalDateTime.parse(rangeEnd, formatter),
                                pageable);
                    } else {
                        eventsList = eventRepository.getEventsWithUsersAndTimesAndCategories(users,
                                categories,
                                LocalDateTime.parse(rangeStart, formatter),
                                LocalDateTime.parse(rangeEnd, formatter),
                                pageable);
                    }
                } else {
                    List<State> stateList = new ArrayList<>();
                    for (String str : states) {
                        stateList.add(State.valueOf(str));
                    }
                    if (categories == null) {
                        eventsList = eventRepository.getEventsWithUsersAndStatesAndTimes(users,
                                stateList,
                                LocalDateTime.parse(rangeStart, formatter),
                                LocalDateTime.parse(rangeEnd, formatter),
                                pageable);
                    } else {
                        eventsList = eventRepository.getEventsWithUsersAndStatesAndCategoriesAndTimes(users,
                                stateList,
                                categories,
                                LocalDateTime.parse(rangeStart, formatter),
                                LocalDateTime.parse(rangeEnd, formatter),
                                pageable);
                        System.out.println(eventsList.size());
                    }
                }
            }
        }
        List<EventFullDto> eventFullDtoList = new ArrayList<>();
        if (!eventsList.isEmpty()) {
            for (Event events : eventsList) {
                eventFullDtoList.add(EventMapper.toEventFullDto(events));
            }
        }
        return eventFullDtoList;
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
