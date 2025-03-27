package ru.afishaBMSTU.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.afishaBMSTU.model.comment.Comment;
import ru.afishaBMSTU.model.event.Event;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> getCommentsByAuthorExternalId(UUID authorExternalId, Pageable pageable);

    List<Comment> getCommentsByEvent(Event event, Pageable pageable);
}
