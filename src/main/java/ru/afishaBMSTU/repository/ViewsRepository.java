package ru.afishaBMSTU.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.afishaBMSTU.model.event.Event;
import ru.afishaBMSTU.model.views.Views;

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
