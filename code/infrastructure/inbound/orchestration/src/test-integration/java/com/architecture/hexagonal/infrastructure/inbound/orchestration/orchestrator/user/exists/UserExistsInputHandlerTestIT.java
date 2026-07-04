package com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.user.exists;

import com.architecture.hexagonal.application.business.user.exists.input.UserExistsInput;
import com.architecture.hexagonal.application.business.user.exists.usecase.UserExistsUseCase;
import com.architecture.hexagonal.infrastructure.contract.orchestration.generated.user.UserExistsQueryDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.TestApplication;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.mapper.user.UserExistsQueryMapper;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.query.UserExistsQueryDtoTestDataBuilder;
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

@SpringBootTest(classes = UserExistsQueryHandlerImpl.class)
@ContextConfiguration(classes = TestApplication.class)
class UserExistsInputHandlerTestIT {

  @Autowired
  private UserExistsQueryHandlerImpl userExistsQueryHandlerImpl;

  @MockitoSpyBean
  private UserExistsQueryMapper userExistsQueryMapper;

  @MockitoBean
  private UserExistsUseCase userExistsUseCase;

  @MockitoSpyBean
  private TransactionBoundary transactionBoundary;

  @Test
  void userExistsQueryHandler_shouldExecuteQuery_whenQueryIsExecuted() {
    final UserExistsQueryDto queryDto = UserExistsQueryDtoTestDataBuilder.builder().build().userExistsQueryDto();

    Mockito.doNothing()
        .when(userExistsUseCase)
        .execute(ArgumentMatchers.any(UserExistsInput.class));

    Void result = userExistsQueryHandlerImpl.handle(queryDto);

    AssertionsForClassTypes.assertThat(result)
        .isNull();

    Mockito.verify(userExistsQueryMapper).toUserExistsQuery(queryDto);
    Mockito.verify(userExistsUseCase).execute(ArgumentMatchers.any(UserExistsInput.class));
    Mockito.verify(transactionBoundary).read(Mockito.any());
  }
}
