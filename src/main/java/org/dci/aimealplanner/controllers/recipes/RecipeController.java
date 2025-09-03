package org.dci.aimealplanner.controllers.recipes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dci.aimealplanner.controllers.auth.AuthUtils;
import org.dci.aimealplanner.entities.ingredients.Ingredient;
import org.dci.aimealplanner.entities.ingredients.IngredientUnitRatio;
import org.dci.aimealplanner.entities.ingredients.Unit;
import org.dci.aimealplanner.entities.recipes.Recipe;
import org.dci.aimealplanner.models.Difficulty;
import org.dci.aimealplanner.services.ingredients.IngredientCategoryService;
import org.dci.aimealplanner.services.ingredients.IngredientService;
import org.dci.aimealplanner.services.ingredients.IngredientUnitRatioService;
import org.dci.aimealplanner.services.ingredients.UnitService;
import org.dci.aimealplanner.services.recipes.MealCategoryService;
import org.dci.aimealplanner.services.recipes.RecipeService;
import org.dci.aimealplanner.services.users.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private final UserService userService;

    @GetMapping("/new")
    public String newRecipe(Authentication authentication,
                            Model model,
                            HttpServletRequest request) {
        String email = AuthUtils.getUserEmail(authentication);
        model.addAttribute("loggedInUser", userService.findByEmail(email));
        model.addAttribute("recipe", new Recipe());
        model.addAttribute("difficulties", Difficulty.values());
        model.addAttribute("categories", mealCategoryService.findAll());
        model.addAttribute("redirectUrl", request.getHeader("Referer"));

        List<Ingredient> ingredientsList = ingredientService.findAll();
        List<Unit> unitList = unitService.findAll();

        Map<Long, List<Long>> allowedUnitsCsv = new HashMap<>();

        for (Ingredient ingredient : ingredientsList) {
            List<IngredientUnitRatio> relatedUnits = ingredientUnitRatioService.findByIngredient(ingredient);
            List<Long> unitIds = relatedUnits.stream().map(ingredientUnitRatio -> ingredientUnitRatio.getUnit().getId()).collect(Collectors.toList());
            allowedUnitsCsv.put(ingredient.getId(), unitIds);
        }

        model.addAttribute("ingredientList", ingredientsList);
        model.addAttribute("unitList", unitList);
        model.addAttribute("allowedUnitsCsv", allowedUnitsCsv);

        return "recipes/recipe_form";
    }

    @PostMapping("/create")
    public String createRecipe(@Valid @ModelAttribute("recipe") Recipe recipe,
                               BindingResult bindingResult,
                               @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                               Authentication authentication,
                               Model model) {
        String email = AuthUtils.getUserEmail(authentication);

        if (bindingResult.hasErrors()) {
            model.addAttribute("loggedInUser", userService.findByEmail(email));
            model.addAttribute("recipe", recipe);
            model.addAttribute("difficulties", Difficulty.values());
            model.addAttribute("unitList", unitService.findAll());
            model.addAttribute("ingredientCategories", ingredientCategoryService.findAll());
            model.addAttribute("ingredientList", ingredientService.findAll());
            model.addAttribute("categories", mealCategoryService.findAll());
            return "recipes/recipe_form";
        }

        //recipeService.create(recipe);
        return "redirect:/recipes";
    }


}
