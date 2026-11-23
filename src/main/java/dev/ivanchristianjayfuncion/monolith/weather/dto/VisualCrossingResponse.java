package dev.ivanchristianjayfuncion.monolith.weather.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record VisualCrossingResponse(
        String resolvedAddress,
        String timezone,
        String description,
        CurrentConditions currentConditions
) {
    // to map raw json from external API
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CurrentConditions(
        Double temp,
        Double humidity,
        Double windspeed,
        String conditions
    ) {}
}
