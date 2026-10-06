package com.architecture.hexagonal.boot.config;

import com.architecture.hexagonal.application.annotation.UseCase;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
    basePackages = "com.architecture.hexagonal.application.usecase",
    useDefaultFilters = false,
    includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = UseCase.class))
public class UseCaseComponentScanConfiguration {}
