package ru.afishaBMSTU.users.events;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.afishaBMSTU.users.events.model.Event;
import ru.afishaBMSTU.users.events.model.Views;

import java.time.LocalDateTime;
import java.util.List;

public interface ViewsRepository extends JpaRepository<Views, Long> {

    @Query("select v from Views as v " +
            "where v.event.id = ?1 and " +
            "v.ip = ?2")
    List<Views> findAllByEventIdAndIp(Long eventId, String ip);

    @Query("select v.event from Views as v " +
            "where v.viewedAt between :start and :end " +
            "order by v.event.views desc")
    List<Event> findAllByViewedAtBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
