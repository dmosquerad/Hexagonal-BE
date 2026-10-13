package com.architecture.hexagonal.application.usecase.business.user.findbyid.impl;

import com.architecture.hexagonal.application.port.out.transaction.TransactionReadBoundary;
import com.architecture.hexagonal.application.service.business.user.UserService;
import com.architecture.hexagonal.application.testutils.model.aggregate.user.UserTestDataBuilder;
import com.architecture.hexagonal.application.testutils.orchestration.data.query.FindUserByUserIdInputTestDataBuilder;
import com.architecture.hexagonal.application.testutils.transaction.SpyTransactionReadBoundary;
import com.architecture.hexagonal.contract.application.generated.user.FindUserByUserIdQuery;
import com.architecture.hexagonal.domain.model.entity.user.User;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindUserByUserIdUseCaseImplTest {

  @Mock private UserService userService;

  @Spy private TransactionReadBoundary transactionBoundary = new SpyTransactionReadBoundary();

  @InjectMocks private FindUserByUserIdUseCaseImpl useCase;

  @Test
  void handleShouldReturnUserWhenQueryIsExecuted() {
    final FindUserByUserIdQuery query =
        FindUserByUserIdInputTestDataBuilder.builder().build().findUserByUserIdInput();
    final User user = UserTestDataBuilder.builder().build().user();
    Mockito.when(userService.findUserById(query.getUserId())).thenReturn(user);

    final User result = useCase.handle(query);

    AssertionsForClassTypes.assertThat(result).isSameAs(user);
    Mockito.verify(userService).findUserById(query.getUserId());
    Mockito.verify(transactionBoundary).execute(Mockito.any());
  }
}
