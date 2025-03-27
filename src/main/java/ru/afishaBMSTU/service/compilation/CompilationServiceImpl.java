package ru.afishaBMSTU.service.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.afishaBMSTU.dto.compilation.CompilationDto;
import ru.afishaBMSTU.dto.event.EventShortDto;
import ru.afishaBMSTU.exceptions.IncorrectParameterException;
import ru.afishaBMSTU.exceptions.NotFoundException;
import ru.afishaBMSTU.mapper.CompilationMapper;
import ru.afishaBMSTU.mapper.EventMapper;
import ru.afishaBMSTU.model.compilation.Compilation;
import ru.afishaBMSTU.model.compilation.CompilationEvent;
import ru.afishaBMSTU.model.event.Event;
import ru.afishaBMSTU.repository.CompilationRepository;
import ru.afishaBMSTU.repository.CompilationsEventRepository;
import ru.afishaBMSTU.repository.EventRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final CompilationsEventRepository compilationsEventRepository;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final CompilationMapper compilationMapper;

    @Override
    @Transactional(readOnly = true)
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
            CompilationDto compilationDto = setEventsToCompilation(compilation);
            result.add(compilationDto);
        }
        log.info("Get compilations successful");
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDto getCompilation(Long id) {
        Compilation compilation = getCompilationById(id);

        CompilationDto compilationDto = setEventsToCompilation(compilation);

        log.info("Get compilation {} successful", compilation.getId());
        return compilationDto;
    }

    private CompilationDto setEventsToCompilation(Compilation compilation) {
        List<EventShortDto> events = new ArrayList<>();
        List<CompilationEvent> compilationEvents = compilationsEventRepository.findAllByCompilationId(compilation.getId());
        for (CompilationEvent compilationEvent : compilationEvents) {
            EventShortDto event = eventMapper.toEventShortDto(getEventById(compilationEvent.getEvent().getId()));
            events.add(event);
        }
        CompilationDto compilationDto = compilationMapper.toCompilationDto(compilation);
        compilationDto.setEvents(events);
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
