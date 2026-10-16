package com.architecture.hexagonal.application.usecase.email.query.getblockedrules.impl;

import com.architecture.hexagonal.application.port.out.email.EmailConfigurationPort;
import com.architecture.hexagonal.application.service.email.EmailService;
import com.architecture.hexagonal.application.service.email.impl.EmailServiceImpl;
import com.architecture.hexagonal.application.testutils.usecase.email.query.GetBlockedRulesQueryTestDataBuilder;
import com.architecture.hexagonal.domain.testutils.data.model.vo.email.EmailBlockRulesVoTestDataBuilder;
import com.architecture.hexagonal.application.testutils.transaction.SpyTransactionReadBoundary;
import com.architecture.hexagonal.contract.bus.model.email.query.GetBlockedRulesQuery;
import com.architecture.hexagonal.domain.model.vo.email.EmailBlockRulesVo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.assertj.core.api.AssertionsForClassTypes;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetBlockedRulesApplicationUseCaseTestIT {

  @Mock private EmailConfigurationPort emailConfigurationPort;
  @Mock private EmailService emailService;
  @Spy private SpyTransactionReadBoundary transactionReadBoundary = new SpyTransactionReadBoundary();

  @InjectMocks private EmailServiceImpl emailServiceImpl;
  @InjectMocks private GetBlockedRulesUseCaseImpl getBlockedRulesUseCase;

  @Test
  void getBlockedRulesUseCaseShouldReturnRulesWhenQueryIsExecuted() {
    final GetBlockedRulesQuery query =
        GetBlockedRulesQueryTestDataBuilder.builder().build().getBlockedRulesQuery();
    final EmailBlockRulesVo blockedRules =
        EmailBlockRulesVoTestDataBuilder.builder().build().emailBlockRulesVo();

    Mockito.when(emailConfigurationPort.getBlockedRules()).thenReturn(blockedRules);
    Mockito.when(emailService.getBlockedRules())
        .thenAnswer(invocation -> emailServiceImpl.getBlockedRules());

    final EmailBlockRulesVo result = getBlockedRulesUseCase.handle(query);

    AssertionsForClassTypes.assertThat(result).isSameAs(blockedRules);
    Mockito.verify(transactionReadBoundary).execute(ArgumentMatchers.any());
    Mockito.verify(emailService).getBlockedRules();
    Mockito.verify(emailConfigurationPort).getBlockedRules();
  }
}
