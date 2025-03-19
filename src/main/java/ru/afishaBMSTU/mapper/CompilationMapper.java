package ru.afishaBMSTU.mapper;


import ru.afishaBMSTU.dto.compilation.CompilationDto;
import ru.afishaBMSTU.dto.compilation.NewCompilationDto;
import ru.afishaBMSTU.model.compilation.Compilation;

public class CompilationMapper {

    public static CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .build();
    }

    public static Compilation toCompilation(NewCompilationDto newCompilationDto) {
        return Compilation.builder()
                .title(newCompilationDto.getTitle())
                .build();
    }
}
