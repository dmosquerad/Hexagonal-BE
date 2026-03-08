package com.architecture.hexagonal.outbound.database.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("database")
@TestConfiguration
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = "com.architecture.hexagonal.outbound.database.repository")
@EntityScan(basePackages = "com.architecture.hexagonal.outbound.database.data")
@ComponentScan(basePackages = "com.architecture.hexagonal.outbound.database")
public class TestApplication {

}