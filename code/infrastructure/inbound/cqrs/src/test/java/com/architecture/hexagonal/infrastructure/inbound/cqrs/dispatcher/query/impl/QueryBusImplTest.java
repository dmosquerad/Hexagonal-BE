package com.architecture.hexagonal.infrastructure.inbound.cqrs.dispatcher.query.impl;

import com.architecture.hexagonal.domain.exception.DomainException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.domain.model.pagination.PaginationResult;
import com.architecture.hexagonal.infrastructure.contract.cqrs.generated.user.GetUsersFilteredQueryDto;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.dispatcher.query.QueryHandler;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.orchestrator.user.getallfiltered.query.handler.GetUsersFilteredQueryHandler;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.testutils.data.query.GetUsersFilteredQueryDtoTestDataBuilder;
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

  @Mock private GetUsersFilteredQueryHandler getUsersFilteredQueryHandler;

  @Spy private final List<QueryHandler<?, ?>> queryHandlers = new ArrayList<>();

  @InjectMocks private QueryBusImpl queryBus;

  @Test
  void execute_shouldDelegateQueryToCorrectHandler() throws DomainException {
    final GetUsersFilteredQueryDto query =
        GetUsersFilteredQueryDtoTestDataBuilder.builder().build().getUsersFilteredQueryDto();
    final PaginationResult<User> users = PaginationResult.<User>builder().build();

    Mockito.when(getUsersFilteredQueryHandler.handle(query)).thenReturn(users);
    queryHandlers.add(getUsersFilteredQueryHandler);
    queryBus = new QueryBusImpl(queryHandlers);

    PaginationResult<User> result = queryBus.execute(query);

    AssertionsForClassTypes.assertThat(result).isEqualTo(users);

    Mockito.verify(getUsersFilteredQueryHandler).handle(query);
  }

  @Test
  void execute_shouldThrowIllegalStateException_whenNoHandlerIsFound() {
    final String unknownQuery = "unknown";

    AssertionsForClassTypes.assertThatThrownBy(() -> queryBus.execute(unknownQuery))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("No handler found for query");
  }
}
