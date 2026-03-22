package com.architecture.hexagonal.infrastructure.outbound.database.mapper.util;

import static org.assertj.core.api.Assertions.assertThat;

import com.architecture.hexagonal.domain.data.vo.EmailVo;
import com.architecture.hexagonal.infrastructure.outbound.database.testutils.data.vo.EmailVoTestDataBuilder;
import org.junit.jupiter.api.Test;

class ToEmailVoUtilTest {

  @Test
  void toEmailVoShouldConvertValidEmail() {
    final EmailVo result = ToEmailVoUtil.toEmailVo("test@example.com");
    final EmailVo expected = EmailVoTestDataBuilder.builder()
      .build()
      .emailVo();

    assertThat(result).isEqualTo(expected);
  }

  @Test
  void toEmailVoShouldReturnEmptyEmailVoWhenInputIsNull() {
    final EmailVo result = ToEmailVoUtil.toEmailVo(null);

    assertThat(result).isEqualTo(EmailVoTestDataBuilder.builder()
        .username(null)
        .domain(null)
        .build()
        .emailVo());
  }
}
