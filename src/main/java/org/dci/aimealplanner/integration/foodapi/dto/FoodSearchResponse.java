package org.dci.aimealplanner.integration.foodapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FoodSearchResponse {
    private List<FoodItem> foods;
}
