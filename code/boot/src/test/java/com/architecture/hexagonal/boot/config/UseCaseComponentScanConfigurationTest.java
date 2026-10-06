package com.architecture.hexagonal.boot.config;

import com.architecture.hexagonal.application.annotation.UseCase;
import com.architecture.hexagonal.application.usecase.business.email.getblockedrules.usecase.impl.GetBlockedRulesUseCaseImpl;
import com.architecture.hexagonal.application.usecase.business.user.create.usecase.impl.CreateUserUseCaseImpl;
import com.architecture.hexagonal.application.usecase.business.user.delete.usecase.impl.DeleteUserUseCaseImpl;
import com.architecture.hexagonal.application.usecase.business.user.exists.usecase.impl.UserExistsUseCaseImpl;
import com.architecture.hexagonal.application.usecase.business.user.findbyid.usecase.impl.FindUserByUserIdUseCaseImpl;
import com.architecture.hexagonal.application.usecase.business.user.getall.usecase.impl.GetAllUsersUseCaseImpl;
import com.architecture.hexagonal.application.usecase.business.user.patch.usecase.impl.PatchUserUseCaseImpl;
import com.architecture.hexagonal.application.usecase.business.user.update.usecase.impl.UpdateUserUseCaseImpl;
import com.architecture.hexagonal.application.usecase.technical.outbox.find.usecase.impl.FindOutboxUseCaseImpl;
import com.architecture.hexagonal.application.usecase.technical.outbox.process.usecase.impl.ProcessOutboxUseCaseImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

class UseCaseComponentScanConfigurationTest {

  private static final String APPLICATION_BASE_PACKAGE =
      "com.architecture.hexagonal.application.usecase";

  @Test
  void shouldDiscoverExactlyTheAnnotatedUseCases() {
    final ClassPathScanningCandidateComponentProvider scanner =
        new ClassPathScanningCandidateComponentProvider(false);
    scanner.addIncludeFilter(new AnnotationTypeFilter(UseCase.class));

    Assertions.assertThat(scanner.findCandidateComponents(APPLICATION_BASE_PACKAGE))
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
                FindOutboxUseCaseImpl.class.getName(),
                ProcessOutboxUseCaseImpl.class.getName()));
  }
}
