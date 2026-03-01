package com.hexagonal.inbound.rest.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.architecture.hexagonal")
public class TestApplication {

}