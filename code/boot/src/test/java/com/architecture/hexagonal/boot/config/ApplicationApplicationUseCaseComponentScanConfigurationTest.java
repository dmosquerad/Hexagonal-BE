package com.architecture.hexagonal.boot.config;

import com.architecture.hexagonal.application.annotation.ApplicationUseCase;
import com.architecture.hexagonal.application.usecase.email.query.getblockedrules.impl.GetBlockedRulesUseCaseImpl;
import com.architecture.hexagonal.application.usecase.inbox.command.persist.impl.PersistInboxUseCaseImpl;
import com.architecture.hexagonal.application.usecase.inbox.command.retry.impl.RetryInboxUseCaseImpl;
import com.architecture.hexagonal.application.usecase.outbox.command.retry.impl.RetryOutboxUseCaseImpl;
import com.architecture.hexagonal.application.usecase.user.command.create.impl.CreateUserUseCaseImpl;
import com.architecture.hexagonal.application.usecase.user.command.delete.impl.DeleteUserUseCaseImpl;
import com.architecture.hexagonal.application.usecase.user.command.patch.impl.PatchUserUseCaseImpl;
import com.architecture.hexagonal.application.usecase.user.command.update.impl.UpdateUserUseCaseImpl;
import com.architecture.hexagonal.application.usecase.user.query.exists.impl.UserExistsUseCaseImpl;
import com.architecture.hexagonal.application.usecase.user.query.findbyid.impl.FindUserByUserIdUseCaseImpl;
import com.architecture.hexagonal.application.usecase.user.query.getall.impl.GetAllUsersUseCaseImpl;
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
                RetryOutboxUseCaseImpl.class.getName(),
                PersistInboxUseCaseImpl.class.getName(),
                RetryInboxUseCaseImpl.class.getName()));
  }
}
