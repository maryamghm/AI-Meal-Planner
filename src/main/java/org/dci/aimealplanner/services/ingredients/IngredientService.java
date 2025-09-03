package org.dci.aimealplanner.services.ingredients;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.dci.aimealplanner.bootstrap.seeding.IngredientSeedData;
import org.dci.aimealplanner.bootstrap.seeding.IngredientSeeder;
import org.dci.aimealplanner.entities.ingredients.Ingredient;
import org.dci.aimealplanner.entities.ingredients.NutritionFact;
import org.dci.aimealplanner.integration.foodapi.dto.FoodItem;
import org.dci.aimealplanner.repositories.ingredients.IngredientRepository;
import org.dci.aimealplanner.repositories.ingredients.NutritionFactRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepository;
    private final IngredientCategoryService ingredientCategoryService;
    private final NutritionFactRepository nutritionFactRepository;


    public boolean exists(String name) {
        return ingredientRepository.findByName(name).isPresent();
    }

    @Transactional
    public Ingredient upsertFromUsda(String requestedName, FoodItem foodItem) {
        String name = normalize(requestedName);

        Ingredient ingredient = ingredientRepository.findByNameIgnoreCase(name).orElseGet(() -> {
                    Ingredient ing = new Ingredient();
                    ing.setName(name);
                    return ing;
                });


        ingredient.setCategory(ingredientCategoryService.findCategory(foodItem.getFoodCategory(), foodItem.getDescription()));
        NutritionFact nutritionFact = foodItem.toNutritionFact();
        nutritionFactRepository.save(nutritionFact);
        ingredient.setNutritionFact(nutritionFact);

        return ingredientRepository.save(ingredient);
    }

    private String normalize(String s) {
        return s == null ? "" : s.trim().toLowerCase(Locale.ROOT);
    }

    public List<Ingredient> findAll() {
        return  ingredientRepository.findAll();
    }

    public Ingredient findById(Long id) {
        return ingredientRepository.findById(id).orElseThrow(() -> new RuntimeException("Ingredient not found"));
    }
}
