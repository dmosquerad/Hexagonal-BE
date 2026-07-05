package com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.user.findbyid;

import com.architecture.hexagonal.application.business.user.findbyid.input.FindUserByUserIdInput;
import com.architecture.hexagonal.application.business.user.findbyid.usecase.FindUserByUserIdUseCase;
import com.architecture.hexagonal.domain.model.aggregate.user.User;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.user.FindUserByUserIdQueryDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.transaction.TransactionBoundary;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.mapper.user.FindUserByUserIdQueryMapper;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.aggregate.UserTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.query.FindUserByUserIdQueryDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.transaction.TransactionBoundaryTest;
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
class FindUserByUserIdInputHandlerTest {

  @InjectMocks private FindUserByUserIdQueryHandlerImpl queryHandler;

  @Spy
  private FindUserByUserIdQueryMapper findUserByUserIdQueryMapper =
      Mappers.getMapper(FindUserByUserIdQueryMapper.class);

  @Mock private FindUserByUserIdUseCase findUserByUserIdUseCase;

  @Spy private TransactionBoundary transactionBoundary = new TransactionBoundaryTest();

  @Test
  void handle_shouldReturnUser_whenQueryIsExecuted() {
    FindUserByUserIdQueryDto queryDto =
        FindUserByUserIdQueryDtoTestDataBuilder.builder().build().findUserByUserIdQueryDto();
    User user = UserTestDataBuilder.builder().build().user();
    Mockito.when(findUserByUserIdUseCase.execute(ArgumentMatchers.any(FindUserByUserIdInput.class)))
        .thenReturn(user);

    User result = queryHandler.handle(queryDto);

    AssertionsForClassTypes.assertThat(result).isSameAs(user);

    Mockito.verify(findUserByUserIdQueryMapper).toFindUserByUserIdQuery(queryDto);
    Mockito.verify(findUserByUserIdUseCase)
        .execute(ArgumentMatchers.any(FindUserByUserIdInput.class));
    Mockito.verify(transactionBoundary).read(Mockito.any());
  }
}
