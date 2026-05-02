package com.architecture.hexagonal.infrastructure.inbound.cqrs.orchestrator.user.exists.query.handler;

import com.architecture.hexagonal.application.user.exists.input.UserExistsQuery;
import com.architecture.hexagonal.application.user.exists.usecase.UserExistsUseCase;
import com.architecture.hexagonal.domain.exception.DomainException;
import com.architecture.hexagonal.infrastructure.contract.cqrs.generated.user.UserExistsQueryDto;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.config.TestApplication;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.mapper.user.UserExistsQueryMapper;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.orchestrator.user.exists.query.handler.UserExistsQueryHandler;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.testutils.data.query.UserExistsQueryDtoTestDataBuilder;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

@SpringBootTest(classes = UserExistsQueryHandler.class)
@ContextConfiguration(classes = TestApplication.class)
class UserExistsQueryHandlerTestIT {

  @Autowired
  private UserExistsQueryHandler userExistsQueryHandler;

  @MockitoSpyBean
  private UserExistsQueryMapper userExistsQueryMapper;

  @MockitoBean
  private UserExistsUseCase userExistsUseCase;

  @Test
  void userExistsQueryHandler_shouldExecuteQuery_whenQueryIsExecuted() throws DomainException {
    final UserExistsQueryDto queryDto = UserExistsQueryDtoTestDataBuilder.builder().build().userExistsQueryDto();

    Void result = userExistsQueryHandler.handle(queryDto);

    AssertionsForClassTypes.assertThat(result)
        .isNull();

    Mockito.verify(userExistsQueryMapper).toUserExistsQuery(queryDto);
    Mockito.verify(userExistsUseCase).execute(ArgumentMatchers.any(UserExistsQuery.class));
  }
}
