package com.architecture.hexagonal.boot.config;

import com.architecture.hexagonal.application.annotation.ApplicationUseCase;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ComponentScan(
    basePackages = ApplicationUseCaseComponentScanConfiguration.APPLICATION_BASE_PACKAGE,
    useDefaultFilters = false,
    includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = ApplicationUseCase.class))
public class ApplicationUseCaseComponentScanConfiguration {
  public static final String APPLICATION_BASE_PACKAGE =
      "com.architecture.hexagonal.application.usecase";
}
