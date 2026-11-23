# Weather API

The Weather API retrieves current conditions from [Visual Crossing](https://www.visualcrossing.com/weather-api) and caches successful responses in Redis.

## Requirements and startup

Follow the [root setup guide](../../README.md#run-locally) to install prerequisites, start PostgreSQL and Redis, and run the shared Spring Boot application. Redis is used by the weather cache; PostgreSQL is used by the Blog API.

The Visual Crossing base URL and API key are configured in `src/main/resources/application.yaml`. Override the key with the `WEATHER_API_KEY` environment variable rather than committing a personal key. The URL can likewise be overridden with `WEATHER_API_BASE_URL`.

## Endpoint

```http
GET http://localhost:8080/api/v1/weather?city={city}
```

The `city` query parameter is required. The request to Visual Crossing uses metric units and asks for current conditions.

Example:

```powershell
curl.exe "http://localhost:8080/api/v1/weather?city=Manila"
```

The successful response contains the resolved location, timezone, temperature, humidity, wind speed, condition, and summary. The current JSON property names are `location`, `timezone`, `temepratureCelsius`, `humidity`, `windSpeedKmh`, `condition`, and `summary`. Note that `temepratureCelsius` is spelled that way in the current response DTO.

## Structure and request flow

The Weather API uses a layered structure to separate HTTP handling, weather lookup, external API access, and response mapping. A request flows through the cache and provider as follows:

```text
HTTP request -> WeatherController -> WeatherService -> Redis cache
                                                    | cache hit -> response
                                                    | cache miss -> RestClient -> Visual Crossing
                                                                                 -> response DTO -> Redis cache -> response
```

`WeatherController` accepts the city parameter. `WeatherService` checks the cache and, on a miss, calls Visual Crossing through the configured `RestClient`, then maps the provider response to `WeatherResponseDto`. Configuration classes set up the client and Redis cache; the exception classes and advice handle city-not-found responses. This is layered architecture and separation of concerns, not a claim of strict Clean Architecture.

## Caching

Responses are cached in Redis under the `weather` cache. The cache key trims the city name and converts it to lowercase, so equivalent city strings share a cache entry. Entries expire after 30 minutes; a cache hit avoids another Visual Crossing request until the entry expires.

## Error behavior

The Weather API returns a `404` Problem Detail when the application raises its city-not-found exception. Currently, Visual Crossing 4xx responses are mapped to that exception, so provider-side client errors (including errors other than an unknown city) may also appear as `404`. Provider server errors do not yet have a dedicated application error response.

## Response-time screenshots

The repository includes screenshots comparing an initial request with a subsequent request:

- [Initial request](../../resources/img/first-response.png)
- [Subsequent request](../../resources/img/second-response.png)

These are illustrative observations from the saved requests; response times vary by network, provider, and cache state.
