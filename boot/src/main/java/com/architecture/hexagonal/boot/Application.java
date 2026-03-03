package com.architecture.hexagonal.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.architecture.hexagonal")
@EnableJpaRepositories(basePackages = "com.architecture.hexagonal.outbound.database.repository")
@EntityScan(basePackages = "com.architecture.hexagonal.outbound.database.data")
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
