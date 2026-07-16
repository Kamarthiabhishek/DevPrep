# DevPrep Authentication Context

## Tech Stack
- Java 21
- Spring Boot 3.5.x
- Spring Security 6.x
- Spring Data JPA
- MySQL
- Maven

## Authentication
- JWT Authentication
- Stateless Authentication
- BCrypt Password Encoding
- Role-based authentication
- JWT stored by frontend in browser Local Storage

## User Entity

Fields:
- id
- name
- email (unique)
- password (BCrypt)
- role

## Package Structure

com.devprep.devprepai
├── config
├── controller
├── dto
├── entity
├── repository
├── security
├── service

## Coding Rules

- Use latest Spring Security APIs.
- Use latest JJWT APIs (0.12.x).
- Do not use deprecated methods.
- Use constructor injection only.
- Use DTOs.
- Never expose Entity directly.
- Use Global Exception Handling.
- Follow Controller → Service → Repository architecture.
- Generate production-ready code with comments where necessary.

## Classes Required

- SecurityConfig
- JWTUtil
- JWTAuthenticationFilter
- CustomUserDetailsService
- AuthController
- AuthService
- LoginRequest
- RegisterRequest
- AuthResponse

## APIs

POST /auth/register

POST /auth/login

Everything else must be authenticated.

Generate only what is requested.
Do not create additional entities or modules.