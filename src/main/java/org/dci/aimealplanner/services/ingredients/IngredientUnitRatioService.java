package org.dci.aimealplanner.services.ingredients;

import lombok.RequiredArgsConstructor;
import org.dci.aimealplanner.entities.ingredients.Ingredient;
import org.dci.aimealplanner.entities.ingredients.IngredientUnitRatio;
import org.dci.aimealplanner.repositories.ingredients.IngredientUnitRatioRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientUnitRatioService {
    private final IngredientUnitRatioRepository ingredientUnitRatioRepository;

    public IngredientUnitRatio create(IngredientUnitRatio ingredientUnitRation) {
        return ingredientUnitRatioRepository.save(ingredientUnitRation);
    }

    public List<IngredientUnitRatio> findAll() {
        return ingredientUnitRatioRepository.findAll();
    }

    public boolean rationExists(Ingredient ingredient) {
        return !ingredientUnitRatioRepository.findByIngredient(ingredient).isEmpty();
    }

    public List<IngredientUnitRatio> findByIngredientIdIn(List<Long> ingredientIds) {
        return ingredientUnitRatioRepository.findByIngredientIdIn(ingredientIds);
    }

    public List<IngredientUnitRatio> findByIngredient(Ingredient ingredient) {
        return ingredientUnitRatioRepository.findByIngredient(ingredient);
    }
}
