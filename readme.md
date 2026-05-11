# Songify 🎵

REST API for a music library application built with **Domain-Driven Design** and **Modular Monolith** architecture.

## About

Songify is a backend application for managing a music library — songs, artists, albums and genres.
Built as a portfolio project showcasing knowledge of architectural patterns, Spring Security, and both unit and integration testing.

## Features

- Full CRUD for songs, artists, albums and genres
- User registration and login with JWT (cookie-based)
- Google social login (OAuth2 / OIDC)
- Role-based access control (`ROLE_USER`, `ROLE_ADMIN`)
- Database migrations (Flyway)
- API documentation (Swagger UI)

## Tech Stack

| Layer | Technology |
|-------|------------|
| Language | Java 17 |
| Framework | Spring Boot 4.0 |
| Security | Spring Security, JWT (Auth0), OAuth2/OIDC |
| Persistence | Spring Data JPA, Hibernate, PostgreSQL |
| DB Migrations | Flyway |
| Validation | Jakarta Validation, Hibernate Validator |
| Views | Thymeleaf |
| Documentation | Springdoc OpenAPI / Swagger UI |
| Build tool | Maven |
| Other | Lombok, Spring Actuator |

## Testing

| Type | Tools |
|------|-------|
| Unit | JUnit 5, AssertJ (no Spring context) |
| Integration | MockMvc, Testcontainers (PostgreSQL) |

## Architecture

The project follows **Domain-Driven Design** with a clear separation between `domain` and `infrastructure` layers.

```
src/main/java/com/songify/
├── domain/
│   ├── crud/        # business logic, entities, repositories (package-private)
│   ├── security/    # JWT, SecurityUser
│   └── usercrud/    # registration, login
└── infrastructure/
    ├── crud/        # REST controllers, error handlers
    ├── security/    # Spring Security configuration
    └── usercrud/    # user controllers
```

External access to the domain is only possible through `SongifyCrudFacade`.

## Getting Started

**Requirements:** Java 17, Maven, Docker, OpenSSL, Java `keytool`

**Step 1 — Clone the repository**
```bash
git clone https://github.com/mrsnou/songify.git
cd songify
```

**Step 2 — Generate SSL certificate** (place in `src/main/resources/`)
```bash
keytool -genkeypair -alias songify -keyalg RSA -keysize 2048 \
  -storetype PKCS12 -keystore src/main/resources/certificate.p12 -validity 365
```

**Step 3 — Generate RSA key pair** (place in project root)
```bash
openssl genrsa -out key.pem 2048
openssl rsa -in key.pem -pubout -out cert.pem
```

**Step 4 — Start the database**
```bash
docker run --name songify-db -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:16-alpine
```

**Step 5 — Run the application**
```bash
./mvnw spring-boot:run
```

Swagger UI available at: `https://localhost:8443/swagger-ui/index.html` (login required - OAuth2 or login and password)


## Configuration

### HTTPS & Required Keys

The application runs on HTTPS (`https://localhost:8443`). Three files are required that are **excluded from the repository** and must be generated manually.

#### 1. SSL Certificate

Generate a PKCS12 certificate and place it in `src/main/resources/`:

```bash
keytool -genkeypair -alias songify -keyalg RSA -keysize 2048 \
  -storetype PKCS12 -keystore certificate.p12 -validity 365
```

#### 2. RSA Key Pair (JWT signing)

Generate a private/public key pair and place both files in the **project root directory**:

```bash
# Generate private key
openssl genrsa -out key.pem 2048

# Extract public key
openssl rsa -in key.pem -pubout -out cert.pem
```

These files are referenced in `application.properties` as:
```properties
jwt.key.public=file:cert.pem
jwt.key.private=file:key.pem
```

Default users seeded via Flyway migration:

| Username | Password | Role |
|----------|----------|------|
| `user` | `user` | ROLE_USER |
| `admin` | `admin` | ROLE_USER, ROLE_ADMIN |

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=postgres

# Google OAuth2 (optional)
GoogleOAuth2.client-id=YOUR_CLIENT_ID
GoogleOAuth2.client-secret=YOUR_CLIENT_SECRET

# JWT
jwt.expiration.seconds=3600
```
