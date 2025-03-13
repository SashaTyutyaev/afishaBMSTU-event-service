package ru.afishaBMSTU.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import ru.afishaBMSTU.events.dto.EventFilterDto;
import ru.afishaBMSTU.users.events.model.Event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
public class EventSpecification {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Specification<Event> withFilters(EventFilterDto filter, String sort) {
        log.info("Getting events with filters {}", filter.toString());
        return Specification.where(textLike(filter.getText()))
                .and(categoriesIn(filter.getCategories()))
                .and(paidEquals(filter.getPaid()))
                .and(rangeStartAfter(filter.getRangeStart()))
                .and(rangeEndBefore(filter.getRangeEnd()))
                .and(onlyAvailable(filter.getOnlyAvailable()))
                .and(orderBy(sort));
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
