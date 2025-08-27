package org.dci.aimealplanner.entities.recipes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "recipe_ingredients",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_recipe_ingredient_unit",
                columnNames = {"recipe_id", "ingredient_id", "unit_id"}
        )
)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MealCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
}
