package com.architecture.hexagonal.application.usercase;

import com.architecture.hexagonal.application.port.out.EmailConfigurationPort;
import com.architecture.hexagonal.application.testutils.data.vo.EmailBlockRulesVoTestDataBuilder;
import com.architecture.hexagonal.application.usecase.GetBlockedRulesUseCase;
import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(classes = {GetBlockedRulesUseCase.class})
class GetBlockedRulesUseCaseTestIT {

  @Autowired
  GetBlockedRulesUseCase getBlockedRulesUseCase;

  @MockitoBean
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
