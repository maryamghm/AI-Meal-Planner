package org.dci.aimealplanner.integration.foodapi;

import org.dci.aimealplanner.integration.foodapi.dto.FoodItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "/.env")
class FoodApiClientTest {

    @Autowired
    private FoodApiClient foodApiClient;
    @Test
    void getFood() {
        Optional<FoodItem> walnuts = foodApiClient.searchFood("Walnuts");
        assertTrue(walnuts.isPresent());
        System.out.println(walnuts.get());
    }

}