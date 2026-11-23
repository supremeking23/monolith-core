package dev.ivanchristianjayfuncion.monolith.weather.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    // will create a Bean for Spring RestClient whereit set the base URL of Visual Crossing

    @Bean
    public RestClient weatherRestClient(@Value("${weather.api.base-url}") String baseUrl) {
        return RestClient.builder().baseUrl(baseUrl).build();
    }
}
