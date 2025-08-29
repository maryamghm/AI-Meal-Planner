package org.dci.aimealplanner.repositories.ingredients;

import org.dci.aimealplanner.entities.ingredients.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    Optional<Ingredient> findByName(String name);

    Optional<Ingredient> findByNameIgnoreCase(String name);
}
