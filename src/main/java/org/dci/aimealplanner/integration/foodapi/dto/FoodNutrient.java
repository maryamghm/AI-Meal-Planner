package org.dci.aimealplanner.integration.foodapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.dci.aimealplanner.entities.ingredients.NutritionFact;


@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FoodNutrient {
    private String nutrientName;
    private String unitName;
    private Double value;


}
