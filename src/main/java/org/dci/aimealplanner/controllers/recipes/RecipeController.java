package org.dci.aimealplanner.controllers.recipes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dci.aimealplanner.controllers.auth.AuthUtils;
import org.dci.aimealplanner.entities.ingredients.Ingredient;
import org.dci.aimealplanner.entities.ingredients.Unit;
import org.dci.aimealplanner.entities.recipes.MealCategory;
import org.dci.aimealplanner.entities.recipes.Recipe;
import org.dci.aimealplanner.entities.recipes.RecipeIngredient;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


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
        Recipe recipe = new Recipe();
        recipe.setIngredients(new ArrayList<>());
        recipe.setMealCategories(new HashSet<>());
        model.addAttribute("recipe", recipe);
        prepareFormModel(model, email, request.getHeader("Referer"));
        return "recipes/recipe_form";
    }

    @PostMapping("/create")
    public String createRecipe(@Valid @ModelAttribute("recipe") Recipe recipe,
                               BindingResult bindingResult,
                               @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                               Authentication authentication,
                               @RequestParam(value = "redirectUrl", required = false) String redirectUrl,
                               Model model) {
        String email = AuthUtils.getUserEmail(authentication);

        if (bindingResult.hasErrors()) {
            model.addAttribute("recipe", recipe);
            prepareFormModel(model, email, redirectUrl);
            return "recipes/recipe_form";
        }

        recipeService.addNewRecipe(recipe, imageFile, email);
        return "redirect:/home/index";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id,
                               Authentication authentication,
                               HttpServletRequest request,
                               Model model) {
        String email = AuthUtils.getUserEmail(authentication);
        Recipe recipe = recipeService.findById(id);
        model.addAttribute("recipe", recipe);

        prepareFormModel(model, email, request.getHeader("Referer"));

        return "recipes/recipe_form";
    }

    @PostMapping("/update/{id}")
    public String updateRecipe(@PathVariable Long id, @Valid @ModelAttribute Recipe recipe,
                               BindingResult bindingResult,
                               @RequestParam(required = false) MultipartFile imageFile,
                               Authentication authentication,
                               @RequestParam(value = "redirectUrl", required = false) String redirectUrl,
                               Model model) {
        String email = AuthUtils.getUserEmail(authentication);
        if (bindingResult.hasErrors()) {
           prepareFormModel(model, email, redirectUrl);
           return "recipes/recipe_form";
        }

        //Recipe updated = recipeService.updateRecipe(id, recipe, imageFile, email);
        return "redirect:/home/index";
    }

    private void prepareFormModel(Model model,
                                  String userEmail,
                                  String redirectUrl) {
        model.addAttribute("loggedInUser", userService.findByEmail(userEmail));

        model.addAttribute("difficulties", Difficulty.values());
        model.addAttribute("categories", mealCategoryService.findAll());
        model.addAttribute("ingredientList", ingredientService.findAll());
        model.addAttribute("unitList", unitService.findAll());
        model.addAttribute("ingredientCategories", ingredientCategoryService.findAll());
        model.addAttribute("redirectUrl", redirectUrl);
    }



}
