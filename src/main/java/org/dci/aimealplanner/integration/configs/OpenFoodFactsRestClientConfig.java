package org.dci.aimealplanner.integration.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class OpenFoodFactsRestClientConfig {

    @Value("${off.base.url}")
    private String offBaseUrl;

    @Bean
    public RestClient offRestClient(RestClient.Builder builder) {
        return builder
                .baseUrl(offBaseUrl)
                .defaultHeader("Accept", "application/json")
                .build();
    }
}
