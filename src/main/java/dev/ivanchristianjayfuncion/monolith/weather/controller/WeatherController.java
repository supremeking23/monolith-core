package dev.ivanchristianjayfuncion.monolith.weather.controller;

import dev.ivanchristianjayfuncion.monolith.weather.dto.WeatherResponseDto;
import dev.ivanchristianjayfuncion.monolith.weather.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/weather")
public class WeatherController {
    // RedisCacheCOnfig (TTL setup) & RestClientConfig
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping
    public ResponseEntity<WeatherResponseDto> getWeather(@RequestParam String city) {
        return ResponseEntity.ok(weatherService.getWeatherByCity(city));
    }

}
