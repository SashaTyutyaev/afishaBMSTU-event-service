package ru.afishaBMSTU.repository;

import ru.afishaBMSTU.model.user.User;
import ru.afishaBMSTU.model.event.Event;
import ru.afishaBMSTU.model.request.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query("select r from Request as r " +
            "where r.user.id = ?1 and r.event.id = ?2")
    Optional<Request> findByRequesterAndEvent(Long requesterId, Long eventId);

    List<Request> findAllByUser(User user);

    List<Request> findAllByEvent(Event event);
}
