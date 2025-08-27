package org.dci.aimealplanner.repositories.ingredients;

import org.dci.aimealplanner.entities.ingredients.IngredientCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientCategoryRepository extends JpaRepository<IngredientCategory, Long> {
}
