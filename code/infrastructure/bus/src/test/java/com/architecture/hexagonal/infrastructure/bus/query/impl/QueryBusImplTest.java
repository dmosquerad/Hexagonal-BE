package com.architecture.hexagonal.infrastructure.bus.query.impl;

import com.architecture.hexagonal.application.usecase.user.getall.impl.GetAllUsersUseCaseImpl;
import com.architecture.hexagonal.contract.application.usecase.generated.user.GetUsersFilteredQuery;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.pagination.PaginationResultVo;
import com.architecture.hexagonal.infrastructure.bus.MessageHandlerFinder;
import java.util.List;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class QueryBusImplTest {

  @Mock private GetAllUsersUseCaseImpl getAllUsersUseCase;

  @Test
  void executeShouldDelegateQueryToCorrectHandler() {
    final GetUsersFilteredQuery query = new GetUsersFilteredQuery();
    final PaginationResultVo<User> users = PaginationResultVo.<User>builder().build();

    Mockito.when(getAllUsersUseCase.handle(query)).thenReturn(users);
    final QueryBusImpl queryBus =
        new QueryBusImpl(new MessageHandlerFinder(List.of(), List.of(getAllUsersUseCase)));

    final PaginationResultVo<User> result = queryBus.execute(query);

    AssertionsForClassTypes.assertThat(result).isEqualTo(users);

    Mockito.verify(getAllUsersUseCase).handle(query);
  }

  @Test
  void executeShouldThrowIllegalStateExceptionWhenNoHandlerIsFound() {
    final String unknownQuery = "unknown";
    final QueryBusImpl queryBus = new QueryBusImpl(new MessageHandlerFinder(List.of(), List.of()));

    AssertionsForClassTypes.assertThatThrownBy(() -> queryBus.execute(unknownQuery))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Expected one QueryHandler")
        .hasMessageContaining("found 0");
  }
}
