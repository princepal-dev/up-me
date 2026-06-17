# UpMe

UpMe is a monorepo with a Spring Boot backend and a Next.js frontend.

## Repository Layout

- `backend/` - Java API, authentication, persistence, and business logic
- `frontend/` - Next.js user interface

## Quick Start

### Backend

```bash
cd backend
./mvnw spring-boot:run
```

### Frontend

```bash
cd frontend
npm install
npm run dev
```

## Requirements

- Java 21
- Maven 3.9+ or the included wrapper
- Node.js 20+
- PostgreSQL

## Configuration

Backend configuration lives in `backend/src/main/resources/application.properties`.

Key settings include:

- `frontend.url`
- `spring.app.jwtSecret`
- `spring.app.jwtExpirationMs`
- `upme.user.monitor.max`

## License

This project is licensed under the MIT License.
