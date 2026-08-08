package com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.email.getblockedrules;

import com.architecture.hexagonal.application.business.email.getblockedrules.usecase.GetBlockedRulesUseCase;
import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.email.GetBlockedRulesQueryDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.transaction.TransactionBoundary;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.query.GetBlockedRulesQueryDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.transaction.TransactionBoundaryTest;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetBlockedRulesQueryHandlerImplTest {

  @InjectMocks private GetBlockedRulesQueryHandlerImpl queryHandler;

  @Mock private GetBlockedRulesUseCase getBlockedRulesUseCase;

  @Spy private TransactionBoundary transactionBoundary = new TransactionBoundaryTest();

  @Test
  void handle_shouldReturnBlockedRules_whenQueryIsExecuted() {
    final EmailBlockRulesVo blockedRules = EmailBlockRulesVo.builder().build();
    final GetBlockedRulesQueryDto queryDto =
        GetBlockedRulesQueryDtoTestDataBuilder.builder().build().getBlockedRulesQueryDto();

    Mockito.when(getBlockedRulesUseCase.execute()).thenReturn(blockedRules);

    EmailBlockRulesVo result = queryHandler.handle(queryDto);

    AssertionsForClassTypes.assertThat(result).isSameAs(blockedRules);

    Mockito.verify(getBlockedRulesUseCase).execute();
    Mockito.verify(transactionBoundary).read(Mockito.any());
  }
}
