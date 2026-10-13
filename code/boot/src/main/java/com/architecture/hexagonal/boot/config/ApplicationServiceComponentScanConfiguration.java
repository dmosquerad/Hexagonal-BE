package com.architecture.hexagonal.boot.config;

import com.architecture.hexagonal.application.annotation.ApplicationService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ComponentScan(
    basePackages = ApplicationServiceComponentScanConfiguration.APPLICATION_SERVICE_BASE_PACKAGE,
    useDefaultFilters = false,
    includeFilters =
        @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = ApplicationService.class))
public class ApplicationServiceComponentScanConfiguration {

  public static final String APPLICATION_SERVICE_BASE_PACKAGE =
      "com.architecture.hexagonal.application.service";
}
