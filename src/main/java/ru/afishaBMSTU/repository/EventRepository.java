package ru.afishaBMSTU.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.afishaBMSTU.model.event.Event;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    List<Event> findAllByCategoryId(Long categoryId);

    List<Event> findAllByInitiatorExternalId(UUID externalId, Pageable pageable);

    Optional<Event> findByInitiatorExternalIdAndId(UUID externalId, Long id);

}