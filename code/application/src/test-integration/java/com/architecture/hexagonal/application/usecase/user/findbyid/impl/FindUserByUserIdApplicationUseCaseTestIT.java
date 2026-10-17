package com.architecture.hexagonal.application.usecase.user.findbyid.impl;

import com.architecture.hexagonal.application.port.out.user.UserRepositoryReadPort;
import com.architecture.hexagonal.application.port.out.user.UserRepositoryWritePort;
import com.architecture.hexagonal.application.port.out.user.UserSenderPort;
import com.architecture.hexagonal.application.service.user.UserService;
import com.architecture.hexagonal.application.service.user.impl.UserServiceImpl;
import com.architecture.hexagonal.domain.testutils.data.model.entity.user.UserTestDataBuilder;
import com.architecture.hexagonal.application.testutils.usecase.query.FindUserByUserIdInputTestDataBuilder;
import com.architecture.hexagonal.application.testutils.transaction.SpyTransactionReadBoundary;
import com.architecture.hexagonal.contract.application.usecase.generated.user.FindUserByUserIdQuery;
import com.architecture.hexagonal.domain.model.entity.user.User;
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
class FindUserByUserIdApplicationUseCaseTestIT {

  @Mock private UserRepositoryReadPort userRepositoryReadPort;
  @Mock private UserRepositoryWritePort userRepositoryWritePort;
  @Mock private UserSenderPort userSenderPort;
  @Mock private UserService userService;

  @Spy private SpyTransactionReadBoundary transactionReadBoundary =
      new SpyTransactionReadBoundary();

  @InjectMocks private UserServiceImpl userServiceImpl;
  @InjectMocks private FindUserByUserIdUseCaseImpl findUserByUserIdUseCase;

  @Test
  void findUserByUserIdUseCaseShouldReturnUserWhenQueryIsExecuted() {
    final FindUserByUserIdQuery query =
        FindUserByUserIdInputTestDataBuilder.builder().build().findUserByUserIdInput();
    final User user =
        UserTestDataBuilder.builder().userId(query.getUserId()).build().user();

    Mockito.when(userRepositoryReadPort.findUserById(query.getUserId()))
        .thenReturn(Optional.of(user));
    Mockito.when(userService.findUserById(query.getUserId()))
        .thenAnswer(
            AdditionalAnswers.answer(
                (UUID userId) -> userServiceImpl.findUserById(userId)));

    final User result = findUserByUserIdUseCase.handle(query);

    AssertionsForClassTypes.assertThat(result).isSameAs(user);
    Mockito.verify(transactionReadBoundary).execute(ArgumentMatchers.any());
    Mockito.verify(userService).findUserById(query.getUserId());
    Mockito.verify(userRepositoryReadPort).findUserById(query.getUserId());
  }
}
