package org.dci.aimealplanner.entities.recipes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "meal_category")
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
