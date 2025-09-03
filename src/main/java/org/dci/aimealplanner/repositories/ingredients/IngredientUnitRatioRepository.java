package org.dci.aimealplanner.repositories.ingredients;

import org.dci.aimealplanner.entities.ingredients.Ingredient;
import org.dci.aimealplanner.entities.ingredients.IngredientUnitRatio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientUnitRatioRepository extends JpaRepository<IngredientUnitRatio, Long> {

    List<IngredientUnitRatio> findByIngredient(Ingredient ingredient);
    List<IngredientUnitRatio> findByIngredientIdIn(List<Long> ingredientIds);
}
