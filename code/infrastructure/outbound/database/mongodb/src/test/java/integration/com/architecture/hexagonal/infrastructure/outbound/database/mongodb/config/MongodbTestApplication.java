package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableAutoConfiguration
@EnableMongoRepositories(basePackages = {
        "com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository"
})
@ComponentScan(basePackages = {
        "com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config",
        "com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper"
})
public class MongodbTestApplication {

}
