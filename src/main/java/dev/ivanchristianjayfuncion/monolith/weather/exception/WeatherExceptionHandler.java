package dev.ivanchristianjayfuncion.monolith.weather.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "dev.ivanchristianjayfuncion.monolith.weather.controller")
public class WeatherExceptionHandler {
    // @RestControllerAdvice for converting exceptions into clean JSON error response (404, 503)

    @ExceptionHandler(CityNotFoundException.class)
    public ProblemDetail handleCityNotFound(CityNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }
}
