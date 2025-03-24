package ru.afishaBMSTU.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.afishaBMSTU.model.user.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
