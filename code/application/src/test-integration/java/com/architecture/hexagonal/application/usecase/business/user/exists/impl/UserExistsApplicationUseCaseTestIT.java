package com.architecture.hexagonal.application.usecase.business.user.exists.impl;

import com.architecture.hexagonal.application.port.out.user.UserRepositoryReadPort;
import com.architecture.hexagonal.application.port.out.user.UserRepositoryWritePort;
import com.architecture.hexagonal.application.port.out.user.UserSenderPort;
import com.architecture.hexagonal.application.service.business.user.UserService;
import com.architecture.hexagonal.application.service.business.user.impl.UserServiceImpl;
import com.architecture.hexagonal.application.testutils.model.aggregate.user.UserTestDataBuilder;
import com.architecture.hexagonal.application.testutils.orchestration.data.query.UserExistsInputTestDataBuilder;
import com.architecture.hexagonal.application.testutils.transaction.SpyTransactionReadBoundary;
import com.architecture.hexagonal.contract.application.generated.user.UserExistsQuery;
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
class UserExistsApplicationUseCaseTestIT {

  @Mock private UserRepositoryReadPort userRepositoryReadPort;
  @Mock private UserRepositoryWritePort userRepositoryWritePort;
  @Mock private UserSenderPort userSenderPort;
  @Mock private UserService userService;

  @Spy private SpyTransactionReadBoundary transactionReadBoundary =
      new SpyTransactionReadBoundary();

  @InjectMocks private UserServiceImpl userServiceImpl;
  @InjectMocks private UserExistsUseCaseImpl userExistsUseCase;

  @Test
  void userExistsUseCaseShouldExecuteQueryWhenQueryIsExecuted() {
    final UserExistsQuery query =
        UserExistsInputTestDataBuilder.builder().build().userExistsInput();
    Mockito.when(userRepositoryReadPort.findUserById(query.getUserId()))
        .thenReturn(
            Optional.of(
                UserTestDataBuilder.builder().userId(query.getUserId()).build().user()));
    Mockito.doAnswer(
            AdditionalAnswers.answerVoid(
                (UUID userId) -> userServiceImpl.existsUser(userId)))
        .when(userService)
        .existsUser(query.getUserId());

    final Void result = userExistsUseCase.handle(query);

    AssertionsForClassTypes.assertThat(result).isNull();
    Mockito.verify(transactionReadBoundary).execute(ArgumentMatchers.any());
    Mockito.verify(userService).existsUser(query.getUserId());
    Mockito.verify(userRepositoryReadPort).findUserById(query.getUserId());
  }
}
