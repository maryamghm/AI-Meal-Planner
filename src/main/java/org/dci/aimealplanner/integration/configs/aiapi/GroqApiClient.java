package org.dci.aimealplanner.integration.configs.aiapi;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.dci.aimealplanner.integration.configs.aiapi.dtos.AiResponse;
import org.dci.aimealplanner.integration.configs.aiapi.dtos.IngredientUnitFromAI;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GroqApiClient {
    private final RestClient groqRestClient;
    private final ObjectMapper objectMapper;


    public IngredientUnitFromAI getUnitRatiosForIngredient(String ingredientName) throws JsonProcessingException {
        String prompt = buildUnitRatioPrompt(ingredientName);

        Map<String, Object> body = Map.of(
                "model", "llama-3.3-70b-versatile",
                "messages", List.of(
                        Map.of("role", "system", "content", "Return ONLY valid JSON. No code fences or commentary."),
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.2
        );


        AiResponse response = groqRestClient.post()
                .uri("chat/completions")
                .body(body)
                .retrieve()
                .body(AiResponse.class);


        return objectMapper.readValue(response.getChoices().get(0).getMessage().getContent(), IngredientUnitFromAI.class);

    }

    private String buildUnitRatioPrompt(String ingredientName) {
        return """
                You are a culinary data assistant. Return ONLY strict JSON. No code fences or commentary.
                
                Task:
                For the given ingredient, list a few COMMON non-gram units and provide grams-per-ONE-unit.
                
                Rules (keep it simple):
                - Allowed unit_code: "ml","piece","tbsp","tsp","cup".
                - Include only units that make sense (omit the rest).
                - grams_per_unit = grams per 1 unit.
                - No extra fields.
                
                Output (JSON only):
                {
                  "ingredient": "string",
                  "units": [
                    { "unit_code": "ml|piece|tbsp|tsp|cup", "grams_per_unit": number }
                  ]
                }
                
                Ingredient: "%s"
                """.formatted(ingredientName);
    }
}
