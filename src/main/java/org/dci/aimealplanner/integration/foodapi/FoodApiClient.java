package org.dci.aimealplanner.integration.foodapi;

import org.dci.aimealplanner.integration.foodapi.dto.FoodItem;
import org.dci.aimealplanner.integration.foodapi.dto.FoodSearchResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Component
public class FoodApiClient {
    private final RestClient restClient;

    public FoodApiClient(RestClient fdcRestClient) {
        this.restClient = fdcRestClient;
    }

    public Optional<FoodItem> searchFood(String query) {
        FoodSearchResponse foodSearchResponse = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/foods/search")
                        .queryParam("query", query)
                        .queryParam("pageSize", 1)
                        .build())
                .retrieve()
                .body(FoodSearchResponse.class);

        if (foodSearchResponse == null ||
                foodSearchResponse.getFoods() == null ||
                foodSearchResponse.getFoods().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(foodSearchResponse.getFoods().get(0));
    }

}
