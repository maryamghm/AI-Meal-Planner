package org.dci.aimealplanner.models.recipes;

import jakarta.validation.constraints.*;
import org.dci.aimealplanner.entities.ImageMetaData;
import org.dci.aimealplanner.entities.recipes.MealCategory;
import org.dci.aimealplanner.entities.recipes.Recipe;
import org.dci.aimealplanner.entities.recipes.RecipeIngredient;
import org.dci.aimealplanner.models.Difficulty;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public record UpdateRecipeDTO(
        Long id,
        @NotBlank(message = "Title cannot be blank.")
        String title,

        @NotNull
        Difficulty difficulty,

        @Positive @NotNull
        @Min(1)
        Integer preparationTimeMinutes,

        @Positive @NotNull
        @DecimalMin(value = "0.1")
        BigDecimal servings,

        ImageMetaData image,

        @NotNull
        String instructions,

        @NotNull
        List<RecipeIngredient> ingredients,

        @NotNull
        Set<MealCategory> mealCategories
) {

    public static UpdateRecipeDTO from(Recipe recipe) {
        return new UpdateRecipeDTO(recipe.getId(), recipe.getTitle(), recipe.getDifficulty(), recipe.getPreparationTimeMinutes(), recipe.getServings(),
                recipe.getImage(), recipe.getInstructions(), recipe.getIngredients(), recipe.getMealCategories());
    }
}
