# Blog API

The Blog API is the PostgreSQL-backed part of `monolith-core`. It supports post creation, retrieval, term-based search, update, and deletion.

## Requirements and startup

Follow the [root setup guide](../../README.md#run-locally) to install prerequisites, start PostgreSQL, and run the shared Spring Boot application. The Blog API uses the `monolith` database configured by the root Docker Compose file.

Flyway creates the `posts` table from `src/main/resources/db/migration/V1__create_posts_table.sql`. On startup, the app adds two example posts if the table has no records.

## Base URL

```text
http://localhost:8080/api/v1/posts
```

## Endpoints

| Method | Path | Description |
| --- | --- | --- |
| `GET` | `/api/v1/posts` | List posts. Optionally filter with `?term=...`. |
| `GET` | `/api/v1/posts/{id}` | Get one post; returns `404` when it does not exist. |
| `POST` | `/api/v1/posts` | Create a post; returns `201 Created`. |
| `PUT` | `/api/v1/posts/{id}` | Update a post; returns `404` when it does not exist. |
| `DELETE` | `/api/v1/posts/{id}` | Delete a post; returns `204 No Content` or `404`. |

Search is case-insensitive and checks the title, content, and category. It does not search tags.

## Post fields

The create and update request body requires all of these fields:

| Field | Type | Constraints |
| --- | --- | --- |
| `title` | string | Required; up to 255 characters |
| `content` | string | Required |
| `category` | string | Required; up to 100 characters |
| `tags` | array of strings | Required; at least one tag |

Example request:

```json
{
  "title": "A first post",
  "content": "Notes about building a small backend.",
  "category": "Engineering",
  "tags": ["java", "spring"]
}
```

Example calls using `curl.exe`:

```powershell
curl.exe "http://localhost:8080/api/v1/posts"
curl.exe "http://localhost:8080/api/v1/posts?term=backend"
curl.exe -X POST "http://localhost:8080/api/v1/posts" `
  -H "Content-Type: application/json" `
  -d '{"title":"A first post","content":"Notes about building a small backend.","category":"Engineering","tags":["java","spring"]}'
```

## Structure and request flow

The Blog API uses a layered structure to separate HTTP handling, business operations, persistence, and data types. A typical request flows through these layers:

```text
HTTP request -> Controller -> Service -> Repository -> PostgreSQL
HTTP response <- Controller <- Service <- Repository <- PostgreSQL
```

The controller validates the incoming DTO, the service applies the post operation, and the repository reads or writes the entity through Spring Data JPA. This is a practical example of layered architecture and separation of concerns; it is not intended to claim a strict Clean Architecture implementation.

The packages are organized as follows:

- `controller` exposes the REST endpoints.
- `service` contains post operations.
- `repository` uses Spring Data JPA and defines the search query.
- `entity` maps posts to the database.
- `dto` validates incoming requests.
- `config` seeds example data for an empty database.

Post timestamps are set on insert and update. Tags are stored as a PostgreSQL text array.
