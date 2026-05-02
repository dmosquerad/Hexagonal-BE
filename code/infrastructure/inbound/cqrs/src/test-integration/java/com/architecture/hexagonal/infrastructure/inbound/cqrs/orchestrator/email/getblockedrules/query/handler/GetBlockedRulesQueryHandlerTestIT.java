package com.architecture.hexagonal.infrastructure.inbound.cqrs.orchestrator.email.getblockedrules.query.handler;

import com.architecture.hexagonal.application.email.getblockedrules.usecase.GetBlockedRulesUseCase;
import com.architecture.hexagonal.domain.exception.DomainException;
import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import com.architecture.hexagonal.infrastructure.contract.cqrs.generated.email.GetBlockedRulesQueryDto;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.config.TestApplication;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.orchestrator.email.getblockedrules.query.handler.GetBlockedRulesQueryHandler;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.testutils.data.query.GetBlockedRulesQueryDtoTestDataBuilder;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.mockito.Mockito;

@SpringBootTest(classes = GetBlockedRulesQueryHandler.class)
@ContextConfiguration(classes = TestApplication.class)
class GetBlockedRulesQueryHandlerTestIT {

  @Autowired
  private GetBlockedRulesQueryHandler getBlockedRulesQueryHandler;

  @MockitoBean
  private GetBlockedRulesUseCase getBlockedRulesUseCase;

  @Test
  void getBlockedRulesQueryHandler_shouldReturnBlockedRules_whenQueryIsExecuted() throws DomainException {
    final GetBlockedRulesQueryDto queryDto = GetBlockedRulesQueryDtoTestDataBuilder.builder().build().getBlockedRulesQueryDto();
    final EmailBlockRulesVo blockedRules = EmailBlockRulesVo.builder().build();

    Mockito.when(getBlockedRulesUseCase.execute()).thenReturn(blockedRules);

    EmailBlockRulesVo result = getBlockedRulesQueryHandler.handle(queryDto);

    AssertionsForClassTypes.assertThat(result)
        .isSameAs(blockedRules);

    Mockito.verify(getBlockedRulesUseCase).execute();
  }
}
