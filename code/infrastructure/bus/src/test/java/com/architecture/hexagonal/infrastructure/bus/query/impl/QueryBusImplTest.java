package com.architecture.hexagonal.infrastructure.bus.query.impl;

import com.architecture.hexagonal.application.port.in.bus.query.QueryHandler;
import com.architecture.hexagonal.application.usecase.user.getall.impl.GetAllUsersUseCaseImpl;
import com.architecture.hexagonal.contract.application.usecase.generated.user.GetUsersFilteredQuery;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.pagination.PaginationResultVo;
import java.util.ArrayList;
import java.util.List;
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

  @Mock private GetAllUsersUseCaseImpl getAllUsersUseCase;

  @Spy private final List<QueryHandler<?, ?>> queryHandlers = new ArrayList<>();

  @InjectMocks private QueryBusImpl queryBus;

  @Test
  void executeShouldDelegateQueryToCorrectHandler() {
    final GetUsersFilteredQuery query = new GetUsersFilteredQuery();
    final PaginationResultVo<User> users = PaginationResultVo.<User>builder().build();

    Mockito.when(getAllUsersUseCase.handle(query)).thenReturn(users);
    queryHandlers.add(getAllUsersUseCase);
    queryBus = new QueryBusImpl(queryHandlers);

    final PaginationResultVo<User> result = queryBus.execute(query);

    AssertionsForClassTypes.assertThat(result).isEqualTo(users);

    Mockito.verify(getAllUsersUseCase).handle(query);
  }

  @Test
  void executeShouldThrowIllegalStateExceptionWhenNoHandlerIsFound() {
    final String unknownQuery = "unknown";

    AssertionsForClassTypes.assertThatThrownBy(() -> queryBus.execute(unknownQuery))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("No handler found for query");
  }
}
