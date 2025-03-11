package ru.afishaBMSTU.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.afishaBMSTU.exceptions.IncorrectParameterException;
import ru.afishaBMSTU.exceptions.NotFoundException;
import ru.afishaBMSTU.users.comments.CommentRepository;
import ru.afishaBMSTU.users.comments.model.CommentDto;
import ru.afishaBMSTU.users.comments.model.CommentMapper;
import ru.afishaBMSTU.users.events.EventRepository;
import ru.afishaBMSTU.users.events.ViewsRepository;
import ru.afishaBMSTU.users.events.model.Event;
import ru.afishaBMSTU.users.events.model.State;
import ru.afishaBMSTU.users.events.model.Views;
import ru.afishaBMSTU.users.events.model.dto.EventFullDto;
import ru.afishaBMSTU.users.events.model.dto.EventMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublicEventService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final EventRepository repository;
    private final ViewsRepository viewsRepository;
    private final CommentRepository commentRepository;

    public List<EventFullDto> getEvents(String text,
                                        List<Long> categories,
                                        boolean paid,
                                        String rangeStart,
                                        String rangeEnd,
                                        boolean onlyAvailable,
                                        String sort,
                                        int from,
                                        int size,
                                        String ip,
                                        String requestUri) {
        if (sort == null) {
            sort = "VIEWS";
        }

        if (rangeEnd != null) {
            if (LocalDateTime.parse(rangeEnd, FORMATTER).isBefore(LocalDateTime.parse(rangeStart, FORMATTER))) {
                log.error("End time must be after start time");
                throw new IncorrectParameterException("End time must be after start time");
            }
        }
        List<Event> eventList;
        Pageable pageable = validatePageable(from, size);
        if (rangeEnd == null) {
            LocalDateTime times = LocalDateTime.now();
            if (text == null) {
                if (categories == null) {
                    if (paid) {
                        if (onlyAvailable) {
                            if (sort.equals("VIEWS")) {
                                eventList = repository.getEventsPaidAndAvailable(times, pageable);
                            } else {
                                eventList = repository.getEventsPaidAndAvailableByDate(times, pageable);
                            }
                        } else {
                            if (sort.equals("VIEWS")) {
                                eventList = repository.getEventsPaid(times, pageable);
                            } else {
                                eventList = repository.getEventsPaidByDate(times, pageable);
                            }
                        }
                    } else {
                        if (onlyAvailable) {
                            if (sort.equals("VIEWS")) {
                                eventList = repository.getEventsAvailable(times, pageable);
                            } else {
                                eventList = repository.getEventsAvailableByDate(times, pageable);
                            }
                        } else {
                            if (sort.equals("VIEWS")) {
                                eventList = repository.getEventsAll(times, pageable);
                            } else {
                                eventList = repository.getEventsAllByDate(times, pageable);
                            }
                        }
                    }
                } else {
                    if (paid) {
                        if (onlyAvailable) {
                            if (sort.equals("VIEWS")) {
                                eventList = repository.getEventsWitchCatAndPaidAndAvailable(categories,
                                        times,
                                        pageable);
                            } else {
                                eventList = repository.getEventsWitchCatAndPaidAndAvailableByDate(categories,
                                        times,
                                        pageable);
                            }
                        } else {
                            if (sort.equals("VIEWS")) {
                                eventList = repository.getEventsPaidAndCat(categories, times, pageable);
                            } else {
                                eventList = repository.getEventsPaidAndCatByDate(categories, times, pageable);
                            }
                        }
                    } else {
                        if (onlyAvailable) {
                            if (sort.equals("VIEWS")) {
                                eventList = repository.getEventsAvailableAndCat(categories, times, pageable);
                            } else {
                                eventList = repository.getEventsAvailableAndCatByDate(categories, times, pageable);
                            }
                        } else {
                            if (sort.equals("VIEWS")) {
                                eventList = repository.getEventsAllAndCat(categories, times, pageable);
                            } else {
                                eventList = repository.getEventsAllAndCatByDate(categories, times, pageable);
                            }
                        }
                    }
                }
            } else {
                if (categories == null) {
                    if (paid) {
                        if (onlyAvailable) {
                            if (sort.equals("VIEWS")) {
                                eventList = repository.getEventsTextAndAvailableAndPaid(text, times, pageable);
                            } else {
                                eventList = repository.getEventsTextAndAvailableAndPaidByDate(text, times, pageable);
                            }
                        } else {
                            if (sort.equals("VIEWS")) {
                                eventList = repository.getEventsTextAndPaid(text, times, pageable);
                            } else {
                                eventList = repository.getEventsTextAndPaidByDate(text, times, pageable);
                            }
                        }
                    } else {
                        if (onlyAvailable) {
                            if (sort.equals("VIEWS")) {
                                eventList = repository.getEventsTextAndAvailable(text, times, pageable);
                            } else {
                                eventList = repository.getEventsTextAndAvailableByDate(text, times, pageable);
                            }
                        } else {
                            if (sort.equals("VIEWS")) {
                                eventList = repository.getEventsText(text, times, pageable);
                            } else {
                                eventList = repository.getEventsTextByDate(text, times, pageable);
                            }
                        }
                    }
                } else {
                    if (paid) {
                        if (onlyAvailable) {
                            if (sort.equals("VIEWS")) {
                                eventList = repository.getEventsTextAndCategoriesAndAvailableAndPaid(text,
                                        categories,
                                        times,
                                        pageable);
                            } else {
                                eventList = repository.getEventsTextAndCategoriesAndAvailableAndPaidByDate(text,
                                        categories,
                                        times,
                                        pageable);
                            }
                        } else {
                            if (sort.equals("VIEWS")) {
                                eventList = repository.getEventsTextAndCategoriesAndPaid(text,
                                        categories,
                                        times,
                                        pageable);
                            } else {
                                eventList = repository.getEventsTextAndCategoriesAndPaidByDate(text,
                                        categories,
                                        times,
                                        pageable);
                            }
                        }
                    } else {
                        if (onlyAvailable) {
                            if (sort.equals("VIEWS")) {
                                eventList = repository.getEventsTextAndCategoriesAndAvailable(text,
                                        categories,
                                        times,
                                        pageable);
                            } else {
                                eventList = repository.getEventsTextAndCategoriesAndAvailableByDate(text,
                                        categories,
                                        times,
                                        pageable);
                            }
                        } else {
                            if (sort.equals("VIEWS")) {
                                eventList = repository.getEventsTextAndCategories(text, categories, times, pageable);
                            } else {
                                eventList = repository.getEventsTextAndCategoriesByDate(text, categories,
                                        times, pageable);
                            }
                        }
                    }
                }
            }
        } else {
            LocalDateTime start = LocalDateTime.parse(rangeStart, FORMATTER);
            LocalDateTime end = LocalDateTime.parse(rangeEnd, FORMATTER);
            if (text == null) {
                if (categories == null) {
                    if (paid) {
                        if (onlyAvailable) {
                            if (sort.equals("VIEWS")) {
                                eventList = repository.getEventsPaidAndAvailable(start, end, pageable);
                            } else {
                                eventList = repository.getEventsPaidAndAvailableByDate(start, end, pageable);
                            }
                        } else {
                            if (sort.equals("VIEWS")) {
                                eventList = repository.getEventsPaid(start, end, pageable);
                            } else {
                                eventList = repository.getEventsPaidByDate(start, end, pageable);
                            }
                        }
                    } else {
                        if (onlyAvailable) {
                            if (sort.equals("VIEWS")) {
                                eventList = repository.getEventsAvailable(start, end, pageable);
                            } else {
                                eventList = repository.getEventsAvailableByDate(start, end, pageable);
                            }
                        } else {
                            if (sort.equals("VIEWS")) {
                                eventList = repository.getEventsAll(start, end, pageable);
                            } else {
                                eventList = repository.getEventsAllByDate(start, end, pageable);
                            }
                        }
                    }
                } else {
                    if (paid) {
                        if (onlyAvailable) {
                            if (sort.equals("VIEWS")) {
                                eventList = repository.getEventsWitchCatAndPaidAndAvailable(categories, start,
                                        end, pageable);
                            } else {
                                eventList = repository.getEventsWitchCatAndPaidAndAvailableByDate(categories,
                                        start,
                                        end,
                                        pageable);
                            }
                        } else {
                            if (sort.equals("VIEWS")) {
                                eventList = repository.getEventsPaidAndCat(categories, start, end, pageable);
                            } else {
                                eventList = repository.getEventsPaidAndCatByDate(categories, start, end, pageable);
                            }
                        }
                    } else {
                        if (onlyAvailable) {
                            if (sort.equals("VIEWS")) {
                                eventList = repository.getEventsAvailableAndCat(categories, start, end, pageable);
                            } else {
                                eventList = repository.getEventsAvailableAndCatByDate(categories, start, end, pageable);
                            }
                        } else {
                            if (sort.equals("VIEWS")) {
                                eventList = repository.getEventsAllAndCat(categories, start, end, pageable);
                            } else {
                                eventList = repository.getEventsAllAndCatByDate(categories, start, end, pageable);
                            }
                        }
                    }
                }
            } else {
                if (categories == null) {
                    if (paid) {
                        if (onlyAvailable) {
                            if (sort.equals("VIEWS")) {
                                eventList = repository.getEventsTextAndAvailableAndPaid(text.toLowerCase(),
                                        start, end, pageable);
                            } else {
                                eventList = repository.getEventsTextAndAvailableAndPaidByDate(text.toLowerCase(),
                                        start, end, pageable);
                            }
                        } else {
                            if (sort.equals("VIEWS")) {
                                eventList = repository.getEventsTextAndPaid(text.toLowerCase(), start, end, pageable);
                            } else {
                                eventList = repository.getEventsTextAndPaidByDate(text.toLowerCase(),
                                        start, end, pageable);
                            }
                        }
                    } else {
                        if (onlyAvailable) {
                            if (sort.equals("VIEWS")) {
                                eventList = repository.getEventsTextAndAvailable(text.toLowerCase(),
                                        start, end, pageable);
                            } else {
                                eventList = repository.getEventsTextAndAvailableByDate(text.toLowerCase(),
                                        start, end, pageable);
                            }
                        } else {
                            if (sort.equals("VIEWS")) {
                                eventList = repository.getEventsText(text.toLowerCase(), start, end, pageable);
                            } else {
                                eventList = repository.getEventsTextByDate(text.toLowerCase(), start, end, pageable);
                            }
                        }
                    }
                } else {
                    if (paid) {
                        if (onlyAvailable) {
                            if (sort.equals("VIEWS")) {
                                eventList = repository.getEventsTextAndCategoriesAndAvailableAndPaid(text.toLowerCase(),
                                        categories,
                                        start,
                                        end,
                                        pageable);
                            } else {
                                eventList = repository
                                        .getEventsTextAndCategoriesAndAvailableAndPaidByDate(text.toLowerCase(),
                                                categories,
                                                start,
                                                end,
                                                pageable);
                            }
                        } else {
                            if (sort.equals("VIEWS")) {
                                eventList = repository.getEventsTextAndCategoriesAndPaid(text.toLowerCase(),
                                        categories,
                                        start,
                                        end,
                                        pageable);
                            } else {
                                eventList = repository.getEventsTextAndCategoriesAndPaidByDate(text.toLowerCase(),
                                        categories,
                                        start,
                                        end,
                                        pageable);
                            }
                        }
                    } else {
                        if (onlyAvailable) {
                            if (sort.equals("VIEWS")) {
                                eventList = repository.getEventsTextAndCategoriesAndAvailable(text.toLowerCase(),
                                        categories,
                                        start,
                                        end,
                                        pageable);
                            } else {
                                eventList = repository.getEventsTextAndCategoriesAndAvailableByDate(text.toLowerCase(),
                                        categories,
                                        start,
                                        end,
                                        pageable);
                            }
                        } else {
                            if (sort.equals("VIEWS")) {
                                eventList = repository.getEventsTextAndCategories(text.toLowerCase(),
                                        categories, start, end, pageable);
                            } else {
                                eventList = repository.getEventsTextAndCategoriesByDate(text.toLowerCase(), categories,
                                        start, end, pageable);
                            }
                        }
                    }
                }
            }
        }
        List<EventFullDto> eventFullDtoList = new ArrayList<>();
        for (Event event : eventList) {
            EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
            eventFullDtoList.add(eventFullDto);
        }
        log.info("Get all event with params successful");
        return eventFullDtoList;
    }

    public EventFullDto getEvent(Long id, String ip, String uri) {
        Event event = getEventById(id);
        if (!event.getState().equals(State.PUBLISHED)) {
            log.error("Event {} is not published", id);
            throw new NotFoundException("Event " + id + " is not published");
        }
        if (viewsRepository.findAllByEventIdAndIp(id, ip).isEmpty()) {
            event.setViews(event.getViews() + 1);
            repository.save(event);
            Views views = Views.builder()
                    .ip(ip)
                    .event(event)
                    .build();
            viewsRepository.save(views);
        }
        log.info("Get event {} successful", event.getId());
        return EventMapper.toEventFullDto(event);
    }

    public List<CommentDto> getCommentsByEvent(Long eventId, Integer from, Integer size) {
        Pageable pageable = validatePageable(from, size);
        Event event = getEventById(eventId);
        return commentRepository.getCommentsByEvent(event, pageable).stream()
                .map(CommentMapper::toCommentDto).collect(Collectors.toList());
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

    private Event getEventById(Long eventId) {
        return repository.findById(eventId).orElseThrow(() -> {
            log.error("Event with id {} not found", eventId);
            return new NotFoundException("Event with id " + eventId + " not found");
        });
    }
}
