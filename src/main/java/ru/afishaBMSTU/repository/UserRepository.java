package ru.afishaBMSTU.repository;

import ru.afishaBMSTU.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u " +
            "where u.id in ?1")
    Page<User> findAllByIdsPageable(List<Long> ids, Pageable pageable);

}
