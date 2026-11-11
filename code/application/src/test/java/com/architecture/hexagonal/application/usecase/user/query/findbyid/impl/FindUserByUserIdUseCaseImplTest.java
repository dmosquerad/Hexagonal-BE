package com.architecture.hexagonal.application.usecase.user.query.findbyid.impl;

import com.architecture.hexagonal.application.service.user.UserService;
import com.architecture.hexagonal.application.transaction.SpyTransactionReadBoundary;
import com.architecture.hexagonal.application.usecase.user.query.FindUserByUserIdInputTestDataBuilder;
import com.architecture.hexagonal.contract.bus.model.user.query.FindUserByUserIdQuery;
import com.architecture.hexagonal.domain.data.model.entity.user.UserTestDataBuilder;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.port.in.transaction.TransactionReadBoundary;
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
