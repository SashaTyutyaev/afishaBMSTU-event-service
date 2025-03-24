package ru.afishaBMSTU.controller.compilation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.afishaBMSTU.dto.compilation.CompilationDto;
import ru.afishaBMSTU.dto.compilation.NewCompilationDto;
import ru.afishaBMSTU.dto.compilation.UpdateCompilationRequest;
import ru.afishaBMSTU.service.compilation.admin.CompilationAdminService;

@RestController
@RequestMapping("/api/admin/compilations")
@RequiredArgsConstructor
public class CompilationAdminController {

    private final CompilationAdminService compilationAdminService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        return compilationAdminService.createCompilation(newCompilationDto);
    }

    @PatchMapping("{compId}")
    public CompilationDto updateCompilation(@PathVariable Long compId,
                                            @Valid @RequestBody UpdateCompilationRequest newCompilationDto) {
        return compilationAdminService.updateCompilation(newCompilationDto, compId);
    }

    @DeleteMapping("{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        compilationAdminService.deleteCompilation(compId);
    }
}
