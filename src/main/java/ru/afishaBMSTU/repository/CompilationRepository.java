package ru.afishaBMSTU.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.afishaBMSTU.model.compilation.Compilation;
import ru.afishaBMSTU.model.compilation.CompilationType;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    List<Compilation> findAllByPinned(Boolean pinned, Pageable pageable);

    @Modifying
    void deleteAllByType(CompilationType type);

    @Query("select c.id from Compilation  as c " +
            "where c.type = :type")
    Long findCompilationIdByType(@Param("type") CompilationType type);
}
