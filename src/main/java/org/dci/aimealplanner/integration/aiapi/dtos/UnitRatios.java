package org.dci.aimealplanner.integration.aiapi.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnitRatios {
    @JsonProperty("unit_code")
    private String unitCode;

    @JsonProperty("grams_per_unit")
    private String gramsPerUnit;
}
