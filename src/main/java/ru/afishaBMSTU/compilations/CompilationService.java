package ru.afishaBMSTU.compilations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.afishaBMSTU.admin.compilations.CompilationRepository;
import ru.afishaBMSTU.admin.compilations.CompilationsEventRepository;
import ru.afishaBMSTU.admin.compilations.model.Compilation;
import ru.afishaBMSTU.admin.compilations.model.CompilationEvent;
import ru.afishaBMSTU.admin.compilations.model.dto.CompilationDto;
import ru.afishaBMSTU.admin.compilations.model.dto.CompilationMapper;
import ru.afishaBMSTU.exceptions.IncorrectParameterException;
import ru.afishaBMSTU.exceptions.NotFoundException;
import ru.afishaBMSTU.users.events.EventRepository;
import ru.afishaBMSTU.users.events.model.Event;
import ru.afishaBMSTU.users.events.model.dto.EventMapper;
import ru.afishaBMSTU.users.events.model.dto.EventShortDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationService {

    private final CompilationRepository compilationRepository;
    private final CompilationsEventRepository compilationsEventRepository;
    private final EventRepository eventRepository;

    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        List<CompilationDto> result = new ArrayList<>();
        List<Compilation> compilations;
        Pageable pageable = validatePageable(from, size);
        if (pinned == null) {
            compilations = compilationRepository.findAll(pageable).getContent();
        } else {
            compilations = compilationRepository.findAllByPinned(pinned, pageable);
        }
        for (Compilation compilation : compilations) {
            List<EventShortDto> events = new ArrayList<>();
            List<CompilationEvent> compilationEvents = compilationsEventRepository.findAllByCompilationId(compilation.getId());
            for (CompilationEvent compilationEvent : compilationEvents) {
                EventShortDto event = EventMapper.toEventShortDto(getEventById(compilationEvent.getEvent().getId()));
                events.add(event);
            }
            CompilationDto compilationDto = CompilationMapper.toCompilationDto(compilation);
            compilationDto.setEvents(events);
            result.add(compilationDto);
        }
        log.info("Get compilations successful");
        return result;
    }

    public CompilationDto getCompilation(Long id) {
        Compilation compilation = getCompilationById(id);
        List<EventShortDto> events = new ArrayList<>();
        List<CompilationEvent> compilationEvents = compilationsEventRepository.findAllByCompilationId(compilation.getId());
        for (CompilationEvent compilationEvent : compilationEvents) {
            EventShortDto event = EventMapper.toEventShortDto(getEventById(compilationEvent.getEvent().getId()));
            events.add(event);
        }
        CompilationDto compilationDto = CompilationMapper.toCompilationDto(compilation);
        compilationDto.setEvents(events);
        log.info("Get compilation {} successful", compilation.getId());
        return compilationDto;
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
        return eventRepository.findById(eventId).orElseThrow(() -> {
            log.error("Event with id {} not found", eventId);
            return new NotFoundException("Event with id " + eventId + " not found");
        });
    }

    private Compilation getCompilationById(Long compId) {
        return compilationRepository.findById(compId).orElseThrow(() -> {
            log.error("Compilation with id {} not found", compId);
            return new NotFoundException("Compilation with id " + compId + " not found");
        });
    }
}
