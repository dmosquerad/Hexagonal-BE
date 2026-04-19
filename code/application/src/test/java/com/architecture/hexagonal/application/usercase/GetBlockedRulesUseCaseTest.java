package com.architecture.hexagonal.application.usercase;

import com.architecture.hexagonal.application.port.out.EmailConfigurationPort;
import com.architecture.hexagonal.application.testutils.data.vo.EmailBlockRulesVoTestDataBuilder;
import com.architecture.hexagonal.application.usecase.GetBlockedRulesUseCase;
import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetBlockedRulesUseCaseTest {

  @InjectMocks
  GetBlockedRulesUseCase getBlockedRulesUseCase;

  @Mock
  EmailConfigurationPort emailConfigurationPort;

  @Test
  void execute_shouldReturnBlockedRules_whenConfigurationIsAvailable() {
    final EmailBlockRulesVo emailBlockRulesVo = EmailBlockRulesVoTestDataBuilder.builder()
        .build()
        .emailBlockRulesVo();

    Mockito.when(emailConfigurationPort.getBlockedRules()).thenReturn(emailBlockRulesVo);

    EmailBlockRulesVo result = getBlockedRulesUseCase.execute();

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(emailBlockRulesVo);

    Mockito.verify(emailConfigurationPort).getBlockedRules();
  }
}
