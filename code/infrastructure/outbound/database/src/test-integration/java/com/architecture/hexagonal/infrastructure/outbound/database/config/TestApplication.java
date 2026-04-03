package com.architecture.hexagonal.infrastructure.outbound.database.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = {
	"com.architecture.hexagonal.infrastructure.outbound.database.repository"
})
@EntityScan(basePackages = {
	"com.architecture.hexagonal.infrastructure.outbound.database.data"
})
@ComponentScan(basePackages = {
	"com.architecture.hexagonal.infrastructure.outbound.database.config",
	"com.architecture.hexagonal.infrastructure.outbound.database.mapper"
})
public class TestApplication {

}