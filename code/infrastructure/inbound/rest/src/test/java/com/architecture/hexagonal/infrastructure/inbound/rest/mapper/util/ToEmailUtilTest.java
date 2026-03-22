package com.architecture.hexagonal.infrastructure.inbound.rest.mapper.util;

import static org.assertj.core.api.Assertions.assertThat;

import com.architecture.hexagonal.domain.data.vo.EmailVo;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.vo.EmailVoTestDataBuilder;
import org.junit.jupiter.api.Test;

class ToEmailUtilTest {

  @Test
  void toEmailShouldConvertEmailVo() {
    final EmailVo emailVo = EmailVoTestDataBuilder.builder()
      .build()
      .emailVo();

    final String result = ToEmailUtil.toEmail(emailVo);

    assertThat(result).isEqualTo("test@example.com");
  }

  @Test
  void toEmailShouldReturnEmptyWhenEmailVoIsNull() {
    final String result = ToEmailUtil.toEmail(null);

    assertThat(result).isEmpty();
  }

}
