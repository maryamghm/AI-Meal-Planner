package org.dci.aimealplanner.entities.recipes;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dci.aimealplanner.entities.ImageMetaData;
import org.dci.aimealplanner.models.Difficulty;
import org.dci.aimealplanner.models.SourceType;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    @Column(name = "preparation_time_minutes", nullable = false)
    private Integer preparationTimeMinutes;

    @Column(nullable = false, precision = 6, scale = 2)
    @DecimalMin(value = "0.01")
    private BigDecimal servings;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "image_id")
    private ImageMetaData image;

    @Column(columnDefinition = "TEXT")
    private String instructions;

    @Column(name = "kcal_per_serv", precision = 10, scale = 2)
    private BigDecimal kcalPerServ;
    @Column(name = "protein_per_serv", precision = 10, scale = 2)
    private BigDecimal proteinPerServ;
    @Column(name = "carbs_per_serv", precision = 10, scale = 2)
    private BigDecimal carbsPerServ;
    @Column(name = "fat_per_serv", precision = 10, scale = 2)
    private BigDecimal fatPerServ;

    @Enumerated(EnumType.STRING)
    private SourceType sourceType;

    @Column(nullable = true)
    private Long authorId;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredient> ingredients = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "recipes_meal_categories",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "meal_category_id")
    )
    private Set<MealCategory> mealCategories = new HashSet<>();
}
