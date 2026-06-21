package com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.user.exists.query.handler;

import com.architecture.hexagonal.application.business.user.exists.input.UserExistsQuery;
import com.architecture.hexagonal.application.business.user.exists.usecase.UserExistsUseCase;
import com.architecture.hexagonal.domain.exception.DomainException;
import com.architecture.hexagonal.infrastructure.contract.orchestration.generated.user.UserExistsQueryDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.mapper.user.UserExistsQueryMapper;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.query.UserExistsQueryDtoTestDataBuilder;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserExistsQueryHandlerTest {

  @InjectMocks private UserExistsQueryHandler queryHandler;

  @Spy
  private UserExistsQueryMapper userExistsQueryMapper =
      Mappers.getMapper(UserExistsQueryMapper.class);

  @Mock private UserExistsUseCase userExistsUseCase;

  @Test
  void handle_shouldReturnNull_whenQueryIsExecuted() throws DomainException {
    UserExistsQueryDto queryDto =
        UserExistsQueryDtoTestDataBuilder.builder().build().userExistsQueryDto();
    Mockito.doNothing()
        .when(userExistsUseCase)
        .execute(ArgumentMatchers.any(UserExistsQuery.class));

    Void result = queryHandler.handle(queryDto);

    Mockito.verify(userExistsQueryMapper).toUserExistsQuery(queryDto);
    Mockito.verify(userExistsUseCase).execute(ArgumentMatchers.any(UserExistsQuery.class));
    Mockito.verifyNoMoreInteractions(userExistsQueryMapper, userExistsUseCase);

    AssertionsForClassTypes.assertThat(result).isNull();
  }
}
