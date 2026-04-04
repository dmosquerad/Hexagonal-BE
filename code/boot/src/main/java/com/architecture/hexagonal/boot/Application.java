package com.architecture.hexagonal.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;

@SpringBootApplication(scanBasePackages = "com.architecture.hexagonal")
@EnableRabbit
@EnableJpaRepositories(basePackages = {
    "com.architecture.hexagonal.infrastructure.outbound.database.repository"
})
@EntityScan(basePackages = "com.architecture.hexagonal.infrastructure.outbound.database.data")
@ConfigurationPropertiesScan(basePackages = {
    "com.architecture.hexagonal.infrastructure.outbound.configuration.config",
    "com.architecture.hexagonal.infrastructure.inbound.rabbitmq.config"
})
public class Application {
  public static void main(final String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
