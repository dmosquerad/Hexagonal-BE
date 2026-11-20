package com.architecture.hexagonal.infrastructure.outbound.database.postgresql.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.Clock;

@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = {
        "com.architecture.hexagonal.infrastructure.outbound.database.postgresql.repository"
})
@EntityScan(basePackages = {
	"com.architecture.hexagonal.infrastructure.outbound.database.postgresql.data"
})
@ComponentScan(basePackages = {
        "com.architecture.hexagonal.infrastructure.outbound.database.postgresql.config",
        "com.architecture.hexagonal.infrastructure.outbound.database.postgresql.mapper"
})
public class PostgresqlTestApplication {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}