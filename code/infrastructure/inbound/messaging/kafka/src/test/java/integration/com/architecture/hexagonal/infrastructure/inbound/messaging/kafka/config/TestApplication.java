package com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@EnableAutoConfiguration
@Import(TestChannelBinderConfiguration.class)
@ComponentScan(basePackages = {
    "com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.config",
    "com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.mapper"
})
public class TestApplication {

}
