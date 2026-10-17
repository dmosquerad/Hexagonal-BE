package com.architecture.hexagonal.application.usecase.user.delete.impl;

import com.architecture.hexagonal.application.port.out.transaction.TransactionWriteBoundary;
import com.architecture.hexagonal.application.service.user.UserService;
import com.architecture.hexagonal.application.testutils.transaction.SpyTransactionWriteBoundary;
import com.architecture.hexagonal.application.testutils.usecase.command.DeleteUserInputTestDataBuilder;
import com.architecture.hexagonal.contract.bus.model.user.DeleteUserCommand;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.testutils.data.model.entity.user.UserTestDataBuilder;
import java.util.function.Supplier;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeleteUserUseCaseImplTest {

  @Mock private UserService userService;

  @Spy private TransactionWriteBoundary transactionBoundary = new SpyTransactionWriteBoundary();

  @InjectMocks private DeleteUserUseCaseImpl useCase;

  @Test
  void handleShouldReturnDeletedUserWhenCommandIsExecuted() {
    final DeleteUserCommand command =
        DeleteUserInputTestDataBuilder.builder().build().deleteUserInput();
    final User user = UserTestDataBuilder.builder().build().user();
    Mockito.when(userService.deleteUser(command.getUserId())).thenReturn(user);

    final User result = useCase.handle(command);

    AssertionsForClassTypes.assertThat(result).isSameAs(user);
    Mockito.verify(userService).deleteUser(command.getUserId());
    Mockito.verify(transactionBoundary).execute(Mockito.<Supplier<User>>any());
  }
}
