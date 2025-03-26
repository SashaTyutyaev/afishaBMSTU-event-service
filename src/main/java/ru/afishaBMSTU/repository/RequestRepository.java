package ru.afishaBMSTU.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.afishaBMSTU.model.event.Event;
import ru.afishaBMSTU.model.request.Request;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query("select r from Request as r " +
            "where r.userExternalId = ?1 and r.event.id = ?2")
    Optional<Request> findByRequesterAndEvent(UUID externalId, Long eventId);

    List<Request> findAllByUserExternalId(UUID externalId);

    List<Request> findAllByEvent(Event event);

    @Query("select r from Request as r " +
            "where r.userExternalId = ?1 and r.id = ?2")
    Optional<Request> findByIdAndUser(UUID externalId, Long requestId);
}
