package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import java.io.IOException;

@Testcontainers
public abstract class MongodbIT {

  @Container
  @ServiceConnection
  static MongoDBContainer mongo = new MongoDBContainer("mongo:8.3")
          .withCopyFileToContainer(
                    MountableFile.forClasspathResource("init-mongo.js"),
                    "/init-mongo.js"
            );

    @BeforeAll
    static void initMongo() throws IOException, InterruptedException {
        mongo.execInContainer(
                "mongosh",
                "--file",
                "/init-mongo.js"
        );
    }

  @DynamicPropertySource
  static void configure(DynamicPropertyRegistry registry) {
    registry.add("spring.data.mongodb.uri", mongo::getReplicaSetUrl);
  }

}
