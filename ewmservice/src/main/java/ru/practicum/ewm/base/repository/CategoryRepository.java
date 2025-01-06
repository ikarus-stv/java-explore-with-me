package ru.practicum.ewm.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.base.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}