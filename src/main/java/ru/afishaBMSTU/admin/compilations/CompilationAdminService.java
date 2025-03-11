package ru.afishaBMSTU.admin.compilations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.afishaBMSTU.admin.compilations.model.Compilation;
import ru.afishaBMSTU.admin.compilations.model.CompilationEvent;
import ru.afishaBMSTU.admin.compilations.model.dto.CompilationDto;
import ru.afishaBMSTU.admin.compilations.model.dto.CompilationMapper;
import ru.afishaBMSTU.admin.compilations.model.dto.NewCompilationDto;
import ru.afishaBMSTU.admin.compilations.model.dto.UpdateCompilationRequest;
import ru.afishaBMSTU.exceptions.NotFoundException;
import ru.afishaBMSTU.users.events.EventRepository;
import ru.afishaBMSTU.users.events.model.Event;
import ru.afishaBMSTU.users.events.model.dto.EventMapper;
import ru.afishaBMSTU.users.events.model.dto.EventShortDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationAdminService {

    private final CompilationRepository compilationRepository;
    private final CompilationsEventRepository compilationsEventRepository;
    private final EventRepository eventRepository;

    @Transactional
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto);
        checkIfCompilationIsPinned(newCompilationDto, compilation);
        compilationRepository.save(compilation);
        if (newCompilationDto.getEvents() != null) {
            addEventToCompilationEventRepository(newCompilationDto, compilation);
        }
        List<CompilationEvent> compilationEventList = compilationsEventRepository.findAllByCompilationId(compilation.getId());
        List<EventShortDto> events = new ArrayList<>();
        if (!compilationEventList.isEmpty()) {
            for (CompilationEvent compilationEvent : compilationEventList) {
                events.add(EventMapper.toEventShortDto(compilationEvent.getEvent()));
            }
        }
        CompilationDto compilationDto = CompilationMapper.toCompilationDto(compilation);
        compilationDto.setEvents(events);
        log.info("Add compilation: {}", compilationDto);
        return compilationDto;
    }

    @Transactional
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = getCompilationById(compId);
        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }
        CompilationDto compilationDto = CompilationMapper.toCompilationDto(compilation);
        if (updateCompilationRequest.getEvents() != null) {
            List<CompilationEvent> compilationEvents = compilationsEventRepository.findAllByCompilationId(compId);
            compilationsEventRepository.deleteAll(compilationEvents);
            if (!updateCompilationRequest.getEvents().isEmpty()) {
                for (Long eventId : updateCompilationRequest.getEvents()) {
                    Event event = getEventById(eventId);
                    CompilationEvent compilationEvent = CompilationEvent.builder()
                            .compilation(compilation)
                            .event(event)
                            .build();
                    compilationsEventRepository.save(compilationEvent);
                }
            }

            List<CompilationEvent> updatedCompilationEventList = compilationsEventRepository.findAllByCompilationId(compilation.getId());
            List<EventShortDto> updatedEvents = new ArrayList<>();
            if (!updatedCompilationEventList.isEmpty()) {
                for (CompilationEvent compilationEvent : updatedCompilationEventList) {
                    updatedEvents.add(EventMapper.toEventShortDto(compilationEvent.getEvent()));
                }
            }
            compilationDto.setEvents(updatedEvents);
        }
        log.info("Update compilation: {}", compilationDto);
        return compilationDto;
    }

    @Transactional
    public void deleteCompilation(Long compId) {
        getCompilationById(compId);
        log.info("Delete compilation: {}", getCompilationById(compId));
        compilationRepository.delete(getCompilationById(compId));
        compilationsEventRepository.deleteAllByCompilationId(compId);
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

    private void addEventToCompilationEventRepository(NewCompilationDto newCompilationDto, Compilation compilation) {
        List<CompilationEvent> compilationEvents = new ArrayList<>();
        for (Long eventId : newCompilationDto.getEvents()) {
            Event event = getEventById(eventId);
            compilationEvents.add(CompilationEvent.builder()
                    .compilation(compilation)
                    .event(event)
                    .build());
        }
        compilationsEventRepository.saveAll(compilationEvents);
    }

    private void checkIfCompilationIsPinned(NewCompilationDto newCompilationDto, Compilation compilation) {
        compilation.setPinned(newCompilationDto.getPinned() != null && newCompilationDto.getPinned());
    }
}
