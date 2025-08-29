package org.dci.aimealplanner.integration.foodapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.dci.aimealplanner.entities.recipes.MealCategory;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OffCategoriesResponse {
    private List<OffCategoryTag> tags;

    public List<MealCategory> toMealCategories() {
        List<MealCategory> mealCategories = new ArrayList<>();
        tags.forEach(tag -> {
           mealCategories.add(tag.toMealCategory());
        });
        return mealCategories;
    }
}
