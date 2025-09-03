package org.dci.aimealplanner.entities.ingredients;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity(name = "ingredients")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotBlank(message = "Ingredient's name cannot be blank")
    @Size(min = 2, max = 100, message = "Ingredients's name must be between 2 and 100 characters")
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id")
    private IngredientCategory category;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "nutrition_fact_id")
    private NutritionFact nutritionFact;

    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IngredientUnitRatio> unitRatios;

}
