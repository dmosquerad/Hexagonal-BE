package com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.user.findbyid;

import com.architecture.hexagonal.application.business.user.findbyid.input.FindUserByUserIdInput;
import com.architecture.hexagonal.application.business.user.findbyid.usecase.FindUserByUserIdUseCase;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.infrastructure.contract.orchestration.generated.user.FindUserByUserIdQueryDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.TestApplication;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.mapper.user.FindUserByUserIdQueryMapper;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.query.FindUserByUserIdQueryDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.TransactionBoundary;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

@SpringBootTest(classes = FindUserByUserIdQueryHandlerImpl.class)
@ContextConfiguration(classes = TestApplication.class)
class FindUserByUserIdInputHandlerTestIT {

  @Autowired
  private FindUserByUserIdQueryHandlerImpl findUserByUserIdQueryHandlerImpl;

  @MockitoSpyBean
  private FindUserByUserIdQueryMapper findUserByUserIdQueryMapper;

  @MockitoBean
  private FindUserByUserIdUseCase findUserByUserIdUseCase;

  @MockitoSpyBean
  private TransactionBoundary transactionBoundary;

  @Test
  void findUserByUserIdQueryHandler_shouldReturnUser_whenQueryIsExecuted() {
    final FindUserByUserIdQueryDto queryDto = FindUserByUserIdQueryDtoTestDataBuilder.builder().build().findUserByUserIdQueryDto();
    final User user = Mockito.mock(User.class);

    Mockito.when(findUserByUserIdUseCase.execute(ArgumentMatchers.any(FindUserByUserIdInput.class)))
        .thenReturn(user);

    User result = findUserByUserIdQueryHandlerImpl.handle(queryDto);

    AssertionsForClassTypes.assertThat(result)
        .isSameAs(user);

    Mockito.verify(findUserByUserIdQueryMapper).toFindUserByUserIdQuery(queryDto);
    Mockito.verify(findUserByUserIdUseCase).execute(ArgumentMatchers.any(FindUserByUserIdInput.class));
    Mockito.verify(transactionBoundary).read(Mockito.any());
  }
}
