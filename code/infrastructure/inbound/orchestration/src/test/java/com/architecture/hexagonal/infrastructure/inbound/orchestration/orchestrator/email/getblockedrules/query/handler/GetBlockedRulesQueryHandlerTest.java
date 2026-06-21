package com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.email.getblockedrules.query.handler;

import com.architecture.hexagonal.application.business.email.getblockedrules.usecase.GetBlockedRulesUseCase;
import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import com.architecture.hexagonal.infrastructure.contract.orchestration.generated.email.GetBlockedRulesQueryDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.query.GetBlockedRulesQueryDtoTestDataBuilder;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetBlockedRulesQueryHandlerTest {

  @InjectMocks private GetBlockedRulesQueryHandler queryHandler;

  @Mock private GetBlockedRulesUseCase getBlockedRulesUseCase;

  @Test
  void handle_shouldReturnBlockedRules_whenQueryIsExecuted() {
    final EmailBlockRulesVo blockedRules = EmailBlockRulesVo.builder().build();
    final GetBlockedRulesQueryDto queryDto =
        GetBlockedRulesQueryDtoTestDataBuilder.builder().build().getBlockedRulesQueryDto();

    Mockito.when(getBlockedRulesUseCase.execute()).thenReturn(blockedRules);

    EmailBlockRulesVo result = queryHandler.handle(queryDto);

    AssertionsForClassTypes.assertThat(result).isSameAs(blockedRules);

    Mockito.verify(getBlockedRulesUseCase).execute();
  }
}
