package com.architecture.hexagonal.application.usecase.business.email.getblockedrules.impl;

import com.architecture.hexagonal.application.port.out.transaction.TransactionReadBoundary;
import com.architecture.hexagonal.application.service.business.email.EmailService;
import com.architecture.hexagonal.application.testutils.model.vo.email.EmailBlockRulesVoTestDataBuilder;
import com.architecture.hexagonal.application.testutils.orchestration.data.query.GetBlockedRulesQueryTestDataBuilder;
import com.architecture.hexagonal.application.testutils.transaction.SpyTransactionReadBoundary;
import com.architecture.hexagonal.domain.model.vo.email.EmailBlockRulesVo;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetBlockedRulesUseCaseImplTest {

  @Mock private EmailService emailService;

  @Spy private TransactionReadBoundary transactionBoundary = new SpyTransactionReadBoundary();

  @InjectMocks private GetBlockedRulesUseCaseImpl useCase;

  @Test
  void handleShouldReturnBlockedRulesWhenQueryIsExecuted() {
    final EmailBlockRulesVo blockedRules =
        EmailBlockRulesVoTestDataBuilder.builder().build().emailBlockRulesVo();
    Mockito.when(emailService.getBlockedRules()).thenReturn(blockedRules);

    final EmailBlockRulesVo result =
        useCase.handle(
            GetBlockedRulesQueryTestDataBuilder.builder().build().getBlockedRulesQuery());

    AssertionsForClassTypes.assertThat(result).isSameAs(blockedRules);
    Mockito.verify(emailService).getBlockedRules();
    Mockito.verify(transactionBoundary).execute(Mockito.any());
  }
}
