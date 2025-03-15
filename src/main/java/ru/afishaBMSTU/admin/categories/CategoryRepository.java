package ru.afishaBMSTU.admin.categories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.afishaBMSTU.admin.categories.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
