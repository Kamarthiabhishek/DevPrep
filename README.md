# DevPrep

DevPrep is a personal developer workspace API that lets a developer organize their learning journey — grouping topics under categories, tracking each topic's progress status, and attaching notes to individual topics. It exposes a Spring Boot REST backend secured with JWT authentication.

## Features

- User registration and login (JWT-based authentication)
- Password hashing with BCrypt
- Category CRUD (create, list, get by id, update, delete) — scoped per authenticated user
- Topic CRUD (create, update, delete, list) — scoped per category
- Topic status management (`NOT_STARTED`, `IN_PROGRESS`, `COMPLETED`) via a dedicated status endpoint
- Notes CRUD (add, list, update, delete) — scoped per topic
- Request validation (`jakarta.validation` — `@NotBlank` on all write DTOs)
- Centralized exception handling (`@RestControllerAdvice`) with a consistent error response shape
- Swagger / OpenAPI docs (springdoc)

> Role-based authorization is only partially wired: a `Role` enum (`USER`, `ADMIN`) exists on the `User` entity, but every new registration is hard-coded to `USER` and no endpoint currently checks for `ADMIN`. Treat this as scaffolding, not an enforced feature, until role checks are added.

## Tech Stack

- Java 21
- Spring Boot 3.5.0
- Spring Security 6.x
- Spring Data JPA / Hibernate
- JWT via `io.jsonwebtoken` (jjwt) 0.12.3
- MySQL (runtime), H2 (test scope)
- Lombok
- Jakarta Bean Validation (`spring-boot-starter-validation`)
- springdoc-openapi (Swagger UI) 2.8.9
- Maven

## Application Architecture

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

- **Controllers** (`controller/`) handle HTTP mapping and delegate to services; they never touch entities directly.
- **Services** (`service/`) contain business logic, ownership checks (e.g. a category must belong to the authenticated user), and transaction boundaries (`@Transactional` on writes).
- **Repositories** (`repository/`) are Spring Data JPA interfaces.
- **DTOs** (`dto/`) are used for every request and response — entities are never returned directly from a controller.
- **Security** (`security/`) holds the JWT filter, JWT utility, and a custom `UserDetailsService`.
- **Config** (`config/`) holds `SecurityConfig`, `CorsConfig`, and the global exception handler. CORS is currently restricted to a single allowed origin, `http://localhost:4200`.
- **Exceptions** (`exception/`) are custom unchecked exceptions (`InvalidCategoryException`, `InvalidTopicException`, `InvalidStatusException`, `InvalidNotesException`) mapped to `400 Bad Request` by the global handler.

## Entity Relationships

```text
User
 └── Category   (one User has many Categories)
      └── Topic     (one Category has many Topics)
           └── Notes    (one Topic has many Notes)
```

- `User 1 —* Category`: `Category.user` is a `@ManyToOne`; `User.categories` is the inverse `@OneToMany`, cascading all operations.
- `Category 1 —* Topic`: `Topic.category` is a `@ManyToOne`; `Category.learningTopic` is the inverse `@OneToMany`, cascading all operations with orphan removal.
- `Topic 1 —* Notes`: `Notes.topic` is a `@ManyToOne`; `Topic.notes` is the inverse `@OneToMany`, cascading all operations with orphan removal.
- All `@ManyToOne` associations are lazily fetched.

## Authentication & Authorization

```text
Register/Login
      ↓
JWT generated
      ↓
Client sends Bearer token
      ↓
JWT filter validates token
      ↓
SecurityContext
      ↓
Authenticated API access
```

