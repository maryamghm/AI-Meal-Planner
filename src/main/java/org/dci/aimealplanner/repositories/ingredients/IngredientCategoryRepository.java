package org.dci.aimealplanner.repositories.ingredients;

import org.dci.aimealplanner.entities.ingredients.IngredientCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.OptionalDouble;

@Repository
public interface IngredientCategoryRepository extends JpaRepository<IngredientCategory, Long> {
    Optional<IngredientCategory> findByNameIgnoreCase(String canonical);
}
