package ru.afishaBMSTU.admin.compilations;

import ru.afishaBMSTU.admin.compilations.model.CompilationEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompilationsEventRepository extends JpaRepository<CompilationEvent, Long> {

    List<CompilationEvent> findAllByCompilationId(Long id);

    void deleteAllByCompilationId(Long id);
}
