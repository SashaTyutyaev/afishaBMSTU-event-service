package ru.afishaBMSTU.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.afishaBMSTU.model.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u " +
            "where u.id in ?1")
    Page<User> findAllByIdsPageable(List<Long> ids, Pageable pageable);

    Boolean existsByNickname(String nickname);

    Optional<User> findByNickname(String nickname);
}