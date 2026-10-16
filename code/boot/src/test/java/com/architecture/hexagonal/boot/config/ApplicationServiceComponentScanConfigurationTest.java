package com.architecture.hexagonal.boot.config;

import com.architecture.hexagonal.application.annotation.ApplicationService;
import com.architecture.hexagonal.application.service.email.impl.EmailServiceImpl;
import com.architecture.hexagonal.application.service.inbox.impl.InboxServiceImpl;
import com.architecture.hexagonal.application.service.outbox.impl.OutboxServiceImpl;
import com.architecture.hexagonal.application.service.user.impl.UserServiceImpl;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

class ApplicationServiceComponentScanConfigurationTest {

  @Test
  void shouldDiscoverExactlyTheAnnotatedApplicationServices() {
    final ClassPathScanningCandidateComponentProvider scanner =
        new ClassPathScanningCandidateComponentProvider(false);
    scanner.addIncludeFilter(new AnnotationTypeFilter(ApplicationService.class));

    Assertions.assertThat(
            scanner.findCandidateComponents(
                ApplicationServiceComponentScanConfiguration.APPLICATION_SERVICE_BASE_PACKAGE))
        .extracting(BeanDefinition::getBeanClassName)
        .containsExactlyInAnyOrderElementsOf(
            List.of(
                EmailServiceImpl.class.getName(),
                UserServiceImpl.class.getName(),
                OutboxServiceImpl.class.getName(),
                InboxServiceImpl.class.getName()));
  }
}
