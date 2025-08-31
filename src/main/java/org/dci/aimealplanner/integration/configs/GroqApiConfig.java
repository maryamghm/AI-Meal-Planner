package org.dci.aimealplanner.integration.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class GroqApiConfig {
    @Value("${groq.base.url}")
    private String baseUrl;

    @Value("${groq.api.key}")
    private String apiKey;

    @Bean
    public RestClient groqRestClient(RestClient.Builder builder) {
        return builder
                .baseUrl(baseUrl)
                .defaultHeaders(headers -> {
                    headers.setBearerAuth(apiKey);
                    headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
                    headers.setAccept(java.util.List.of(org.springframework.http.MediaType.APPLICATION_JSON));
                })
                .build();
    }
}
