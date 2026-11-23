# monolith-core

`monolith-core` is a Spring Boot backend project that currently brings two small REST APIs together in one application:

- **Blog API** - create, search, update, and delete blog posts, backed by PostgreSQL.
- **Weather API** - fetch current weather from Visual Crossing and cache responses in Redis.

The repository is intended to grow with additional projects. This root README is the project index and shared setup guide; each app has its own guide:

| App | Guide |
| --- | --- |
| Blog API | [docs/blog/README.md](docs/blog/README.md) |
| Weather API | [docs/weather/README.md](docs/weather/README.md) |

## Technology

- Java 25
- Spring Boot 4.1.2 and Gradle
- PostgreSQL with Spring Data JPA and Flyway
- Redis with Spring Cache
- Visual Crossing Weather API

## Run locally

### Prerequisites

- Java 25
- Docker with Docker Compose
- A Visual Crossing API key to use the Weather API

### Start the supporting services

From the repository root, start PostgreSQL and Redis:

```shell
docker compose up -d
```

The Compose file supplies local development defaults. Do not use its database credentials in a production environment.

### Configure the Visual Crossing API key

The default Visual Crossing URL and key are configured in `src/main/resources/application.yaml`. Set `WEATHER_API_KEY` in your environment to override the configured key; do not commit personal API keys to the repository.

### Start the application

On Windows:

```powershell
.\gradlew.bat bootRun
```

On macOS or Linux:

```shell
./gradlew bootRun
```

The APIs are served by the same application at `http://localhost:8080`. PostgreSQL migrations run through Flyway on startup. The Blog API inserts example posts when the posts table is empty.

To run the tests:

```powershell
.\gradlew.bat test
```

## API guides

- [Blog API endpoints, data model, and examples](docs/blog/README.md)
- [Weather API endpoint, caching, configuration, and examples](docs/weather/README.md)

## Project layout

```text
src/main/java/dev/ivanchristianjayfuncion/monolith/
  blog/       Blog API components
  weather/    Weather API components
```

Both modules run inside the same Spring Boot process and share the repository's build and runtime configuration. Add future app-specific documentation under `docs/<app-name>/README.md` and link to it from this page.
