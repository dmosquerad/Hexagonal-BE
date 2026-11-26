package com.architecture.hexagonal.application.integration.usecase.user.query.exists.impl;

import com.architecture.hexagonal.application.databuilder.transaction.SpyTransactionReadBoundary;
import com.architecture.hexagonal.application.databuilder.model.query.user.UserExistsInputTestDataBuilder;
import com.architecture.hexagonal.application.usecase.user.query.exists.impl.UserExistsUseCaseImpl;
import com.architecture.hexagonal.domain.port.out.repository.user.UserRepositoryReadPort;
import com.architecture.hexagonal.domain.port.out.repository.user.UserRepositoryWritePort;
import com.architecture.hexagonal.domain.port.out.bus.event.EventBus;
import com.architecture.hexagonal.application.service.user.UserService;
import com.architecture.hexagonal.application.service.user.impl.UserServiceImpl;
import com.architecture.hexagonal.application.model.query.user.UserExistsQuery;
import com.architecture.hexagonal.domain.databuilder.model.entity.user.UserTestDataBuilder;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.assertj.core.api.AssertionsForClassTypes;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserExistsUseCaseImplTestIT {

  @Mock private UserRepositoryReadPort userRepositoryReadPort;
  @Mock private UserRepositoryWritePort userRepositoryWritePort;
  @Mock private EventBus eventBus;
  @Mock private UserService userService;

  @Spy private SpyTransactionReadBoundary transactionReadBoundary =
      new SpyTransactionReadBoundary();

  @InjectMocks private UserServiceImpl userServiceImpl;
  @InjectMocks private UserExistsUseCaseImpl userExistsUseCase;

  @Test
  void userExistsUseCaseShouldExecuteQueryWhenQueryIsExecuted() {
    final UserExistsQuery query =
        UserExistsInputTestDataBuilder.builder().build().userExistsInput();
    Mockito.when(userRepositoryReadPort.findUserById(query.userId()))
        .thenReturn(
            Optional.of(
                UserTestDataBuilder.builder().userId(query.userId()).build().user()));
    Mockito.doAnswer(
            AdditionalAnswers.answerVoid(
                (UUID userId) -> userServiceImpl.existsUser(userId)))
        .when(userService)
        .existsUser(query.userId());

    final Void result = userExistsUseCase.handle(query);

    AssertionsForClassTypes.assertThat(result).isNull();
    Mockito.verify(transactionReadBoundary).execute(ArgumentMatchers.any());
    Mockito.verify(userService).existsUser(query.userId());
    Mockito.verify(userRepositoryReadPort).findUserById(query.userId());
  }
}
