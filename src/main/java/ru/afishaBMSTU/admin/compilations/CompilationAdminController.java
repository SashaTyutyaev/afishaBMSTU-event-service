package ru.afishaBMSTU.admin.compilations;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ru.afishaBMSTU.admin.compilations.model.dto.CompilationDto;
import ru.afishaBMSTU.admin.compilations.model.dto.NewCompilationDto;
import ru.afishaBMSTU.admin.compilations.model.dto.UpdateCompilationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class CompilationAdminController {

    private final CompilationAdminService compilationAdminService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        return compilationAdminService.addCompilation(newCompilationDto);
    }

    @PatchMapping("{compId}")
    public CompilationDto updateCompilation(@PathVariable Long compId,
                                            @Valid @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        return compilationAdminService.updateCompilation(compId, updateCompilationRequest);
    }

    @DeleteMapping("{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        compilationAdminService.deleteCompilation(compId);
    }
}
