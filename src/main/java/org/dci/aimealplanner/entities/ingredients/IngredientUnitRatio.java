package org.dci.aimealplanner.entities.ingredients;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ingredient_unit_ratio")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class IngredientUnitRatio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private Unit unit;


    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    @Column(nullable = false)
    private Double ratio;
}
