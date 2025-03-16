package ru.afishaBMSTU.users.events;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.afishaBMSTU.users.events.model.Event;
import ru.afishaBMSTU.users.events.model.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    List<Event> findAllByCategoryId(Long categoryId);

    List<Event> findAllByInitiatorId(Long initiatorId, Pageable pageable);

    Optional<Event> findByInitiatorIdAndId(Long initiatorId, Long id);

    @Query("select e from Event e " +
            "where e.initiator.id in ?1 " +
            "and e.state in ?2 " +
            "and e.category.id in ?3 " +
            "and e.eventDate between ?4 and ?5")
    List<Event> getEventsWithUsersAndStatesAndCategoriesAndTimes(List<Long> users,
                                                                 List<State> states,
                                                                 List<Long> categories,
                                                                 LocalDateTime rangeStart,
                                                                 LocalDateTime rangeEnd,
                                                                 Pageable pageable);

    @Query("select e from Event e " +
            "where e.eventDate between ?1 and ?2")
    List<Event> getEventsWithTimes(LocalDateTime rangeStart,
                                   LocalDateTime rangeEnd,
                                   Pageable pageable);

    @Query("select e from Event e " +
            "where e.category.id in ?1 " +
            "and e.eventDate between ?2 and ?3")
    List<Event> getEventsWithCategoryAndTimes(List<Long> categories,
                                              LocalDateTime rangeStart,
                                              LocalDateTime rangeEnd,
                                              Pageable pageable);

    @Query("select e from Event e " +
            "where e.state in ?1 " +
            "and e.category.id in ?2 " +
            "and e.eventDate between ?3 and ?4")
    List<Event> getEventsWithStateAndCategoriesAndTimes(List<State> states,
                                                        List<Long> categories,
                                                        LocalDateTime rangeStart,
                                                        LocalDateTime rangeEnd,
                                                        Pageable pageable);

    @Query("select e from Event e " +
            "where e.state in ?1 " +
            "and e.eventDate between ?2 and ?3")
    List<Event> getEventsWithStateAndTimes(List<State> states,
                                           LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd,
                                           Pageable pageable);

    @Query("select e from Event e " +
            "where e.initiator.id in ?1 " +
            "and e.eventDate between ?2 and ?3")
    List<Event> getEventsWithUsersAndTimes(List<Long> users,
                                           LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd,
                                           Pageable pageable);

    @Query("select e from Event e " +
            "where e.initiator.id in ?1 " +
            "and e.category.id in ?2 " +
            "and e.eventDate between ?3 and ?4")
    List<Event> getEventsWithUsersAndTimesAndCategories(List<Long> users,
                                                        List<Long> categories,
                                                        LocalDateTime rangeStart,
                                                        LocalDateTime rangeEnd,
                                                        Pageable pageable);

    @Query("select e from Event e " +
            "where e.initiator.id in ?1 " +
            "and e.state in ?2 " +
            "and e.eventDate between ?3 and ?4")
    List<Event> getEventsWithUsersAndStatesAndTimes(List<Long> users,
                                                    List<State> states,
                                                    LocalDateTime rangeStart,
                                                    LocalDateTime rangeEnd,
                                                    Pageable pageable);

    @Query("select e from Event e " +
            "where e.eventDate > ?1")
    List<Event> getEventsWithoutTimes(LocalDateTime rangeEnd,
                                      Pageable pageable);

    @Query("select e from Event e " +
            "where e.category.id in ?1 " +
            "and e.eventDate > ?2")
    List<Event> getEventsWithCategory(List<Long> categories,
                                      LocalDateTime rangeEnd,
                                      Pageable pageable);

    @Query("select e from Event e " +
            "where e.state in ?1 " +
            "and e.category.id in ?2 " +
            "and e.eventDate > ?3")
    List<Event> getEventsWithStateAndCategories(List<State> states,
                                                List<Long> categories,
                                                LocalDateTime rangeEnd,
                                                Pageable pageable);

    @Query("select e from Event e " +
            "where e.state in ?1 " +
            "and e.eventDate > ?2")
    List<Event> getEventsWithState(List<State> states,
                                   LocalDateTime rangeEnd,
                                   Pageable pageable);

    @Query("select e from Event e " +
            "where e.initiator.id in ?1 " +
            "and e.eventDate > ?2")
    List<Event> getEventsWithUsers(List<Long> users,
                                   LocalDateTime rangeEnd,
                                   Pageable pageable);

    @Query("select e from Event e " +
            "where e.initiator.id in ?1 " +
            "and e.category.id in ?2 " +
            "and e.eventDate > ?3")
    List<Event> getEventsWithUsersAndCategories(List<Long> users,
                                                List<Long> categories,
                                                LocalDateTime rangeEnd,
                                                Pageable pageable);

    @Query("select e from Event e " +
            "where e.initiator.id in ?1 " +
            "and e.state in ?2 " +
            "and e.eventDate > ?3")
    List<Event> getEventsWithUsersAndState(List<Long> users,
                                           List<State> states,
                                           LocalDateTime rangeEnd,
                                           Pageable pageable);

    @Query("select e from Event e " +
            "where e.initiator.id in ?1 " +
            "and e.state in ?2 " +
            "and e.category.id in ?3 " +
            "and e.eventDate > ?4")
    List<Event> getEventsWithUsersAndStatesAndCategoriesWithoutTimes(List<Long> users,
                                                                     List<State> states,
                                                                     List<Long> categories,
                                                                     LocalDateTime rangeEnd,
                                                                     Pageable pageable);
}