package ru.afishaBMSTU.mapper;


import org.mapstruct.Mapper;
import ru.afishaBMSTU.dto.compilation.CompilationDto;
import ru.afishaBMSTU.dto.compilation.NewCompilationDto;
import ru.afishaBMSTU.model.compilation.Compilation;

@Mapper(componentModel = "spring")
public interface CompilationMapper {

    CompilationDto toCompilationDto(Compilation compilation);

    Compilation toCompilation(NewCompilationDto newCompilationDto);
}
