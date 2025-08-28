package org.dci.aimealplanner.repositories.recipes;

import org.dci.aimealplanner.entities.recipes.MealCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MealCategoryRepository extends JpaRepository<MealCategory, Long> {
    Optional<MealCategory> findByNameIgnoreCase(String name);
}
