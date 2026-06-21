package com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.user.findbyid.query.handler;

import com.architecture.hexagonal.application.business.user.findbyid.input.FindUserByUserIdQuery;
import com.architecture.hexagonal.application.business.user.findbyid.usecase.FindUserByUserIdUseCase;
import com.architecture.hexagonal.domain.exception.DomainException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.infrastructure.contract.orchestration.generated.user.FindUserByUserIdQueryDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.mapper.user.FindUserByUserIdQueryMapper;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.aggregate.UserTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.query.FindUserByUserIdQueryDtoTestDataBuilder;
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
class FindUserByUserIdQueryHandlerTest {

  @InjectMocks private FindUserByUserIdQueryHandler queryHandler;

  @Spy
  private FindUserByUserIdQueryMapper findUserByUserIdQueryMapper =
      Mappers.getMapper(FindUserByUserIdQueryMapper.class);

  @Mock private FindUserByUserIdUseCase findUserByUserIdUseCase;

  @Test
  void handle_shouldReturnUser_whenQueryIsExecuted() throws DomainException {
    FindUserByUserIdQueryDto queryDto =
        FindUserByUserIdQueryDtoTestDataBuilder.builder().build().findUserByUserIdQueryDto();
    User user = UserTestDataBuilder.builder().build().user();
    Mockito.when(findUserByUserIdUseCase.execute(ArgumentMatchers.any(FindUserByUserIdQuery.class)))
        .thenReturn(user);

    User result = queryHandler.handle(queryDto);

    AssertionsForClassTypes.assertThat(result).isSameAs(user);

    Mockito.verify(findUserByUserIdQueryMapper).toFindUserByUserIdQuery(queryDto);
    Mockito.verify(findUserByUserIdUseCase)
        .execute(ArgumentMatchers.any(FindUserByUserIdQuery.class));
  }
}
