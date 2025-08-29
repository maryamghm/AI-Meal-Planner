package org.dci.aimealplanner.integration.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class FoodAPIRestClientConfig {

    @Value("${fdc.base.url}")
    private String fdcBaseUrl;

    @Value("${fdc.api.key}")
    private String fdcApiKey;

    @Bean
    public RestClient fdcRestClient(RestClient.Builder builder) {
        return builder
                .baseUrl(fdcBaseUrl)
                .defaultHeader("Accept", "application/json")
                .defaultHeader("X-Api-Key", fdcApiKey)
                .build();
    }
}
