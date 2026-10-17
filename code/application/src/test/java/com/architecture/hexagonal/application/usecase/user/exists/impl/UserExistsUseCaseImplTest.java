package com.architecture.hexagonal.application.usecase.user.exists.impl;

import com.architecture.hexagonal.application.port.out.transaction.TransactionReadBoundary;
import com.architecture.hexagonal.application.service.user.UserService;
import com.architecture.hexagonal.application.testutils.transaction.SpyTransactionReadBoundary;
import com.architecture.hexagonal.application.testutils.usecase.query.UserExistsInputTestDataBuilder;
import com.architecture.hexagonal.contract.application.usecase.generated.user.UserExistsQuery;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserExistsUseCaseImplTest {

  @Mock private UserService userService;

  @Spy private TransactionReadBoundary transactionBoundary = new SpyTransactionReadBoundary();

  @InjectMocks private UserExistsUseCaseImpl useCase;

  @Test
  void handleShouldReturnNullWhenQueryIsExecuted() {
    final UserExistsQuery query =
        UserExistsInputTestDataBuilder.builder().build().userExistsInput();
    final Void result = useCase.handle(query);

    AssertionsForClassTypes.assertThat(result).isNull();
    Mockito.verify(userService).existsUser(query.getUserId());
    Mockito.verifyNoMoreInteractions(userService);
    Mockito.verify(transactionBoundary).execute(Mockito.any());
  }
}
