package com.architecture.hexagonal.infrastructure.inbound.bus.query.impl;

import com.architecture.hexagonal.application.port.out.transaction.TransactionReadBoundary;
import com.architecture.hexagonal.application.service.business.user.UserService;
import com.architecture.hexagonal.application.usecase.business.user.findbyid.impl.FindUserByUserIdUseCaseImpl;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.infrastructure.inbound.bus.config.TestApplication;
import com.architecture.hexagonal.application.port.in.bus.query.QueryBus;
import com.architecture.hexagonal.contract.application.generated.user.FindUserByUserIdQuery;
import com.architecture.hexagonal.contract.application.generated.user.GetUsersFilteredQuery;
import java.util.UUID;
import java.util.function.Supplier;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;

@SpringBootTest(classes = {QueryBusImpl.class, FindUserByUserIdUseCaseImpl.class})
@ContextConfiguration(classes = TestApplication.class)
class QueryBusImplTestIT {

  @Autowired private QueryBus queryBus;

  @MockitoBean private UserService userService;

  @MockitoBean private TransactionReadBoundary transactionBoundary;

  @Test
  void executeShouldDelegateQueryToCorrectHandler() {
    final FindUserByUserIdQuery query = new FindUserByUserIdQuery();
    query.setUserId(UUID.randomUUID());
    final User user = User.builder().build();

    Mockito.when(transactionBoundary.execute(Mockito.any()))
        .thenAnswer(
            AdditionalAnswers.answer((Supplier<User> action) -> action.get()));
    Mockito.when(userService.findUserById(Mockito.any())).thenReturn(user);

    final User result = queryBus.execute(query);

    AssertionsForClassTypes.assertThat(result).isSameAs(user);

    Mockito.verify(userService).findUserById(Mockito.any());
    Mockito.verify(transactionBoundary).execute(Mockito.<Supplier<User>>any());
  }

  @Test
  void executeShouldThrowIllegalStateExceptionWhenNoHandlerIsFound() {
    final GetUsersFilteredQuery query = new GetUsersFilteredQuery();

    AssertionsForClassTypes.assertThatThrownBy(() -> queryBus.execute(query))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("No handler found for query");
  }
}
