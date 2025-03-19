package ru.afishaBMSTU.service.compilation.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.afishaBMSTU.dto.compilation.CompilationDto;
import ru.afishaBMSTU.dto.compilation.NewCompilationDto;
import ru.afishaBMSTU.dto.compilation.UpdateCompilationRequest;
import ru.afishaBMSTU.dto.event.EventShortDto;
import ru.afishaBMSTU.exceptions.NotFoundException;
import ru.afishaBMSTU.mapper.CompilationMapper;
import ru.afishaBMSTU.mapper.EventMapper;
import ru.afishaBMSTU.model.compilation.Compilation;
import ru.afishaBMSTU.model.compilation.CompilationEvent;
import ru.afishaBMSTU.model.compilation.CompilationType;
import ru.afishaBMSTU.model.event.Event;
import ru.afishaBMSTU.repository.CompilationRepository;
import ru.afishaBMSTU.repository.CompilationsEventRepository;
import ru.afishaBMSTU.repository.EventRepository;
import ru.afishaBMSTU.repository.ViewsRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service("compilationAdminService")
@RequiredArgsConstructor
@Slf4j
public class CompilationAdminServiceImpl implements CompilationAdminService {

    private final CompilationRepository compilationRepository;
    private final CompilationsEventRepository compilationsEventRepository;
    private final EventRepository eventRepository;
    private final ViewsRepository viewsRepository;

    @Override
    @Transactional
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto);
        checkIfCompilationIsPinned(newCompilationDto, compilation);
        compilation.setType(CompilationType.CREATED_BY_ADMIN);
        compilationRepository.save(compilation);
        if (newCompilationDto.getEvents() != null) {
            List<Event> events = eventRepository.findAllById(newCompilationDto.getEvents());
            addEventToCompilationEventRepository(events, compilation);
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
    public CompilationDto updateCompilation(UpdateCompilationRequest newCompilationDto, Long compId) {
        Compilation compilation = getCompilationById(compId);
        if (newCompilationDto.getTitle() != null) {
            compilation.setTitle(newCompilationDto.getTitle());
        }
        if (newCompilationDto.getPinned() != null) {
            compilation.setPinned(newCompilationDto.getPinned());
        }
        CompilationDto compilationDto = CompilationMapper.toCompilationDto(compilation);
        if (newCompilationDto.getEvents() != null) {
            List<CompilationEvent> compilationEvents = compilationsEventRepository.findAllByCompilationId(compId);
            compilationsEventRepository.deleteAll(compilationEvents);
            if (!newCompilationDto.getEvents().isEmpty()) {
                for (Long eventId : newCompilationDto.getEvents()) {
                    Event event = getEventById(eventId);
                    CompilationEvent compilationEvent = CompilationEvent.builder()
                            .compilation(compilation)
                            .event(event)
                            .build();
                    compilationsEventRepository.save(compilationEvent);
                }
            }

            List<CompilationEvent> updatedCompilationEventList = compilationsEventRepository
                    .findAllByCompilationId(compilation.getId());
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

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        getCompilationById(compId);
        log.info("Delete compilation: {}", getCompilationById(compId));
        compilationRepository.delete(getCompilationById(compId));
        compilationsEventRepository.deleteAllByCompilationId(compId);
    }

    @Scheduled(cron = "0 0 18 * * ?")
    @Transactional
    protected void createCompilationOfPopularEventsByDay() {

        deletePopularCompilations(CompilationType.DAY_POPULAR);

        List<Event> events = findEventsByViewedBetween(LocalDateTime.now().minusDays(1), LocalDateTime.now());

        Compilation compilation = createAndSavePopularCompilation("Day popular Compilation",
                CompilationType.DAY_POPULAR);

        addEventToCompilationEventRepository(events, compilation);
        log.info("Successfully created day popular compilation");
    }

    @Scheduled(cron = "0 0 18 * * MON")
    @Transactional
    protected void createCompilationOfPopularEventsByWeek() {

        deletePopularCompilations(CompilationType.WEEK_POPULAR);

        List<Event> events = findEventsByViewedBetween(LocalDateTime.now().minusDays(7), LocalDateTime.now());

        Compilation compilation = createAndSavePopularCompilation("Week popular Compilation",
                CompilationType.WEEK_POPULAR);

        addEventToCompilationEventRepository(events, compilation);
        log.info("Successfully created week popular compilation");
    }

    private void deletePopularCompilations(CompilationType type) {
        Long compilationId = compilationRepository.findCompilationIdByType(type);
        if (compilationId != null) {
            compilationsEventRepository.deleteAllByCompilationId(compilationId);
            compilationRepository.deleteById(compilationId);
        }
    }

    private Compilation createAndSavePopularCompilation(String title, CompilationType type) {
        Compilation compilation = Compilation.builder()
                .title(title)
                .type(type)
                .pinned(true)
                .build();

        return compilationRepository.save(compilation);
    }

    private List<Event> findEventsByViewedBetween(LocalDateTime start, LocalDateTime end) {
        return viewsRepository.findAllByViewedAtBetween(start, end);
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

    private void addEventToCompilationEventRepository(List<Event> events, Compilation compilation) {
        List<CompilationEvent> compilationEvents = new ArrayList<>();
        for (Event event : events) {
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
