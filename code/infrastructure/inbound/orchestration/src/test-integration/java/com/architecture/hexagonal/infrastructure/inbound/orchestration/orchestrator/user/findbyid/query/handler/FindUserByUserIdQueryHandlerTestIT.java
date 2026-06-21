package com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.user.findbyid.query.handler;

import com.architecture.hexagonal.application.business.user.findbyid.input.FindUserByUserIdQuery;
import com.architecture.hexagonal.application.business.user.findbyid.usecase.FindUserByUserIdUseCase;
import com.architecture.hexagonal.domain.exception.DomainException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.infrastructure.contract.orchestration.generated.user.FindUserByUserIdQueryDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.TestApplication;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.mapper.user.FindUserByUserIdQueryMapper;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.query.FindUserByUserIdQueryDtoTestDataBuilder;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

@SpringBootTest(classes = FindUserByUserIdQueryHandler.class)
@ContextConfiguration(classes = TestApplication.class)
class FindUserByUserIdQueryHandlerTestIT {

  @Autowired
  private FindUserByUserIdQueryHandler findUserByUserIdQueryHandler;

  @MockitoSpyBean
  private FindUserByUserIdQueryMapper findUserByUserIdQueryMapper;

  @MockitoBean
  private FindUserByUserIdUseCase findUserByUserIdUseCase;

  @Test
  void findUserByUserIdQueryHandler_shouldReturnUser_whenQueryIsExecuted() throws DomainException {
    final FindUserByUserIdQueryDto queryDto = FindUserByUserIdQueryDtoTestDataBuilder.builder().build().findUserByUserIdQueryDto();
    final User user = Mockito.mock(User.class);

    Mockito.when(findUserByUserIdUseCase.execute(ArgumentMatchers.any(FindUserByUserIdQuery.class)))
        .thenReturn(user);

    User result = findUserByUserIdQueryHandler.handle(queryDto);

    AssertionsForClassTypes.assertThat(result)
        .isSameAs(user);

    Mockito.verify(findUserByUserIdQueryMapper).toFindUserByUserIdQuery(queryDto);
    Mockito.verify(findUserByUserIdUseCase).execute(ArgumentMatchers.any(FindUserByUserIdQuery.class));
  }
}
