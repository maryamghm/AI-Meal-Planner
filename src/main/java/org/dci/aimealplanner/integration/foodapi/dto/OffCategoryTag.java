package org.dci.aimealplanner.integration.foodapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.dci.aimealplanner.entities.ingredients.IngredientCategory;
import org.dci.aimealplanner.entities.recipes.MealCategory;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OffCategoryTag {
    private String name;

    public MealCategory toMealCategory() {
        MealCategory mealCategory = new MealCategory();
        mealCategory.setName(name);

        return mealCategory;
    }
}
