package com.architecture.hexagonal.application.usecase.business.user.delete.impl;

import com.architecture.hexagonal.application.port.out.user.UserRepositoryReadPort;
import com.architecture.hexagonal.application.port.out.user.UserRepositoryWritePort;
import com.architecture.hexagonal.application.port.out.user.UserSenderPort;
import com.architecture.hexagonal.application.service.business.user.UserService;
import com.architecture.hexagonal.application.service.business.user.impl.UserServiceImpl;
import com.architecture.hexagonal.application.testutils.model.aggregate.user.UserTestDataBuilder;
import com.architecture.hexagonal.application.testutils.orchestration.data.command.DeleteUserInputTestDataBuilder;
import com.architecture.hexagonal.application.testutils.transaction.SpyTransactionWriteBoundary;
import com.architecture.hexagonal.contract.application.generated.user.DeleteUserCommand;
import com.architecture.hexagonal.domain.model.entity.user.User;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.assertj.core.api.AssertionsForClassTypes;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeleteUserApplicationUseCaseTestIT {

  @Mock private UserRepositoryReadPort userRepositoryReadPort;
  @Mock private UserRepositoryWritePort userRepositoryWritePort;
  @Mock private UserSenderPort userSenderPort;
  @Mock private UserService userService;

  @Spy private SpyTransactionWriteBoundary transactionWriteBoundary =
      new SpyTransactionWriteBoundary();

  @InjectMocks private UserServiceImpl userServiceImpl;
  @InjectMocks private DeleteUserUseCaseImpl deleteUserUseCase;

  @Test
  void deleteUserUseCaseShouldReturnDeletedUserWhenCommandIsExecuted() {
    final DeleteUserCommand command =
        DeleteUserInputTestDataBuilder.builder().build().deleteUserInput();
    final User user =
        UserTestDataBuilder.builder().userId(command.getUserId()).build().user();

    Mockito.when(userRepositoryWritePort.deleteUser(command.getUserId()))
        .thenReturn(Optional.of(user));
    Mockito.when(userService.deleteUser(command.getUserId()))
        .thenAnswer(
            org.mockito.AdditionalAnswers.answer(
                (UUID userId) -> userServiceImpl.deleteUser(userId)));

    final User result = deleteUserUseCase.handle(command);

    AssertionsForClassTypes.assertThat(result).isSameAs(user);
    Mockito.verify(transactionWriteBoundary).execute(ArgumentMatchers.any());
    Mockito.verify(userService).deleteUser(command.getUserId());
    Mockito.verify(userRepositoryWritePort).deleteUser(command.getUserId());
    Mockito.verify(userSenderPort).userSenderDeleted(user);
  }
}
