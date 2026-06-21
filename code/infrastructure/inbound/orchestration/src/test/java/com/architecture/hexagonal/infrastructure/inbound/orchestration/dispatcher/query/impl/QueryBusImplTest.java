package com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.query.impl;

import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.domain.model.pagination.PaginationResult;
import com.architecture.hexagonal.infrastructure.contract.orchestration.generated.user.GetUsersFilteredQueryDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.query.QueryHandler;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.user.getallfiltered.GetUsersFilteredQueryHandlerImpl;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.query.GetUsersFilteredQueryDtoTestDataBuilder;
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

  @Mock private GetUsersFilteredQueryHandlerImpl getUsersFilteredQueryHandlerImpl;

  @Spy private final List<QueryHandler<?, ?>> queryHandlers = new ArrayList<>();

  @InjectMocks private QueryBusImpl queryBus;

  @Test
  void execute_shouldDelegateQueryToCorrectHandler() {
    final GetUsersFilteredQueryDto query =
        GetUsersFilteredQueryDtoTestDataBuilder.builder().build().getUsersFilteredQueryDto();
    final PaginationResult<User> users = PaginationResult.<User>builder().build();

    Mockito.when(getUsersFilteredQueryHandlerImpl.handle(query)).thenReturn(users);
    queryHandlers.add(getUsersFilteredQueryHandlerImpl);
    queryBus = new QueryBusImpl(queryHandlers);

    PaginationResult<User> result = queryBus.execute(query);

    AssertionsForClassTypes.assertThat(result).isEqualTo(users);

    Mockito.verify(getUsersFilteredQueryHandlerImpl).handle(query);
  }

  @Test
  void execute_shouldThrowIllegalStateException_whenNoHandlerIsFound() {
    final String unknownQuery = "unknown";

    AssertionsForClassTypes.assertThatThrownBy(() -> queryBus.execute(unknownQuery))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("No handler found for query");
  }
}
