package com.architecture.hexagonal.infrastructure.bus.query.impl;

import com.architecture.hexagonal.application.port.in.bus.query.QueryHandler;
import com.architecture.hexagonal.application.testutils.time.TestClock;
import com.architecture.hexagonal.application.usecase.user.query.getall.impl.GetAllUsersUseCaseImpl;
import com.architecture.hexagonal.contract.bus.model.user.query.GetUsersFilteredQuery;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.pagination.PaginationResultVo;
import com.architecture.hexagonal.infrastructure.bus.BusHandlerFinder;
import com.architecture.hexagonal.infrastructure.bus.middleware.BusMiddleware;
import com.architecture.hexagonal.infrastructure.bus.middleware.impl.BusLoggingMiddleware;
import java.time.Clock;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class QueryBusImplTest {

  @Mock private BusHandlerFinder handlerFinder;

  @Spy private BusMiddleware loggingMiddleware = new BusLoggingMiddleware(TestClock.FIXED_CLOCK);

  @InjectMocks private QueryBusImpl queryBus;

  @Mock private GetAllUsersUseCaseImpl getAllUsersUseCase;

  @Test
  void executeShouldDelegateQueryToCorrectHandler() {
    final GetUsersFilteredQuery query = new GetUsersFilteredQuery();
    final PaginationResultVo<User> users = PaginationResultVo.<User>builder().build();

    Mockito.when(handlerFinder.find(query, QueryHandler.class)).thenReturn(getAllUsersUseCase);
    Mockito.when(getAllUsersUseCase.handle(query)).thenReturn(users);

    final PaginationResultVo<User> result = queryBus.execute(query);

    AssertionsForClassTypes.assertThat(result).isEqualTo(users);

    Mockito.verify(getAllUsersUseCase).handle(query);
  }

  @Test
  void executeShouldThrowIllegalStateExceptionWhenNoHandlerIsFound() {
    final String unknownQuery = "unknown";
    Mockito.when(handlerFinder.find(unknownQuery, QueryHandler.class))
        .thenThrow(new IllegalStateException("Expected one QueryHandler, found 0"));

    AssertionsForClassTypes.assertThatThrownBy(() -> queryBus.execute(unknownQuery))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Expected one QueryHandler")
        .hasMessageContaining("found 0");
  }
}
