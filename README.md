# Encryption Service

Spring Boot service that provides encryption and password hashing endpoints for the chat platform.

## Prerequisites
- Java 21
- Maven 3.9+
- (Optional) Docker + Docker Compose

## Environment (.env)
Create `encryption-service/.env` with the encryption password:

```
CHAT_APP_ENCRYPTION_PASSWORD=change-me-please
```

This value is required. It is used to derive the AES key for encryption/decryption.

## Run locally (Maven)
From `encryption-service/`:

```
./load-env.ps1
```

That script builds the project, loads `.env`, and starts the API module. If you prefer manual steps:

```
$env:CHAT_APP_ENCRYPTION_PASSWORD='change-me-please'
mvn -pl api -am spring-boot:run
```

The service starts on `http://localhost:8082`.

## Run with Docker
From `encryption-service/`:

```
docker network create chat-net
```

(Only needed once; `docker-compose.yml` expects this external network.) Then:

```
docker compose up --build
```

## Useful endpoints
- Health check: `http://localhost:8082/actuator/health`
- OpenAPI: `http://localhost:8082/v3/api-docs`
- Swagger UI: `http://localhost:8082/swagger-ui`
