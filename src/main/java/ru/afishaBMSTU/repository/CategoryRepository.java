package ru.afishaBMSTU.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.afishaBMSTU.model.category.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
