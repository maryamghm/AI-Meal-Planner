package org.dci.aimealplanner.integration.foodapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.dci.aimealplanner.entities.ingredients.NutritionFact;

import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FoodItem {
    private long fdcId;
    private String description;
    private String foodCategory;
    private List<FoodNutrient> foodNutrients;

    private final int NUTRITION_FACT_COUNT = 6;

    private static final List<String> ENERGY_KEYS = List.of("energy");
    private static final List<String> PROTEIN_KEYS = List.of("protein");
    private static final List<String> FAT_KEYS = List.of("total lipid", "fat");
    private static final List<String> CARB_KEYS = List.of("carbohydrate, by difference", "carbohydrate");
    private static final List<String> FIBER_KEYS = List.of("fiber, total dietary", "fiber");
    private static final List<String> SUGAR_KEYS = List.of("sugars, total", "total sugars", "sugar");

    public NutritionFact toNutritionFact() {
        NutritionFact nutritionFact = new NutritionFact();
        if (foodNutrients == null) return nutritionFact;

        Map<Key, Double> values = new EnumMap<>(Key.class);

        for (FoodNutrient nutrient : foodNutrients) {
            if (nutrient.getValue() == null) continue;
            Key key = classify(nutrient);
            if (key == null) continue;

            if (key == Key.ENERGY && nutrient.getUnitName() != null) {
                String unit = nutrient.getUnitName().trim().toLowerCase(Locale.ROOT);
                if (!(unit.contains("kcal") || unit.equals("kcal"))) {

                    continue;
                }
            }
            values.putIfAbsent(key, nutrient.getValue());
        }

        nutritionFact.setKcal(values.get(Key.ENERGY));
        nutritionFact.setProtein(values.get(Key.PROTEIN));
        nutritionFact.setCarbs(values.get(Key.CARBS));
        nutritionFact.setFat(values.get(Key.FAT));
        nutritionFact.setFiber(values.get(Key.FIBER));
        nutritionFact.setSugar(values.get(Key.SUGAR));
        return nutritionFact;
    }

    public boolean allNutritionFactsAvailable() {
        if (foodNutrients == null || foodNutrients.isEmpty()) return false;

        EnumSet<Key> found = EnumSet.noneOf(Key.class);
        for (FoodNutrient nutrient : foodNutrients) {
            Key key = classify(nutrient);
            if (key != null) found.add(key);
            if (found.size() == NUTRITION_FACT_COUNT) return true;
        }
        return false;
    }

    private enum Key { ENERGY, PROTEIN, FAT, CARBS, FIBER, SUGAR }

    private Key classify(FoodNutrient nutrient) {
        String name = makeLower(nutrient.getNutrientName());

        if (name.isEmpty()) return null;

        if (containsAny(name, ENERGY_KEYS)) return Key.ENERGY;
        if (containsAny(name, PROTEIN_KEYS)) return Key.PROTEIN;
        if (containsAny(name, FAT_KEYS)) return Key.FAT;
        if (containsAny(name, CARB_KEYS)) return Key.CARBS;
        if (containsAny(name, FIBER_KEYS)) return Key.FIBER;
        if (containsAny(name, SUGAR_KEYS)) return Key.SUGAR;
        return null;
    }

    private static boolean containsAny(String name, List<String> nutritionKeys) {
        for (String key : nutritionKeys) {
            if (name.contains(key)) return true;
        }
        return false;
    }

    private static String makeLower(String s) {
        return s == null ? "" : s.trim().toLowerCase();
    }
}
