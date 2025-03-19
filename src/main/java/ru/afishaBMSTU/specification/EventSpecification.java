package ru.afishaBMSTU.specification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import ru.afishaBMSTU.dto.event.EventAdminFilterDto;
import ru.afishaBMSTU.dto.event.EventFilterDto;
import ru.afishaBMSTU.model.event.Event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
public class EventSpecification {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Specification<Event> withFilters(EventFilterDto filter, String sort) {
        return Specification.where(textLike(filter.getText()))
                .and(categoriesIn(filter.getCategories()))
                .and(paidEquals(filter.getPaid()))
                .and(rangeStartAfter(filter.getRangeStart()))
                .and(rangeEndBefore(filter.getRangeEnd()))
                .and(onlyAvailable(filter.getOnlyAvailable()))
                .and(orderBy(sort));
    }

    public static Specification<Event> withAdminFilters(EventAdminFilterDto filter) {
        return Specification.where(usersIn(filter.getUsers()))
                .and(categoriesIn(filter.getCategories()))
                .and(statesIn(filter.getStates()))
                .and(rangeStartAfter(filter.getRangeStart()))
                .and(rangeEndBefore(filter.getRangeEnd()));
    }

    private static Specification<Event> textLike(String text) {
        return (root, query, cb) -> {
            if (text == null || text.isEmpty()) {
                return null;
            }
            String searchText = "%" + text.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("annotation")), searchText),
                    cb.like(cb.lower(root.get("description")), searchText)
            );
        };
    }

    private static Specification<Event> categoriesIn(List<Long> categories) {
        return (root, query, cb) -> {
            if (CollectionUtils.isEmpty(categories)) {
                return null;
            }
            return root.get("category").get("id").in(categories);
        };
    }

    private static Specification<Event> statesIn(List<String> states) {
        return (root, query, cb) -> {
            if (CollectionUtils.isEmpty(states)) {
                return null;
            }
            return root.get("state").in(states);
        };
    }

    private static Specification<Event> usersIn(List<Long> users) {
        return (root, query, cb) -> {
            if (CollectionUtils.isEmpty(users)) {
                return null;
            }
            return root.get("initiator").get("id").in(users);
        };
    }

    private static Specification<Event> paidEquals(Boolean paid) {
        return (root, query, cb) -> {
            if (paid == null) {
                return null;
            }
            return cb.equal(root.get("paid"), paid);
        };
    }

    private static Specification<Event> rangeStartAfter(String rangeStart) {
        return (root, query, cb) -> {
            if (rangeStart == null || rangeStart.isEmpty()) {
                return null;
            }
            LocalDateTime startDate = LocalDateTime.parse(rangeStart, FORMATTER);
            return cb.greaterThanOrEqualTo(root.get("eventDate"), startDate);
        };
    }

    private static Specification<Event> rangeEndBefore(String rangeEnd) {
        return (root, query, cb) -> {
            if (rangeEnd == null || rangeEnd.isEmpty()) {
                return null;
            }
            LocalDateTime endDate = LocalDateTime.parse(rangeEnd, FORMATTER);
            return cb.lessThanOrEqualTo(root.get("eventDate"), endDate);
        };
    }

    private static Specification<Event> onlyAvailable(Boolean onlyAvailable) {
        return (root, query, cb) -> {
            if (onlyAvailable == null || !onlyAvailable) {
                return null;
            }
            return cb.greaterThan(root.get("availableSeats"), 0);
        };
    }

    private static Specification<Event> orderBy(String sortBy) {
        return (root, query, cb) -> {
            if (sortBy == null || sortBy.isEmpty()) {
                return null;
            }
            if ("date".equalsIgnoreCase(sortBy)) {
                query.orderBy(cb.desc(root.get("eventDate")));
            } else if ("views".equalsIgnoreCase(sortBy)) {
                query.orderBy(cb.desc(root.get("views")));
            }
            return null;
        };
    }
}
