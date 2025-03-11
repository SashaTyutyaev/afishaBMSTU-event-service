package ru.afishaBMSTU.users.events;

import ru.afishaBMSTU.users.events.model.Event;
import ru.afishaBMSTU.users.events.model.State;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByCategoryId(Long categoryId);

    List<Event> findAllByInitiatorId(Long initiatorId, Pageable pageable);

    Event findByInitiatorIdAndId(Long initiatorId, Long id);

    @Query("select e from Event e " +
            "where e.paid = true " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate > ?1 " +
            "order by e.views desc")
    List<Event> getEventsPaidAndAvailable(LocalDateTime now,
                                          Pageable pageable);

    @Query("select e from Event e " +
            "where e.paid = true " +
            "and e.eventDate > ?1 " +
            "order by e.views desc")
    List<Event> getEventsPaid(LocalDateTime now,
                              Pageable pageable);

    @Query("select e from Event e " +
            "where e.confirmedRequests < e.participantLimit " +
            "and e.eventDate > ?1 " +
            "order by e.views desc")
    List<Event> getEventsAvailable(LocalDateTime now,
                                   Pageable pageable);

    @Query("select e from Event e " +
            "where e.eventDate > ?1 " +
            "order by e.views desc")
    List<Event> getEventsAll(LocalDateTime now,
                             Pageable pageable);

    @Query("select e from Event e " +
            "where e.paid = true " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate > ?1 " +
            "order by e.eventDate desc")
    List<Event> getEventsPaidAndAvailableByDate(LocalDateTime now,
                                                Pageable pageable);

    @Query("select e from Event e " +
            "where e.paid = true " +
            "and e.eventDate > ?1 " +
            "order by e.eventDate desc")
    List<Event> getEventsPaidByDate(LocalDateTime now,
                                    Pageable pageable);

    @Query("select e from Event e " +
            "where e.confirmedRequests < e.participantLimit " +
            "and e.eventDate > ?1 " +
            "order by e.eventDate desc")
    List<Event> getEventsAvailableByDate(LocalDateTime now,
                                         Pageable pageable);

    @Query("select e from Event e " +
            "where e.eventDate > ?1 " +
            "order by e.eventDate desc")
    List<Event> getEventsAllByDate(LocalDateTime now,
                                   Pageable pageable);

    @Query("select e from Event e " +
            "where e.paid = true " +
            "and e.category.id in ?1 " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate > ?2 " +
            "order by e.views desc")
    List<Event> getEventsWitchCatAndPaidAndAvailable(List<Long> categories,
                                                     LocalDateTime now,
                                                     Pageable pageable);

    @Query("select e from Event e " +
            "where e.paid = true " +
            "and e.category.id in ?1 " +
            "and e.eventDate > ?2 " +
            "order by e.views desc")
    List<Event> getEventsPaidAndCat(List<Long> categories,
                                    LocalDateTime now,
                                    Pageable pageable);

    @Query("select e from Event e " +
            "where e.category.id in ?1 " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate > ?2 " +
            "order by e.views desc")
    List<Event> getEventsAvailableAndCat(List<Long> categories,
                                         LocalDateTime now,
                                         Pageable pageable);

    @Query("select e from Event e " +
            "where e.category.id in ?1 " +
            "and e.eventDate > ?2 " +
            "order by e.views desc")
    List<Event> getEventsAllAndCat(List<Long> categories,
                                   LocalDateTime now,
                                   Pageable pageable);

    @Query("select e from Event e " +
            "where e.paid = true " +
            "and e.category.id in ?1 " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate > ?2 " +
            "order by e.eventDate desc")
    List<Event> getEventsWitchCatAndPaidAndAvailableByDate(List<Long> categories,
                                                           LocalDateTime now,
                                                           Pageable pageable);

    @Query("select e from Event e " +
            "where e.paid = true " +
            "and e.category.id in ?1 " +
            "and e.eventDate > ?2 " +
            "order by e.eventDate desc")
    List<Event> getEventsPaidAndCatByDate(List<Long> categories,
                                          LocalDateTime now,
                                          Pageable pageable);

    @Query("select e from Event e " +
            "where e.category.id in ?1 " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate > ?2 " +
            "order by e.eventDate desc")
    List<Event> getEventsAvailableAndCatByDate(List<Long> categories,
                                               LocalDateTime now,
                                               Pageable pageable);

    @Query("select e from Event e " +
            "where e.category.id in ?1 " +
            "and e.eventDate > ?2 " +
            "order by e.eventDate desc")
    List<Event> getEventsAllAndCatByDate(List<Long> categories,
                                         LocalDateTime now,
                                         Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.paid = true " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate > ?2 " +
            "order by e.views desc")
    List<Event> getEventsTextAndAvailableAndPaid(String text,
                                                 LocalDateTime now,
                                                 Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate > ?2 " +
            "order by e.views desc")
    List<Event> getEventsTextAndAvailable(String text,
                                          LocalDateTime now,
                                          Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.paid = true " +
            "and e.eventDate > ?2 " +
            "order by e.views desc")
    List<Event> getEventsTextAndPaid(String text,
                                     LocalDateTime now,
                                     Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.eventDate > ?2 " +
            "order by e.views desc")
    List<Event> getEventsText(String text,
                              LocalDateTime now,
                              Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.paid = true " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate > ?2 " +
            "order by e.eventDate desc")
    List<Event> getEventsTextAndAvailableAndPaidByDate(String text,
                                                       LocalDateTime now,
                                                       Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate > ?2 " +
            "order by e.eventDate desc")
    List<Event> getEventsTextAndAvailableByDate(String text,
                                                LocalDateTime now,
                                                Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.paid = true " +
            "and e.eventDate > ?2 " +
            "order by e.eventDate desc")
    List<Event> getEventsTextAndPaidByDate(String text,
                                           LocalDateTime now,
                                           Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.eventDate > ?2 " +
            "order by e.eventDate desc")
    List<Event> getEventsTextByDate(String text,
                                    LocalDateTime now,
                                    Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.category.id in ?2" +
            "and e.paid = true " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate > ?3 " +
            "order by e.views desc")
    List<Event> getEventsTextAndCategoriesAndAvailableAndPaid(String text,
                                                              List<Long> categories,
                                                              LocalDateTime now,
                                                              Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.category.id in ?2" +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate > ?3 " +
            "order by e.views desc")
    List<Event> getEventsTextAndCategoriesAndAvailable(String text,
                                                       List<Long> categories,
                                                       LocalDateTime now,
                                                       Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.category.id in ?2 " +
            "and e.paid = true " +
            "and e.eventDate > ?3 " +
            "order by e.views desc")
    List<Event> getEventsTextAndCategoriesAndPaid(String text,
                                                  List<Long> categories,
                                                  LocalDateTime times,
                                                  Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.category.id in ?2 " +
            "and e.eventDate > ?3 " +
            "order by e.views desc")
    List<Event> getEventsTextAndCategories(String text,
                                           List<Long> categories,
                                           LocalDateTime times,
                                           Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.category.id in ?2 " +
            "and e.paid = true " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate > ?3 " +
            "order by e.eventDate desc")
    List<Event> getEventsTextAndCategoriesAndAvailableAndPaidByDate(String text,
                                                                    List<Long> categories,
                                                                    LocalDateTime now,
                                                                    Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.category.id in ?2" +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate > ?3 " +
            "order by e.eventDate desc")
    List<Event> getEventsTextAndCategoriesAndAvailableByDate(String text,
                                                             List<Long> categories,
                                                             LocalDateTime now,
                                                             Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.category.id in ?2 " +
            "and e.paid = true " +
            "and e.eventDate > ?3 " +
            "order by e.eventDate desc")
    List<Event> getEventsTextAndCategoriesAndPaidByDate(String text,
                                                        List<Long> categories,
                                                        LocalDateTime now,
                                                        Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.category.id in ?2 " +
            "and e.eventDate > ?3 " +
            "order by e.eventDate desc")
    List<Event> getEventsTextAndCategoriesByDate(String text,
                                                 List<Long> categories,
                                                 LocalDateTime now,
                                                 Pageable pageable);

    @Query("select e from Event e " +
            "where e.paid = true " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate between ?1 and ?2 " +
            "order by e.views desc")
    List<Event> getEventsPaidAndAvailable(LocalDateTime start,
                                          LocalDateTime end,
                                          Pageable pageable);

    @Query("select e from Event e " +
            "where e.paid = true " +
            "and e.eventDate between ?1 and ?2 " +
            "order by e.views desc")
    List<Event> getEventsPaid(LocalDateTime start,
                              LocalDateTime end,
                              Pageable pageable);

    @Query("select e from Event e " +
            "where e.confirmedRequests < e.participantLimit " +
            "and e.eventDate between ?1 and ?2 " +
            "order by e.views desc")
    List<Event> getEventsAvailable(LocalDateTime start,
                                   LocalDateTime end,
                                   Pageable pageable);

    @Query("select e from Event e " +
            "where e.eventDate between ?1 and ?2 " +
            "order by e.views desc")
    List<Event> getEventsAll(LocalDateTime start,
                             LocalDateTime end,
                             Pageable pageable);

    @Query("select e from Event e " +
            "where e.paid = true " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate between ?1 and ?2 " +
            "order by e.eventDate desc")
    List<Event> getEventsPaidAndAvailableByDate(LocalDateTime start,
                                                LocalDateTime end,
                                                Pageable pageable);

    @Query("select e from Event e " +
            "where e.paid = true " +
            "and e.eventDate between ?1 and ?2 " +
            "order by e.eventDate desc")
    List<Event> getEventsPaidByDate(LocalDateTime start,
                                    LocalDateTime end,
                                    Pageable pageable);

    @Query("select e from Event e " +
            "where e.confirmedRequests < e.participantLimit " +
            "and e.eventDate between ?1 and ?2 " +
            "order by e.eventDate desc")
    List<Event> getEventsAvailableByDate(LocalDateTime start,
                                         LocalDateTime end,
                                         Pageable pageable);

    @Query("select e from Event e " +
            "where e.eventDate between ?1 and ?2 " +
            "order by e.eventDate desc")
    List<Event> getEventsAllByDate(LocalDateTime start,
                                   LocalDateTime end,
                                   Pageable pageable);

    @Query("select e from Event e " +
            "where e.paid = true " +
            "and e.category.id in ?1 " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate between ?2 and ?3 " +
            "order by e.views desc")
    List<Event> getEventsWitchCatAndPaidAndAvailable(List<Long> categories,
                                                     LocalDateTime start,
                                                     LocalDateTime end,
                                                     Pageable pageable);

    @Query("select e from Event e " +
            "where e.paid = true " +
            "and e.category.id in ?1 " +
            "and e.eventDate between ?2 and ?3 " +
            "order by e.views desc")
    List<Event> getEventsPaidAndCat(List<Long> categories,
                                    LocalDateTime start,
                                    LocalDateTime end,
                                    Pageable pageable);

    @Query("select e from Event e " +
            "where e.category.id in ?1 " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate between ?2 and ?3 " +
            "order by e.views desc")
    List<Event> getEventsAvailableAndCat(List<Long> categories,
                                         LocalDateTime start,
                                         LocalDateTime end,
                                         Pageable pageable);

    @Query("select e from Event e " +
            "where e.category.id in ?1 " +
            "and e.eventDate between ?2 and ?3 " +
            "order by e.views desc")
    List<Event> getEventsAllAndCat(List<Long> categories,
                                   LocalDateTime start,
                                   LocalDateTime end,
                                   Pageable pageable);

    @Query("select e from Event e " +
            "where e.paid = true " +
            "and e.category.id in ?1 " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate between ?2 and ?3 " +
            "order by e.eventDate desc")
    List<Event> getEventsWitchCatAndPaidAndAvailableByDate(List<Long> categories,
                                                           LocalDateTime start,
                                                           LocalDateTime end,
                                                           Pageable pageable);

    @Query("select e from Event e " +
            "where e.paid = true " +
            "and e.category.id in ?1 " +
            "and e.eventDate between ?2 and ?3 " +
            "order by e.eventDate desc")
    List<Event> getEventsPaidAndCatByDate(List<Long> categories,
                                          LocalDateTime start,
                                          LocalDateTime end,
                                          Pageable pageable);

    @Query("select e from Event e " +
            "where e.category.id in ?1 " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate between ?2 and ?3 " +
            "order by e.eventDate desc")
    List<Event> getEventsAvailableAndCatByDate(List<Long> categories,
                                               LocalDateTime start,
                                               LocalDateTime end,
                                               Pageable pageable);

    @Query("select e from Event e " +
            "where e.category.id in ?1 " +
            "and e.eventDate between ?2 and ?3 " +
            "order by e.eventDate desc")
    List<Event> getEventsAllAndCatByDate(List<Long> categories,
                                         LocalDateTime start,
                                         LocalDateTime end,
                                         Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.paid = true " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate between ?2 and ?3 " +
            "order by e.views desc")
    List<Event> getEventsTextAndAvailableAndPaid(String text,
                                                 LocalDateTime start,
                                                 LocalDateTime end,
                                                 Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate between ?2 and ?3 " +
            "order by e.views desc")
    List<Event> getEventsTextAndAvailable(String text,
                                          LocalDateTime start,
                                          LocalDateTime end,
                                          Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.paid = true " +
            "and e.eventDate between ?2 and ?3 " +
            "order by e.views desc")
    List<Event> getEventsTextAndPaid(String text,
                                     LocalDateTime start,
                                     LocalDateTime end,
                                     Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.eventDate between ?2 and ?3 " +
            "order by e.views desc")
    List<Event> getEventsText(String text,
                              LocalDateTime start,
                              LocalDateTime end,
                              Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.paid = true " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate between ?2 and ?3 " +
            "order by e.eventDate desc")
    List<Event> getEventsTextAndAvailableAndPaidByDate(String text,
                                                       LocalDateTime start,
                                                       LocalDateTime end,
                                                       Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate between ?2 and ?3 " +
            "order by e.eventDate desc")
    List<Event> getEventsTextAndAvailableByDate(String text,
                                                LocalDateTime start,
                                                LocalDateTime end,
                                                Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.paid = true " +
            "and e.eventDate between ?2 and ?3 " +
            "order by e.eventDate desc")
    List<Event> getEventsTextAndPaidByDate(String text,
                                           LocalDateTime start,
                                           LocalDateTime end,
                                           Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.eventDate between ?2 and ?3 " +
            "order by e.eventDate desc")
    List<Event> getEventsTextByDate(String text,
                                    LocalDateTime start,
                                    LocalDateTime end,
                                    Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.category.id in ?2" +
            "and e.paid = true " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate between ?3 and ?4 " +
            "order by e.views desc")
    List<Event> getEventsTextAndCategoriesAndAvailableAndPaid(String text,
                                                              List<Long> categories,
                                                              LocalDateTime start,
                                                              LocalDateTime end,
                                                              Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.category.id in ?2" +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate between ?3 and ?4 " +
            "order by e.views desc")
    List<Event> getEventsTextAndCategoriesAndAvailable(String text,
                                                       List<Long> categories,
                                                       LocalDateTime start,
                                                       LocalDateTime end,
                                                       Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.category.id in ?2" +
            "and e.paid = true " +
            "and e.eventDate between ?3 and ?4 " +
            "order by e.views desc")
    List<Event> getEventsTextAndCategoriesAndPaid(String text,
                                                  List<Long> categories,
                                                  LocalDateTime start,
                                                  LocalDateTime end,
                                                  Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.category.id in ?2" +
            "and e.eventDate between ?3 and ?4 " +
            "order by e.views desc")
    List<Event> getEventsTextAndCategories(String text,
                                           List<Long> categories,
                                           LocalDateTime start,
                                           LocalDateTime end,
                                           Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.category.id in ?2" +
            "and e.paid = true " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate between ?3 and ?4 " +
            "order by e.eventDate desc")
    List<Event> getEventsTextAndCategoriesAndAvailableAndPaidByDate(String text,
                                                                    List<Long> categories,
                                                                    LocalDateTime start,
                                                                    LocalDateTime end,
                                                                    Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.category.id in ?2" +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate between ?3 and ?4 " +
            "order by e.eventDate desc")
    List<Event> getEventsTextAndCategoriesAndAvailableByDate(String text,
                                                             List<Long> categories,
                                                             LocalDateTime start,
                                                             LocalDateTime end,
                                                             Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.category.id in ?2 " +
            "and e.paid = true " +
            "and e.eventDate between ?3 and ?4 " +
            "order by e.eventDate desc")
    List<Event> getEventsTextAndCategoriesAndPaidByDate(String text,
                                                        List<Long> categories,
                                                        LocalDateTime start,
                                                        LocalDateTime end,
                                                        Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.category.id in ?2 " +
            "and e.eventDate between ?3 and ?4 " +
            "order by e.eventDate desc")
    List<Event> getEventsTextAndCategoriesByDate(String text,
                                                 List<Long> categories,
                                                 LocalDateTime start,
                                                 LocalDateTime end,
                                                 Pageable pageable);

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

    @Query("select e from Event e " +
            "where e.id in ?1")
    List<Event> findAll(List<Long> eventsIdList);
}

