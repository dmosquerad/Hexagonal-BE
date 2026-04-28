package com.architecture.hexagonal.infrastructure.outbound.message.mapper.converter;

import static org.assertj.core.api.Assertions.assertThat;

import com.architecture.hexagonal.domain.model.vo.EmailVo;
import com.architecture.hexagonal.infrastructure.outbound.message.testutils.data.vo.EmailVoTestDataBuilder;
import org.junit.jupiter.api.Test;

class EmailVoToStringConverterTest {

  @Test
  void toEmail_shouldConvertEmailVo_whenEmailVoIsValid() {
    final EmailVo emailVo = EmailVoTestDataBuilder.builder()
        .build()
        .emailVo();

    final String result = EmailVoToStringConverter.toEmail(emailVo);

    assertThat(result).isEqualTo("test@example.com");
  }

  @Test
  void toEmail_shouldReturnEmpty_whenEmailVoIsNull() {
    final String result = EmailVoToStringConverter.toEmail(null);

    assertThat(result).isEmpty();
  }
}
