package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config;

import java.time.Clock;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
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

  @Bean
  public Clock clock() {
    return Clock.systemUTC();
  }
}
