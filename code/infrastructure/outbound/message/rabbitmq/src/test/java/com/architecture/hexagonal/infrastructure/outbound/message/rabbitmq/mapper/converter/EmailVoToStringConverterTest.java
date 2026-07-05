package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.mapper.converter;

import com.architecture.hexagonal.domain.model.vo.EmailVo;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.data.vo.EmailVoTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class EmailVoToStringConverterTest {

  @Test
  void toEmail_shouldConvertEmailVo_whenEmailVoIsValid() {
    final EmailVo emailVo = EmailVoTestDataBuilder.builder().build().emailVo();

    final String result = EmailVoToStringConverter.toEmail(emailVo);

    Assertions.assertThat(result).isEqualTo("test@example.com");
  }

  @Test
  void toEmail_shouldReturnEmpty_whenEmailVoIsNull() {
    final String result = EmailVoToStringConverter.toEmail(null);

    Assertions.assertThat(result).isEmpty();
  }
}
