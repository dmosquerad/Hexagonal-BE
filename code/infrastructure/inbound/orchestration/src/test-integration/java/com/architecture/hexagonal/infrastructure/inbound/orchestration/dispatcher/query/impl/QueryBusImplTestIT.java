package com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.query.impl;

import com.architecture.hexagonal.application.usecase.business.user.findbyid.input.FindUserByUserIdInput;
import com.architecture.hexagonal.application.usecase.business.user.findbyid.usecase.FindUserByUserIdUseCase;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.user.FindUserByUserIdQueryDto;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.user.GetUsersFilteredQueryDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.TestApplication;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.transaction.TransactionBoundary;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.query.QueryBus;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.mapper.user.FindUserByUserIdQueryMapper;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.user.findbyid.FindUserByUserIdQueryHandlerImpl;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.query.FindUserByUserIdQueryDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.query.GetUsersFilteredQueryDtoTestDataBuilder;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.mockito.Mockito;

@SpringBootTest(classes = {QueryBusImpl.class, FindUserByUserIdQueryHandlerImpl.class})
@ContextConfiguration(classes = TestApplication.class)
class QueryBusImplTestIT {

  @Autowired private QueryBus queryBus;

  @MockitoSpyBean private FindUserByUserIdQueryMapper findUserByUserIdQueryMapper;

  @MockitoBean private FindUserByUserIdUseCase findUserByUserIdUseCase;

  @MockitoSpyBean private TransactionBoundary transactionBoundary;

  @Test
  void executeShouldDelegateQueryToCorrectHandler() {
    final FindUserByUserIdQueryDto query =
        FindUserByUserIdQueryDtoTestDataBuilder.builder().build().findUserByUserIdQueryDto();
    final User user = Mockito.mock(User.class);

    Mockito.when(findUserByUserIdUseCase.execute(Mockito.any(FindUserByUserIdInput.class)))
        .thenReturn(user);

    User result = queryBus.execute(query);

    AssertionsForClassTypes.assertThat(result).isSameAs(user);

    Mockito.verify(findUserByUserIdQueryMapper).toFindUserByUserIdQuery(query);
    Mockito.verify(findUserByUserIdUseCase).execute(Mockito.any(FindUserByUserIdInput.class));
    Mockito.verify(transactionBoundary).read(Mockito.any());
  }

  @Test
  void executeShouldThrowIllegalStateExceptionWhenNoHandlerIsFound() {
    final GetUsersFilteredQueryDto query =
        GetUsersFilteredQueryDtoTestDataBuilder.builder().build().getUsersFilteredQueryDto();

    AssertionsForClassTypes.assertThatThrownBy(() -> queryBus.execute(query))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("No handler found for query");
  }
}