- Registration hashes the password with `BCryptPasswordEncoder` and stores the user with `role = USER`.
- Login authenticates via Spring Security's `AuthenticationManager` and returns a signed JWT.
- `JWTAuthenticationFilter` runs once per request: it reads the `Authorization: Bearer <token>` header, validates the token via `JWTUtil`, loads the user through `CustomUserDetailsService`, and populates the `SecurityContext` if valid.
- Sessions are stateless (`SessionCreationPolicy.STATELESS`) — every request must carry its own token.
- `/api/auth/register`, `/api/auth/login`, and the Swagger endpoints are public; everything under `/api/categories/**` requires authentication.
- Only one role currently exists in practice: `USER`. `ADMIN` is defined on the `Role` enum but not assigned or checked anywhere.

## API Endpoints

### Authentication

| Method | Endpoint | Description | Authentication |
|--------|----------|--------------|-----------------|
| POST | `/api/auth/register` | Register a new user | Public |
| POST | `/api/auth/login` | Authenticate and receive a JWT | Public |

### Categories

| Method | Endpoint | Description | Authentication |
|--------|----------|--------------|-----------------|
| POST | `/api/categories` | Create a category | Required |
| GET | `/api/categories` | List the current user's categories | Required |
| GET | `/api/categories/{id}` | Get a category by id | Required |
| PATCH | `/api/categories/{id}` | Rename a category | Required |
| DELETE | `/api/categories/{id}` | Delete a category | Required |

### Topics

| Method | Endpoint | Description | Authentication |
|--------|----------|--------------|-----------------|
| POST | `/api/categories/{categoryId}/topics` | Add a topic to a category | Required |
| GET | `/api/categories/{categoryId}/topics` | List topics in a category | Required |
| PATCH | `/api/categories/{categoryId}/topics/{topicId}` | Edit a topic's title/description | Required |
| DELETE | `/api/categories/{categoryId}/topics/{topicId}` | Delete a topic | Required |
| PATCH | `/api/categories/{categoryId}/topics/{topicId}/status` | Update topic status (`IN_PROGRESS` / `COMPLETED`) | Required |

### Notes

| Method | Endpoint | Description | Authentication |
|--------|----------|--------------|-----------------|
| POST | `/api/categories/{categoryId}/topics/{topicId}/notes` | Add a note to a topic | Required |
| GET | `/api/categories/{categoryId}/topics/{topicId}/notes` | List notes for a topic | Required |
| PATCH | `/api/categories/{categoryId}/topics/{topicId}/notes/{id}` | Edit a note | Required |
| DELETE | `/api/categories/{categoryId}/topics/{topicId}/notes/{id}` | Delete a note | Required |

## Project Structure

```text
src/main/java/com/devprep/
├── DevPrepApplication.java
├── config/
│   ├── CorsConfig.java
│   ├── GlobalExceptionHandler.java
│   └── SecurityConfig.java
├── controller/
│   ├── AuthController.java
│   ├── CategoryController.java
│   ├── NotesController.java
│   └── TopicController.java
├── dto/
│   ├── AuthResponse.java
│   ├── CategoryRequest.java / CategoryResponse.java
│   ├── ErrorResponse.java
│   ├── LoginRequest.java / RegisterRequest.java
│   ├── NotesRequest.java / NotesResponse.java
│   └── TopicRequest.java / TopicResponse.java / TopicStatusRequest.java
├── entity/
│   ├── User.java
│   ├── Category.java
│   ├── Topic.java
│   └── Notes.java
├── enums/
│   ├── Role.java
│   └── TopicStatus.java
├── exception/
│   ├── InvalidCategoryException.java
│   ├── InvalidTopicException.java
│   ├── InvalidStatusException.java
│   └── InvalidNotesException.java
├── repository/
│   ├── UserRepository.java
│   ├── CategoryRepository.java
│   ├── TopicRepository.java
│   └── NotesRepository.java
├── security/
│   ├── JWTUtil.java
│   ├── JWTAuthenticationFilter.java
│   └── CustomUserDetailsService.java
└── service/
    ├── AuthService.java
    ├── CategoryService.java
    ├── TopicService.java
    └── NotesService.java
```

## Database Configuration

