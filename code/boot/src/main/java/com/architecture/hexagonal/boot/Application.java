package com.architecture.hexagonal.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.architecture.hexagonal")
@EnableJpaRepositories(
    basePackages = {"com.architecture.hexagonal.infrastructure.outbound.database.repository"})
@EntityScan(basePackages = "com.architecture.hexagonal.infrastructure.outbound.database.data")
@ConfigurationPropertiesScan(
    basePackages = {"com.architecture.hexagonal.infrastructure.outbound.configuration.config"})
public class Application {
  public static void main(final String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
