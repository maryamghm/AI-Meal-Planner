package org.dci.aimealplanner.entities.recipes;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dci.aimealplanner.entities.ingredients.Ingredient;
import org.dci.aimealplanner.entities.ingredients.Unit;

import java.math.BigDecimal;

@Entity(name = "recipe_ingredients")
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
public class RecipeIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;


    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;


    @Column(nullable = false, precision = 12, scale = 3)
    @DecimalMin("0.001")
    private BigDecimal amount;


    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private Unit unit;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof RecipeIngredient that)) return false;

        return id.equals(that.id) && recipe.getId().equals(that.recipe.getId()) && ingredient.getId().equals(that.ingredient.getId()) && amount.equals(that.amount) && unit.getId().equals(that.unit.getId());
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + recipe.getId().hashCode();
        result = 31 * result + ingredient.getId().hashCode();
        result = 31 * result + amount.hashCode();
        result = 31 * result + unit.getId().hashCode();
        return result;
    }
}
