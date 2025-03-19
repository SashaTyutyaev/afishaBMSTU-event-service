package ru.afishaBMSTU.service.compilation.admin;

import ru.afishaBMSTU.dto.compilation.CompilationDto;
import ru.afishaBMSTU.dto.compilation.NewCompilationDto;
import ru.afishaBMSTU.dto.compilation.UpdateCompilationRequest;

public interface CompilationAdminService {
    CompilationDto createCompilation(NewCompilationDto newCompilationDto);
    CompilationDto updateCompilation(UpdateCompilationRequest newCompilationDto, Long compId);
    void deleteCompilation(Long compId);
}
