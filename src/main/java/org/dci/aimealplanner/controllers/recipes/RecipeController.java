package org.dci.aimealplanner.controllers.recipes;

import lombok.RequiredArgsConstructor;
import org.dci.aimealplanner.entities.recipes.Recipe;
import org.dci.aimealplanner.models.Difficulty;
import org.dci.aimealplanner.services.ingredients.IngredientCategoryService;
import org.dci.aimealplanner.services.ingredients.IngredientService;
import org.dci.aimealplanner.services.ingredients.IngredientUnitRatioService;
import org.dci.aimealplanner.services.ingredients.UnitService;
import org.dci.aimealplanner.services.recipes.MealCategoryService;
import org.dci.aimealplanner.services.recipes.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/recipes")
public class RecipeController {
    private final RecipeService recipeService;
    private final MealCategoryService mealCategoryService;
    private final IngredientService ingredientService;
    private final UnitService  unitService;
    private final IngredientUnitRatioService  ingredientUnitRatioService;
    private final IngredientCategoryService ingredientCategoryService;

    @GetMapping("/new")
    public String newRecipe(Model model) {
        model.addAttribute("recipe", new Recipe());
        model.addAttribute("difficulties", Difficulty.values());
        model.addAttribute("unitList", unitService.findAll());
        model.addAttribute("ingredientCategories", ingredientCategoryService.findAll());
        model.addAttribute("categories", mealCategoryService.findAll());
        return "recipes/recipe_form";
    }
}
