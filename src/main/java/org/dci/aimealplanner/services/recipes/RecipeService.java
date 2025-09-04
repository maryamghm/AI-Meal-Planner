package org.dci.aimealplanner.services.recipes;

import lombok.RequiredArgsConstructor;
import org.dci.aimealplanner.entities.ImageMetaData;
import org.dci.aimealplanner.entities.ingredients.Ingredient;
import org.dci.aimealplanner.entities.ingredients.Unit;
import org.dci.aimealplanner.entities.recipes.Recipe;
import org.dci.aimealplanner.entities.recipes.RecipeIngredient;
import org.dci.aimealplanner.models.recipes.UpdateRecipeDTO;
import org.dci.aimealplanner.repositories.recipes.RecipeIngredientRepository;
import org.dci.aimealplanner.repositories.recipes.RecipeRepository;
import org.dci.aimealplanner.services.cloudinary.CloudinaryService;
import org.dci.aimealplanner.services.ingredients.IngredientService;
import org.dci.aimealplanner.services.ingredients.UnitService;
import org.dci.aimealplanner.services.users.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final CloudinaryService cloudinaryService;
    private final UserService userService;
    private final UnitService unitService;
    private final IngredientService ingredientService;
    private final RecipeIngredientRepository recipeIngredientRepository;

    @Transactional
    public Recipe addNewRecipe(UpdateRecipeDTO recipeDTO, MultipartFile imageFile, String email) {
        List<RecipeIngredient> recipeIngredients = normalizeAndResolveIngredients(recipeDTO);
        Recipe recipe = new Recipe();
        updateRecipeFields(recipe, recipeDTO, imageFile, email);

        if (recipeIngredients != null) {
            for (RecipeIngredient recipeIngredient : recipeIngredients) {
                recipeIngredient.setRecipe(recipe);
            }
            recipe.setIngredients(recipeIngredients);
        }

        return recipeRepository.save(recipe);
    }

    @Transactional
    public Recipe updateRecipe(Long id, UpdateRecipeDTO recipeDTO, MultipartFile imageFile, String email) {
        Recipe existingRecipe = recipeRepository.findById(id).orElseThrow();

        Map<Long, RecipeIngredient> existingById = existingRecipe.getIngredients().stream()
                .filter(recipeIngredient -> recipeIngredient.getId() != null)
                .collect(Collectors.toMap(RecipeIngredient::getId, ri -> ri));

        List<RecipeIngredient> target = new ArrayList<>();

        if (recipeDTO.ingredients() != null) {
            for (RecipeIngredient posted : recipeDTO.ingredients()) {
                Long childId = posted.getId();
                Long ingId   = posted.getIngredient() != null ? posted.getIngredient().getId() : null;
                Long unitId  = posted.getUnit() != null ? posted.getUnit().getId() : null;

                if (ingId == null || unitId == null || posted.getAmount() == null) continue;

                Ingredient ing = ingredientService.findById(ingId);
                Unit unit = unitService.findById(unitId);

                RecipeIngredient managedChild;
                if (childId != null) {

                    managedChild = existingById.get(childId);
                    if (managedChild == null) {
                        throw new IllegalArgumentException("Ingredient row not found: id=" + childId);
                    }

                    managedChild.setIngredient(ing);
                    managedChild.setUnit(unit);
                    managedChild.setAmount(posted.getAmount());
                } else {

                    managedChild = new RecipeIngredient();
                    managedChild.setRecipe(existingRecipe);
                    managedChild.setIngredient(ing);
                    managedChild.setUnit(unit);
                    managedChild.setAmount(posted.getAmount());
                }

                target.add(managedChild);
            }
        }

        existingRecipe.getIngredients().clear();
        existingRecipe.getIngredients().addAll(target);


        updateRecipeFields(existingRecipe, recipeDTO, imageFile, email);

        return recipeRepository.save(existingRecipe);
    }


    public Recipe findById(long id) {
        return recipeRepository.findById(id).orElseThrow(() -> new RuntimeException("Recipe with id " + id + " not found"));
    }

    private List<RecipeIngredient> normalizeAndResolveIngredients(UpdateRecipeDTO recipeDTO) {
        if (recipeDTO.ingredients() == null) return null;

        List<RecipeIngredient> normalizeRecipeIngredients = new ArrayList<>();
        List<RecipeIngredient> recipeIngredients = recipeDTO.ingredients();
        for (RecipeIngredient recipeIngredient : recipeIngredients) {
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
                if (recipeIngredient.getId() != null) {
                    newRecipeIngredient.setId(recipeIngredient.getId());
                }
                normalizeRecipeIngredients.add(newRecipeIngredient);
            }
        }
        return normalizeRecipeIngredients;
    }


    private void updateRecipeFields(Recipe recipe, UpdateRecipeDTO recipeDTO, MultipartFile imageFile, String email) {
        recipe.setDifficulty(recipeDTO.difficulty());
        recipe.setPreparationTimeMinutes(recipeDTO.preparationTimeMinutes());
        recipe.setServings(recipeDTO.servings());
        recipe.setTitle(recipeDTO.title());
        recipe.setInstructions(recipeDTO.instructions());
        recipe.setMealCategories(recipeDTO.mealCategories());

        if (!imageFile.isEmpty()) {
            Map<String, String> uploadedData = cloudinaryService.upload(imageFile);

            ImageMetaData imageMetaData = new ImageMetaData();
            imageMetaData.setImageUrl(uploadedData.get("url"));
            imageMetaData.setPublicId(uploadedData.get("publicId"));
            recipe.setImage(imageMetaData);
        }
        recipe.setAuthorId(userService.findByEmail(email).getId());
    }


}
