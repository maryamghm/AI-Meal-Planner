package org.dci.aimealplanner.models.recipes;

import org.dci.aimealplanner.entities.ImageMetaData;
import org.dci.aimealplanner.entities.recipes.MealCategory;
import org.dci.aimealplanner.entities.recipes.Recipe;
import org.dci.aimealplanner.entities.recipes.RecipeIngredient;
import org.dci.aimealplanner.models.Difficulty;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public record RecipeDTO(
        Long id,
        String title,
        Difficulty difficulty,
        Integer preparationTimeMinutes,
        BigDecimal servings,
        ImageMetaData image,
        String instructions,
        List<RecipeIngredient> ingredients,
        Set<MealCategory> mealCategories
) {
    public static RecipeDTO from(Recipe recipe) {
        return new RecipeDTO(recipe.getId(),recipe.getTitle(), recipe.getDifficulty(), recipe.getPreparationTimeMinutes(), recipe.getServings(),
                recipe.getImage(), recipe.getInstructions(), recipe.getIngredients(), recipe.getMealCategories());
    }

    public static RecipeDTO from(UpdateRecipeDTO recipeDTO) {
        return new RecipeDTO(recipeDTO.id(), recipeDTO.title(), recipeDTO.difficulty(), recipeDTO.preparationTimeMinutes(),
                recipeDTO.servings(), recipeDTO.image(), recipeDTO.instructions(), recipeDTO.ingredients(), recipeDTO.mealCategories());
    }
}
