# Songify 🎵

REST API aplikacji muzycznej zbudowane w oparciu o **Domain-Driven Design** i architekturę **Modular Monolith**.

## O projekcie

Songify to backendowa aplikacja umożliwiająca zarządzanie biblioteką muzyczną — piosenkami, artystami, albumami i gatunkami.
Projekt powstał jako aplikacja portfolio, demonstrująca znajomość wzorców architektonicznych, Spring Security oraz testowania jednostkowego i integracyjnego.

## Funkcjonalności

- Pełny CRUD dla piosenek, artystów, albumów i gatunków
- Rejestracja i logowanie użytkownika (JWT w cookie)
- Logowanie przez Google (OAuth2 / OIDC)
- Kontrola dostępu oparta na rolach (`ROLE_USER`, `ROLE_ADMIN`)
- Migracje bazy danych (Flyway)
- Dokumentacja API (Swagger UI)

## Tech Stack

| Warstwa | Technologia |
|---------|-------------|
| Język | Java 17 |
| Framework | Spring Boot 4.0 |
| Bezpieczeństwo | Spring Security, JWT (Auth0), OAuth2/OIDC |
| Persystencja | Spring Data JPA, Hibernate, PostgreSQL |
| Migracje DB | Flyway |
| Walidacja | Jakarta Validation, Hibernate Validator |
| Widoki | Thymeleaf |
| Dokumentacja | Springdoc OpenAPI / Swagger UI |
| Narzędzie budowania | Maven |
| Inne | Lombok, Spring Actuator |

## Testy

| Typ | Narzędzia |
|-----|-----------|
| Jednostkowe | JUnit 5, AssertJ (bez kontekstu Spring) |
| Integracyjne | MockMvc, Testcontainers (PostgreSQL) |

## Architektura

Projekt stosuje **Domain-Driven Design** z podziałem na warstwy `domain` i `infrastructure`.

```
src/main/java/com/songify/
├── domain/
│   ├── crud/        # logika biznesowa, encje, repozytoria (package-private)
│   ├── security/    # JWT, SecurityUser
│   └── usercrud/    # rejestracja, logowanie
└── infrastructure/
    ├── crud/        # kontrolery REST, obsługa błędów
    ├── security/    # konfiguracja Spring Security
    └── usercrud/    # kontrolery użytkownika
```

Dostęp do domeny z zewnątrz odbywa się wyłącznie przez `SongifyCrudFacade`.

## Uruchomienie

**Wymagania:** Docker (do uruchomienia PostgreSQL)

```bash
# Sklonuj repozytorium
git clone https://github.com/mrsnou/songify.git

# Uruchom bazę danych
docker run --name songify-db -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:16-alpine

# Uruchom aplikację
./mvnw spring-boot:run
```

Swagger UI dostępny pod: `http://localhost:8080/swagger-ui/index.html`

## Konfiguracja

Domyślni użytkownicy z migracji Flyway:

| Login | Hasło | Rola |
|-------|-------|------|
| `user` | `user` | ROLE_USER |
| `admin` | `admin` | ROLE_USER, ROLE_ADMIN |

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=postgres

# Google OAuth2 (opcjonalne)
GoogleOAuth2.client-id=TWOJ_CLIENT_ID
GoogleOAuth2.client-secret=TWOJ_CLIENT_SECRET

# JWT
jwt.expiration.seconds=3600
```
