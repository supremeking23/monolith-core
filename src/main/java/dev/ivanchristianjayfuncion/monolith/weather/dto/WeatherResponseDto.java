package dev.ivanchristianjayfuncion.monolith.weather.dto;

import java.io.Serializable;

public record WeatherResponseDto (
    // External API raw response DTOs + clean output DTO
    // Raw visual crossing DTO vs Clean passthrough DTO
    String location,
    String timezone,
    Double temepratureCelsius,
    Double humidity,
    Double windSpeedKmh,
    String condition,
    String summary
) implements Serializable {}
