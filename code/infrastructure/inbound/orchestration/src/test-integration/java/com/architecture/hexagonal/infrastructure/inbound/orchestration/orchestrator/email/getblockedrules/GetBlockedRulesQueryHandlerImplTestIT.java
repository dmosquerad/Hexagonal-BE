package com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.email.getblockedrules;

import com.architecture.hexagonal.application.business.email.getblockedrules.usecase.GetBlockedRulesUseCase;
import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.email.GetBlockedRulesQueryDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.TestApplication;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.query.GetBlockedRulesQueryDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.transaction.TransactionBoundary;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.mockito.Mockito;

@SpringBootTest(classes = GetBlockedRulesQueryHandlerImpl.class)
@ContextConfiguration(classes = TestApplication.class)
class GetBlockedRulesQueryHandlerImplTestIT {

  @Autowired
  private GetBlockedRulesQueryHandlerImpl getBlockedRulesQueryHandlerImpl;

  @MockitoBean
  private GetBlockedRulesUseCase getBlockedRulesUseCase;

  @MockitoSpyBean
  private TransactionBoundary transactionBoundary;

  @Test
  void getBlockedRulesQueryHandler_shouldReturnBlockedRules_whenQueryIsExecuted() {
    final GetBlockedRulesQueryDto queryDto = GetBlockedRulesQueryDtoTestDataBuilder.builder().build().getBlockedRulesQueryDto();
    final EmailBlockRulesVo blockedRules = EmailBlockRulesVo.builder().build();

    Mockito.when(getBlockedRulesUseCase.execute()).thenReturn(blockedRules);

    EmailBlockRulesVo result = getBlockedRulesQueryHandlerImpl.handle(queryDto);

    AssertionsForClassTypes.assertThat(result)
        .isSameAs(blockedRules);

    Mockito.verify(getBlockedRulesUseCase).execute();
    Mockito.verify(transactionBoundary).read(Mockito.any());
  }
}
