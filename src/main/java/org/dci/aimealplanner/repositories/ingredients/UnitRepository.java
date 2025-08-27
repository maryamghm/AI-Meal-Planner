package org.dci.aimealplanner.repositories.ingredients;

import org.dci.aimealplanner.entities.ingredients.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitRepository extends JpaRepository<Ingredient, Long> {
}
