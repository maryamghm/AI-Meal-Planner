package org.dci.aimealplanner.services.recipes;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dci.aimealplanner.entities.ImageMetaData;
import org.dci.aimealplanner.entities.ingredients.Ingredient;
import org.dci.aimealplanner.entities.ingredients.Unit;
import org.dci.aimealplanner.entities.recipes.Recipe;
import org.dci.aimealplanner.entities.recipes.RecipeIngredient;
import org.dci.aimealplanner.repositories.recipes.RecipeIngredientRepository;
import org.dci.aimealplanner.repositories.recipes.RecipeRepository;
import org.dci.aimealplanner.services.cloudinary.CloudinaryService;
import org.dci.aimealplanner.services.ingredients.IngredientService;
import org.dci.aimealplanner.services.ingredients.UnitService;
import org.dci.aimealplanner.services.users.UserService;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final CloudinaryService cloudinaryService;
    private final UserService userService;
    private final UnitService unitService;
    private final IngredientService ingredientService;
    private final RecipeIngredientRepository recipeIngredientRepository;

    public Recipe addNewRecipe(Recipe recipe, MultipartFile imageFile, String email) {
        List<RecipeIngredient> recipeIngredients = normalizeAndResolveIngredients(recipe);
        if (recipeIngredients != null) {
            recipe.getIngredients().clear();
        }
        if (!imageFile.isEmpty()) {
            Map<String, String> uploadedData = cloudinaryService.upload(imageFile);

            ImageMetaData imageMetaData = new ImageMetaData();
            imageMetaData.setImageUrl(uploadedData.get("url"));
            imageMetaData.setPublicId(uploadedData.get("publicId"));
            recipe.setImage(imageMetaData);
        }

        recipe.setAuthorId(userService.findByEmail(email).getId());
        if (recipeIngredients != null) {
            for (RecipeIngredient recipeIngredient : recipeIngredients) {
                recipeIngredient.setRecipe(recipe);
            }
            recipe.setIngredients(recipeIngredients);
        }
        return recipeRepository.save(recipe);
    }

    private List<RecipeIngredient> normalizeAndResolveIngredients(Recipe recipe) {
        if (recipe.getIngredients() == null) return null;

        List<RecipeIngredient> normalizeRecipeIngredients = new ArrayList<>();
        for (RecipeIngredient recipeIngredient : recipe.getIngredients()) {
            boolean noIngredient = (recipeIngredient.getIngredient() == null || recipeIngredient.getIngredient().getId() == null);
            boolean noAmount = (recipeIngredient.getAmount() == null);

            if (!noIngredient && !noAmount) {
                Unit unit = unitService.findById(recipeIngredient.getUnit().getId());
                Ingredient ingredient = ingredientService.findById(recipeIngredient.getIngredient().getId());
                BigDecimal amount = recipeIngredient.getAmount();
                RecipeIngredient newRecipeIngredient = new RecipeIngredient();
                newRecipeIngredient.setIngredient(ingredient);
                newRecipeIngredient.setAmount(amount);
                newRecipeIngredient.setUnit(unit);
                normalizeRecipeIngredients.add(newRecipeIngredient);
            }
        }
        return normalizeRecipeIngredients;
    }

    public Recipe findById(long id) {
        return recipeRepository.findById(id).orElseThrow(() -> new RuntimeException("Recipe with id " + id + " not found"));
    }

//    public Recipe updateRecipe(Long id, Recipe recipe, MultipartFile imageFile, String email) {
//
//    }
}
