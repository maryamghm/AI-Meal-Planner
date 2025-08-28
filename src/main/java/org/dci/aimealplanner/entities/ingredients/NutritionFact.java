package org.dci.aimealplanner.entities.ingredients;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "nutrition_fats")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NutritionFact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PositiveOrZero
    private Double kcal;

    @PositiveOrZero
    private Double protein;

    @PositiveOrZero
    private Double carbs;

    @PositiveOrZero
    private Double fat;

    @PositiveOrZero
    private Double fiber;

    @PositiveOrZero
    private Double sugar;
}
