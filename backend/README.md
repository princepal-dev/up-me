# UpMe Backend

Spring Boot API for the UpMe platform. The backend handles authentication, monitor management, and ping log persistence.

## Tech Stack

- Java 21
- Spring Boot 4
- Spring Data JPA
- Spring Security
- OAuth2 login support
- JWT authentication
- PostgreSQL
- Lombok

## Prerequisites

- Java 21
- Maven 3.9+ or the included Maven wrapper
- PostgreSQL

## Run Locally

From the backend directory:

```bash
./mvnw spring-boot:run
```

To build and run tests:

```bash
./mvnw clean test
./mvnw clean package
```

## Configuration

Application settings live in `src/main/resources/application.properties`.

Important values include:

- `frontend.url` - frontend origin allowed for local development
- `spring.app.jwtSecret` - JWT signing secret
- `spring.app.jwtExpirationMs` - JWT expiration window
- `upme.user.monitor.max` - maximum monitors per user

If you connect to a local database, add the usual Spring datasource properties in the same file or via environment variables.

## Project Layout

- `controller` - HTTP endpoints
- `service` - business logic
- `repository` - database access
- `model` - JPA entities
- `security` - authentication and authorization
- `request` and `response` - API payloads
- `utils` - shared helpers

## Notes

The API is intended to work with the Next.js frontend in this repository.
