package dev.ivanchristianjayfuncion.monolith.weather.exception;

public class CityNotFoundException extends RuntimeException {
    // custom runtime exception if invalid or if city is not searchable that user passes

    public CityNotFoundException(String message) {
        super(message);
    }
}
