package com.architecture.hexagonal.outbound.database.config;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("database")
@Testcontainers
public abstract class DatabaseIT {

  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");

  @DynamicPropertySource
  static void configure(DynamicPropertyRegistry registry) {
      registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
      registry.add("spring.jpa.show-sql", () -> "true");
      registry.add("spring.jpa.properties.hibernate.format_sql", () -> "true");
      registry.add("spring.jpa.properties.hibernate.use_sql_comments", () -> "true");
  }
}
