# Hexagonal Architecture - Java Project

This is a production-ready example project implementing **hexagonal architecture** (ports and adapters pattern) using **Java** and **Spring Boot** with **PostgreSQL**.

## 📋 Description

Hexagonal architecture advocates for separation of concerns, enhancing code maintainability and testability. This project demonstrates a comprehensive implementation with:

- **Domain Layer**: Pure business logic independent of frameworks
- **Application Layer**: Use cases and business orchestration  
- **Infrastructure Layer**: REST adapters (inbound) and database adapters (outbound)
- **Boot Module**: Spring Boot configuration and dependency injection

## 🏗️ Project Structure

The project is organized into independent Maven modules:

```
hexagonal/
├── domain/                          # Pure business logic
├── application/                     # Use cases & application services
├── infrastructure/
│   ├── inbound/
│   │   └── rest/                   # REST controllers + OpenAPI contracts
│   └── outbound/
│       └── database/               # JPA repositories & entities
└── boot/                           # Spring Boot main application
```

## 🚀 Quick Start

### 1. Start PostgreSQL
```bash
cd src/docker
docker compose up -d
```

### 2. Build & Run
```bash
mvn clean install
cd boot
mvn spring-boot:run
```

Application available at: **http://localhost:8080/api**

## 📚 Modules

### Domain
Contains business entities and the core business logic. This module is independent of any framework.

**Content:**
- Domain entities
- Repository interfaces (ports)
- Domain services

### Application
Implements the application use cases using domain services.

**Content:**
- Application services
- DTOs (Data Transfer Objects)
- Mappers

### Infrastructure

#### Inbound (REST)
Inbound adapters that expose the REST API.

**Features:**
- REST Controllers
- OpenAPI/Swagger documentation with contract-first approach
- Automatic code generation from OpenAPI specifications
- Versioned API contracts

**Available Resources:**
- `/api/users` - User-related operations

#### Outbound (Database)
Outbound adapters for data persistence using **PostgreSQL**.

**Content:**
- JPA Repository implementations
- Entity mapping and data access layer
- Database schema management with Hibernate

### Boot
Boot module that configures the Spring Boot application and includes all necessary dependencies.

**Content:**
- Main application class (`Application.java`)
- Profile-based configuration with REST and Database profiles
- Spring component scanning and auto-configuration

## 🔌 Ports and Adapters

### Ports (Interfaces)
Ports are defined as interfaces that represent contracts between the domain and the outside world.

### Adapters
Adapters are concrete implementations of ports:
- **REST Adapter**: Converts HTTP requests into application commands
- **Database Adapter**: Persists data in PostgreSQL using JPA/Hibernate

## 📖 API Documentation

Once the application is running, access the interactive API documentation:
- **Swagger UI**: http://localhost:8080/api/swagger-ui/index.html
- **OpenAPI JSON**: http://localhost:8080/api/v3/api-docs
- **Redoc**: http://localhost:8080/api/redoc.html

The API uses **OpenAPI 3.0** specification with contract-first approach defined in `infrastructure/inbound/rest/src/main/resources/contract/`

## 🧪 Testing

### Unit Tests
```bash
mvn test
```

### Integration Tests
```bash
mvn verify
# or specifically
mvn failsafe:integration-test
```

The project includes:
- **Unit Tests** (`src/test/java`): Fast, isolated business logic tests
- **Integration Tests** (`src/test-integration/java`): Database and API layer integration tests
- **Stub Tests** (`src/test-stub/java`): Tests with mock dependencies

## 📝 Configuration

Application configuration is organized by concerns:

- **Main**: `boot/src/main/resources/application.yml` - Composition layer
- **REST**: `infrastructure/inbound/rest/src/main/resources/application-rest.yml` - API configuration
- **Database**: `infrastructure/outbound/database/src/main/resources/application-database.yml` - Persistence configuration

**Created as an example of Hexagonal Architecture in Java** 🏗️
