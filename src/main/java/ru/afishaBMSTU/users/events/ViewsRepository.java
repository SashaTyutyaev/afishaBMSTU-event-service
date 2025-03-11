package ru.afishaBMSTU.users.events;

import ru.afishaBMSTU.users.events.model.Views;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ViewsRepository extends JpaRepository<Views, Long> {

    @Query("select v from Views as v " +
            "where v.event.id = ?1 and " +
            "v.ip = ?2")
    List<Views> findAllByEventIdAndIp(Long eventId, String ip);
}
