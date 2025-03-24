package ru.afishaBMSTU.controller.compilation;

import lombok.RequiredArgsConstructor;
import ru.afishaBMSTU.dto.compilation.CompilationDto;
import org.springframework.web.bind.annotation.*;
import ru.afishaBMSTU.service.compilation.CompilationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/compilations")
public class CompilationController {

    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "10") Integer size) {
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("{compId}")
    public CompilationDto getCompilation(@PathVariable Long compId) {
        return compilationService.getCompilation(compId);
    }

}
