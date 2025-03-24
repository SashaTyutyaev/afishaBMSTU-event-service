package ru.afishaBMSTU.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.afishaBMSTU.model.phone.Phone;

import java.util.Optional;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {

    @Query("select p.user.id from Phone p where p.phone like :number")
    Optional<Long> findUserIdByPhone(@Param("number") String phoneNumber);
}
