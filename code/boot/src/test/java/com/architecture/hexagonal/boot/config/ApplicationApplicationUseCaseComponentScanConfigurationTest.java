package com.architecture.hexagonal.boot.config;

import com.architecture.hexagonal.application.annotation.ApplicationUseCase;
import com.architecture.hexagonal.application.usecase.email.getblockedrules.impl.GetBlockedRulesUseCaseImpl;
import com.architecture.hexagonal.application.usecase.outbox.retry.impl.RetryOutboxUseCaseImpl;
import com.architecture.hexagonal.application.usecase.user.create.impl.CreateUserUseCaseImpl;
import com.architecture.hexagonal.application.usecase.user.delete.impl.DeleteUserUseCaseImpl;
import com.architecture.hexagonal.application.usecase.user.exists.impl.UserExistsUseCaseImpl;
import com.architecture.hexagonal.application.usecase.user.findbyid.impl.FindUserByUserIdUseCaseImpl;
import com.architecture.hexagonal.application.usecase.user.getall.impl.GetAllUsersUseCaseImpl;
import com.architecture.hexagonal.application.usecase.user.patch.impl.PatchUserUseCaseImpl;
import com.architecture.hexagonal.application.usecase.user.update.impl.UpdateUserUseCaseImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

class ApplicationApplicationUseCaseComponentScanConfigurationTest {

  @Test
  void shouldDiscoverExactlyTheAnnotatedUseCases() {
    final ClassPathScanningCandidateComponentProvider scanner =
        new ClassPathScanningCandidateComponentProvider(false);
    scanner.addIncludeFilter(new AnnotationTypeFilter(ApplicationUseCase.class));

    Assertions.assertThat(
            scanner.findCandidateComponents(
                ApplicationUseCaseComponentScanConfiguration.APPLICATION_BASE_PACKAGE))
        .extracting(BeanDefinition::getBeanClassName)
        .containsExactlyInAnyOrderElementsOf(
            java.util.List.of(
                GetBlockedRulesUseCaseImpl.class.getName(),
                CreateUserUseCaseImpl.class.getName(),
                DeleteUserUseCaseImpl.class.getName(),
                UserExistsUseCaseImpl.class.getName(),
                FindUserByUserIdUseCaseImpl.class.getName(),
                GetAllUsersUseCaseImpl.class.getName(),
                PatchUserUseCaseImpl.class.getName(),
                UpdateUserUseCaseImpl.class.getName(),
                RetryOutboxUseCaseImpl.class.getName()));
  }
}
