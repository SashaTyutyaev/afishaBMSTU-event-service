package ru.afishaBMSTU.users.comments;

import ru.afishaBMSTU.admin.users.model.User;
import ru.afishaBMSTU.users.comments.model.Comment;
import ru.afishaBMSTU.users.events.model.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> getCommentsByUser(User user, Pageable pageable);

    List<Comment> getCommentsByEvent(Event event, Pageable pageable);
}
