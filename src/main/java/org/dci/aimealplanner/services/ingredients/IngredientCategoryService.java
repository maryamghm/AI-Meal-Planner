package org.dci.aimealplanner.services.ingredients;

import lombok.RequiredArgsConstructor;
import org.dci.aimealplanner.entities.ingredients.IngredientCategory;
import org.dci.aimealplanner.repositories.ingredients.IngredientCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class IngredientCategoryService {
    private final IngredientCategoryRepository ingredientCategoryRepository;

    private static final Map<String, String> USDA_TO_CANONICAL = Map.ofEntries(
            Map.entry("Fruits and Fruit Juices", "Fruits"),
            Map.entry("Vegetables and Vegetable Products", "Vegetables"),
            Map.entry("Nut and Seed Products", "Nuts & Seeds"),
            Map.entry("Dairy and Egg Products", "Dairy & Eggs"),
            Map.entry("Beef Products", "Meat"),
            Map.entry("Pork Products", "Meat"),
            Map.entry("Poultry Products", "Meat"),
            Map.entry("Finfish and Shellfish Products", "Fish & Seafood"),
            Map.entry("Cereal Grains and Pasta", "Grains & Cereals"),
            Map.entry("Legumes and Legume Products", "Legumes"),
            Map.entry("Fats and Oils", "Oils & Fats"),
            Map.entry("Spices and Herbs", "Herbs & Spices"),
            Map.entry("Sugars", "Sweeteners"),
            Map.entry("Soups, Sauces, and Gravies", "Sauces & Condiments")
    );

    private static final List<Map.Entry<Pattern, String>> KEYWORDS = List.of(
            Map.entry(Pattern.compile("\\b(walnut|almond|peanut|cashew|pistachio|hazelnut|seed)s?\\b", Pattern.CASE_INSENSITIVE), "Nuts & Seeds"),
            Map.entry(Pattern.compile("\\b(apple|banana|orange|pear|grape|berry|peach|plum|lemon|lime|mango|pineapple)\\b", Pattern.CASE_INSENSITIVE), "Fruits"),
            Map.entry(Pattern.compile("\\b(tomato|onion|garlic|spinach|broccoli|cabbage|carrot|pepper|lettuce|kale|zucchini|cucumber|eggplant)\\b", Pattern.CASE_INSENSITIVE), "Vegetables"),
            Map.entry(Pattern.compile("\\b(milk|yogurt|cheese|butter|cream|egg)s?\\b", Pattern.CASE_INSENSITIVE), "Dairy & Eggs"),
            Map.entry(Pattern.compile("\\b(beef|pork|chicken|turkey|lamb)\\b", Pattern.CASE_INSENSITIVE), "Meat"),
            Map.entry(Pattern.compile("\\b(salmon|tuna|shrimp|cod|trout|sardine)\\b", Pattern.CASE_INSENSITIVE), "Fish & Seafood"),
            Map.entry(Pattern.compile("\\b(rice|wheat|bread|pasta|oat|quinoa|couscous|bulgur)\\b", Pattern.CASE_INSENSITIVE), "Grains & Cereals"),
            Map.entry(Pattern.compile("\\b(lentil|bean|chickpea|kidney bean|black bean|soy)\\b", Pattern.CASE_INSENSITIVE), "Legumes"),
            Map.entry(Pattern.compile("\\b(oil|olive oil|sunflower oil|coconut oil)\\b", Pattern.CASE_INSENSITIVE), "Oils & Fats"),
            Map.entry(Pattern.compile("\\b(basil|parsley|coriander|cumin|paprika|turmeric|cinnamon|thyme|oregano|rosemary|mint|dill)\\b", Pattern.CASE_INSENSITIVE), "Herbs & Spices"),
            Map.entry(Pattern.compile("\\b(sugar|honey|syrup|sweetener)\\b", Pattern.CASE_INSENSITIVE), "Sweeteners"),
            Map.entry(Pattern.compile("\\b(ketchup|mustard|mayonnaise|soy sauce|vinegar|miso|tahini|sriracha)\\b", Pattern.CASE_INSENSITIVE), "Sauces & Condiments")
    );

    public void addAll(List<IngredientCategory> ingredientCategories) {
        ingredientCategoryRepository.saveAll(ingredientCategories);
    }

    public IngredientCategory findCategory(String usdaFoodCategory, String description) {
        if (usdaFoodCategory != null) {
            String canonical = USDA_TO_CANONICAL.get(usdaFoodCategory);
            if (canonical != null) {
                return ingredientCategoryRepository.findByNameIgnoreCase(canonical)
                        .orElseGet(() -> fallbackCategory());
            }
        }

        if (description != null) {
            for (var rule : KEYWORDS) {
                if (rule.getKey().matcher(description).find()) {
                    String canonical = rule.getValue();
                    return ingredientCategoryRepository.findByNameIgnoreCase(canonical)
                            .orElseGet(this::fallbackCategory);
                }
            }
        }

        return fallbackCategory();
    }

    private IngredientCategory fallbackCategory() {
        return ingredientCategoryRepository.findByNameIgnoreCase("Uncategorized")
                .orElseThrow(() -> new IllegalStateException("Default category 'Uncategorized' not found. Seed it first."));
    }

    public List<IngredientCategory> findAll() {
        return ingredientCategoryRepository.findAll();
    }
}
