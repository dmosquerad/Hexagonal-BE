package com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.user.exists;

import com.architecture.hexagonal.application.business.user.exists.input.UserExistsInput;
import com.architecture.hexagonal.application.business.user.exists.usecase.UserExistsUseCase;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.user.UserExistsQueryDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.transaction.TransactionBoundary;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.mapper.user.UserExistsQueryMapper;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.query.UserExistsQueryDtoTestDataBuilder;
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
class UserExistsInputHandlerTest {

  @InjectMocks private UserExistsQueryHandlerImpl queryHandler;

  @Spy
  private UserExistsQueryMapper userExistsQueryMapper =
      Mappers.getMapper(UserExistsQueryMapper.class);

  @Mock private UserExistsUseCase userExistsUseCase;

  @Spy private TransactionBoundary transactionBoundary = new TransactionBoundaryTest();

  @Test
  void handle_shouldReturnNull_whenQueryIsExecuted() {
    UserExistsQueryDto queryDto =
        UserExistsQueryDtoTestDataBuilder.builder().build().userExistsQueryDto();
    Mockito.doNothing()
        .when(userExistsUseCase)
        .execute(ArgumentMatchers.any(UserExistsInput.class));

    Void result = queryHandler.handle(queryDto);

    AssertionsForClassTypes.assertThat(result).isNull();

    Mockito.verify(userExistsQueryMapper).toUserExistsQuery(queryDto);
    Mockito.verify(userExistsUseCase).execute(ArgumentMatchers.any(UserExistsInput.class));
    Mockito.verifyNoMoreInteractions(userExistsQueryMapper, userExistsUseCase);
    Mockito.verify(transactionBoundary).read(Mockito.any());
  }
}
