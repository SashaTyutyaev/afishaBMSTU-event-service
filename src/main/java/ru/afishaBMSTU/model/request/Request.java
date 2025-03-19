package ru.afishaBMSTU.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import ru.afishaBMSTU.model.user.User;
import ru.afishaBMSTU.model.event.Event;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "requests")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    private LocalDateTime created;
}
