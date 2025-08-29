package org.dci.aimealplanner.bootstrap.seeding;

import lombok.RequiredArgsConstructor;
import org.dci.aimealplanner.integration.foodapi.FoodApiClient;
import org.dci.aimealplanner.integration.foodapi.OpenFoodFactsClient;
import org.dci.aimealplanner.integration.foodapi.dto.FoodItem;
import org.dci.aimealplanner.services.ingredients.IngredientCategoryService;
import org.dci.aimealplanner.services.ingredients.IngredientService;
import org.dci.aimealplanner.services.recipes.MealCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class IngredientSeeder implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(IngredientSeeder.class);
    private final FoodApiClient foodApiClient;
    private final OpenFoodFactsClient openFoodFactsClient;
    private final IngredientService ingredientService;
    private final IngredientCategoryService ingredientCategoryService;
    private final MealCategoryService mealCategoryService;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        //seedIngredients();
        // seedMealCategory();
    }

    private void seedIngredients() {
        List<String> seeds = IngredientSeedData.commonIngredients();

        int createdOrUpdated = 0;
        int skipped = 0;
        int missing = 0;
        int failed = 0;

        for (String name : seeds) {
            try {
                if (ingredientService.exists(name)) {
                    skipped++;
                    continue;
                }
                Optional<FoodItem> apiIngredientObject = foodApiClient.searchFood(name);
                if (apiIngredientObject.isEmpty()) {
                    missing++;
                    log.warn("No USDA hit for '{}'", name);
                    continue;
                }

                if (!apiIngredientObject.get().allNutritionFactsAvailable()) {
                    skipped++;
                    continue;
                }

                ingredientService.upsertFromUsda(name, apiIngredientObject.get());
                createdOrUpdated++;

            } catch (Exception e) {
                failed++;
                log.warn("Failed seeding '{}': {}", name, e.getMessage());
            }
            log.info("Seeding summary: created/updated={}, skipped={}, missing={}, failed={}",
                    createdOrUpdated, skipped, missing, failed);
        }
    }

    public void seedMealCategory() {
        mealCategoryService.addAll(openFoodFactsClient.getOffCategories().toMealCategories());
    }
}
