package com.architecture.hexagonal.infrastructure.bus.query.impl;

import com.architecture.hexagonal.domain.port.in.transaction.TransactionReadBoundary;
import com.architecture.hexagonal.application.service.user.UserService;
import com.architecture.hexagonal.application.usecase.user.query.findbyid.impl.FindUserByUserIdUseCaseImpl;
import com.architecture.hexagonal.application.model.query.user.FindUserByUserIdQuery;
import com.architecture.hexagonal.application.model.query.user.GetUsersFilteredQuery;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.infrastructure.bus.config.TestApplication;
import com.architecture.hexagonal.infrastructure.bus.BusHandlerFinder;
import com.architecture.hexagonal.infrastructure.bus.BusMessageValidator;
import com.architecture.hexagonal.infrastructure.bus.middleware.impl.BusLoggingMiddleware;
import com.architecture.hexagonal.domain.port.in.bus.query.QueryBus;
import java.util.UUID;
import java.util.function.Supplier;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;

@SpringBootTest(
    classes = {
      QueryBusImpl.class,
      BusHandlerFinder.class,
      BusMessageValidator.class,
      BusLoggingMiddleware.class,
      FindUserByUserIdUseCaseImpl.class
    })
@ContextConfiguration(classes = TestApplication.class)
class QueryBusImplTestIT {

  @Autowired private QueryBus queryBus;

  @MockitoBean private UserService userService;

  @MockitoBean private TransactionReadBoundary transactionBoundary;

  @Test
  void executeShouldDelegateQueryToCorrectHandler() {
    final FindUserByUserIdQuery query =
        FindUserByUserIdQuery.builder().userId(UUID.randomUUID()).build();
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
    final GetUsersFilteredQuery query = GetUsersFilteredQuery.builder().build();

    AssertionsForClassTypes.assertThatThrownBy(() -> queryBus.execute(query))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Expected one QueryHandler")
        .hasMessageContaining("found 0");
  }

  @Test
  void executeShouldRejectInvalidNestedContractValues() {
    final com.architecture.hexagonal.application.model.vo.pagination.Pagination pagination =
        com.architecture.hexagonal.application.model.vo.pagination.Pagination.builder()
            .page(0)
            .size(0)
            .build();
    final GetUsersFilteredQuery query =
        GetUsersFilteredQuery.builder().pagination(pagination).build();

    AssertionsForClassTypes.assertThatThrownBy(() -> queryBus.execute(query))
        .isInstanceOf(ConstraintViolationException.class);

    Mockito.verifyNoInteractions(userService);
  }
}
