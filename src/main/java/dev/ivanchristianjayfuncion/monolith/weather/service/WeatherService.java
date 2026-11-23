package dev.ivanchristianjayfuncion.monolith.weather.service;

import dev.ivanchristianjayfuncion.monolith.weather.dto.VisualCrossingResponse;
import dev.ivanchristianjayfuncion.monolith.weather.dto.WeatherResponseDto;
import dev.ivanchristianjayfuncion.monolith.weather.exception.CityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class WeatherService {

    private static final Logger log = LoggerFactory.getLogger(WeatherService.class);
    private final RestClient weatherRestClient;
    private final String apiKey;

    public WeatherService(
            RestClient weatherRestClient,
            @Value("${weather.api.key}") String apiKey) {
        this.weatherRestClient = weatherRestClient;
        this.apiKey = apiKey;
    }

    @Cacheable(value = "weather", key = "#city.trim().toLowerCase()")
    public WeatherResponseDto getWeatherByCity(String city) {
        log.info("Cache MISS: Fetching live weather data from Visual Crossing for city: {}", city);

        VisualCrossingResponse rawResponse = weatherRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/{city}")
                        .queryParam("unitGroup", "metric")
                        .queryParam("include", "current")
                        .queryParam("key", apiKey)
                        .queryParam("contentType", "json")
                        .build(city))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new CityNotFoundException("City not found or invalid location: " + city);
                })
                .body(VisualCrossingResponse.class);

        if (rawResponse == null || rawResponse.currentConditions() == null) {
            throw new CityNotFoundException("No weather data returned for: " + city);
        }

        return new WeatherResponseDto(
                rawResponse.resolvedAddress(),
                rawResponse.timezone(),
                rawResponse.currentConditions().temp(),
                rawResponse.currentConditions().humidity(),
                rawResponse.currentConditions().windspeed(),
                rawResponse.currentConditions().conditions(),
                rawResponse.description()
        );
    }
}