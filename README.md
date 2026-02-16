# Hexagonal Architecture - Java Project

This is an example project that implements a **hexagonal architecture** (also known as ports and adapters architecture) using Java and Spring Boot.

## 📋 Description

Hexagonal architecture is a design pattern that promotes the separation of concerns, making code maintenance and testing easier. This project demonstrates how to implement this pattern in a modern Java application.

## 🏗️ Project Structure

The project is organized into independent Maven modules:

```
hexagonal/
├── domain/                 # Domain module (entities, domain services)
├── application/            # Application module (use cases, application services)
├── infrastructure/         # Infrastructure module
│   ├── inbound/           # Inbound adapters (REST, Web)
│   │   └── rest/          # REST API with OpenAPI/Swagger
│   └── outbound/          # Outbound adapters (Databases, External APIs)
│       └── memory/        # In-memory implementation
└── boot/                   # Boot module (Spring Boot Application)
```

## 🚀 How to Run

### 1. Build the project
```bash
mvn clean install
```

### 2. Run the application
```bash
cd boot
mvn spring-boot:run
```

The application will be available at `http://localhost:8080`

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
- OpenAPI/Swagger documentation available at `/swagger-ui.html`
- Versioned API contracts

**Available Resources:**
- `/api/users` - User-related operations

#### Outbound (Memory)
Outbound adapters that implement persistence. Currently uses an in-memory implementation.

**Content:**
- Repository implementations
- Data access

### Boot
Boot module that configures the Spring Boot application and includes all necessary dependencies.

**Content:**
- Main application class (`Application.java`)
- Property configuration (`application.yml`)
- Spring configuration

## 🔌 Ports and Adapters

### Ports (Interfaces)
Ports are defined as interfaces that represent contracts between the domain and the outside world.

### Adapters
Adapters are concrete implementations of ports:
- **REST Adapter**: Converts HTTP requests into application commands
- **Memory Adapter**: Persists data in memory (for development/testing)

## 📖 API Documentation

Once the application is running, access:
- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api/v3/api-docs

## 🧪 Testing

To run tests:
```bash
mvn test
```

## 📝 Configuration

Application configuration is found in [boot/src/main/resources/application.yml](boot/src/main/resources/application.yml)

**Created as an example of Hexagonal Architecture in Java** 🏗️
