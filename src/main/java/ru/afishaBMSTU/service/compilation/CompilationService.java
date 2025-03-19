package ru.afishaBMSTU.service.compilation;

import ru.afishaBMSTU.dto.compilation.CompilationDto;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);
    CompilationDto getCompilation(Long id);
}
