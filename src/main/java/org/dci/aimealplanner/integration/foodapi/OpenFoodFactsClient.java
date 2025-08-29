package org.dci.aimealplanner.integration.foodapi;

import org.dci.aimealplanner.integration.foodapi.dto.OffCategoriesResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


@Component

public class OpenFoodFactsClient {
    private final RestClient restClient;

    public OpenFoodFactsClient(RestClient offRestClient) {
        this.restClient = offRestClient;
    }

    public OffCategoriesResponse getOffCategories() {
        return restClient.get()
                .uri("/facets/categories.json")
                .retrieve()
                .body(OffCategoriesResponse.class);
    }
}