The application connects to MySQL at runtime and uses H2 for tests. Connection details are **not hard-coded** — they're read from environment variables (see below). `spring.jpa.hibernate.ddl-auto=update` is currently set, so schema changes are applied automatically on startup; this is convenient for development but should be replaced with migrations before production use.

## Environment Variables

The application requires the following environment variables at startup:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
JWT_SECRET
```

Optional (defaults to `86400000` ms / 24 hours if not set):

```text
JWT_EXPIRATION_MS
```

Values are never committed to the repository — set these in your shell, an `.env` file (loaded by your process manager), or your IDE's run configuration.

## Running the Project

```bash
git clone https://github.com/Kamarthiabhishek/DevPrep.git
cd DevPrep

# set the required environment variables first, then:
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

The app starts on `http://localhost:8080` by default.

## Swagger API Documentation

Once running, interactive API docs are available at:

```text
http://localhost:8080/swagger-ui/index.html
```

Raw OpenAPI spec:

```text
http://localhost:8080/v3/api-docs
```

## API Usage Example

**1. Register**

```http
POST /api/auth/register
Content-Type: application/json

{
  "name": "Abhi",
  "email": "abhi@example.com",
  "password": "secret123"
}
```

**2. Login**

```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "abhi@example.com",
  "password": "secret123"
}
```

Response includes a bearer token:

```json
{
  "token": "<jwt>",
  "tokenType": "Bearer",
  "email": "abhi@example.com",
  "role": "USER"
}
```

**3. Send the JWT on subsequent requests**

```http
Authorization: Bearer <jwt>
```

**4. Create a category**

```http
POST /api/categories
Authorization: Bearer <jwt>
Content-Type: application/json

{
  "name": "Java"
}
```

**5. Create a topic**

```http
POST /api/categories/{categoryId}/topics
Authorization: Bearer <jwt>
Content-Type: application/json

{
  "title": "Streams API",
  "description": "Java 8 Streams and functional interfaces"
}
```

**6. Update topic status**

```http
PATCH /api/categories/{categoryId}/topics/{topicId}/status
Authorization: Bearer <jwt>
Content-Type: application/json

{
  "status": "IN_PROGRESS"
}
```

**7. Add a note**

```http
POST /api/categories/{categoryId}/topics/{topicId}/notes
Authorization: Bearer <jwt>
Content-Type: application/json

{
  "content": "Streams are lazily evaluated and single-use."
}
```

## Error Handling

All errors are handled centrally by `GlobalExceptionHandler` (`@RestControllerAdvice`) and returned in a consistent shape:

```json
{
  "timestamp": "2026-08-14T10:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "name : Name cannot be blank",
  "path": "/api/categories"
}
```

- `MethodArgumentNotValidException` (failed `@Valid` checks) → `400`, with field-level messages joined into one string.
- `IllegalArgumentException` and the custom domain exceptions (`InvalidCategoryException`, `InvalidTopicException`, `InvalidStatusException`, `InvalidNotesException`) → `400`.
- `BadCredentialsException` / `UsernameNotFoundException` → `401`, with a generic `"Invalid Credentials"` message (the real cause is not leaked).
- Any other unhandled `Exception` → `500`, and currently returns `exception.getMessage()` directly in the response body — worth tightening before production, since this can leak internal error detail.

## Future Improvements

- **Unit/integration testing** — test coverage is minimal today: one test class (`CategoryServiceTest`) exists with a single working test (`shouldAddCategorySuccessfully`) and one incomplete stub (`shouldEditCategorySuccessfully`, which has no act/assert steps yet). Services, the JWT filter/util, and controllers have no test coverage yet.
- Refresh tokens (tokens currently just expire; there's no renewal flow)
- Pagination for list endpoints (categories, topics, notes)
- Search/filtering on topics and notes
- Tightening the generic `500` error handler so it doesn't return raw exception messages
- Deployment setup (containerization, CI/CD, hosted database)
