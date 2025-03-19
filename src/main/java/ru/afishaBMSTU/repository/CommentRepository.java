package ru.afishaBMSTU.repository;

import ru.afishaBMSTU.model.user.User;
import ru.afishaBMSTU.model.comment.Comment;
import ru.afishaBMSTU.model.event.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> getCommentsByUser(User user, Pageable pageable);

    List<Comment> getCommentsByEvent(Event event, Pageable pageable);
}
