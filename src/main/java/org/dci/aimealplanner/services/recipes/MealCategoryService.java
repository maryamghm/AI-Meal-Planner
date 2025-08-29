package org.dci.aimealplanner.services.recipes;

import lombok.RequiredArgsConstructor;
import org.dci.aimealplanner.entities.recipes.MealCategory;
import org.dci.aimealplanner.repositories.recipes.MealCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MealCategoryService {
    private final MealCategoryRepository mealCategoryRepository;

    public void addAll(List<MealCategory> mealCategories) {
        mealCategoryRepository.saveAll(mealCategories);
    }
}
