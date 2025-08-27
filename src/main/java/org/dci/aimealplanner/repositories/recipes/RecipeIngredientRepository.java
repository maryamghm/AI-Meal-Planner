package org.dci.aimealplanner.repositories.recipes;

import org.dci.aimealplanner.entities.recipes.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {
}
