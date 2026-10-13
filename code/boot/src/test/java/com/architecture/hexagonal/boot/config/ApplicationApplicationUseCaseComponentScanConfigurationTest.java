package com.architecture.hexagonal.boot.config;

import com.architecture.hexagonal.application.annotation.ApplicationUseCase;
import com.architecture.hexagonal.application.usecase.business.email.getblockedrules.impl.GetBlockedRulesUseCaseImpl;
import com.architecture.hexagonal.application.usecase.business.user.create.impl.CreateUserUseCaseImpl;
import com.architecture.hexagonal.application.usecase.business.user.delete.impl.DeleteUserUseCaseImpl;
import com.architecture.hexagonal.application.usecase.business.user.exists.impl.UserExistsUseCaseImpl;
import com.architecture.hexagonal.application.usecase.business.user.findbyid.impl.FindUserByUserIdUseCaseImpl;
import com.architecture.hexagonal.application.usecase.business.user.getall.impl.GetAllUsersUseCaseImpl;
import com.architecture.hexagonal.application.usecase.business.user.patch.impl.PatchUserUseCaseImpl;
import com.architecture.hexagonal.application.usecase.business.user.update.impl.UpdateUserUseCaseImpl;
import com.architecture.hexagonal.application.usecase.technical.outbox.retry.impl.RetryOutboxUseCaseImpl;
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
