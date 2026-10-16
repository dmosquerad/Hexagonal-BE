package com.architecture.hexagonal.infrastructure.inbound.endpoint.rest.mapper.converter;

import com.architecture.hexagonal.domain.model.vo.email.EmailVo;
import com.architecture.hexagonal.domain.testutils.data.model.vo.email.EmailVoTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class EmailConverterTest {

  @Test
  void toEmailShouldConvertEmailVoWhenEmailVoIsValid() {
    final EmailVo emailVo = EmailVoTestDataBuilder.builder().build().emailVo();

    final String result = EmailConverter.toEmail(emailVo);

    Assertions.assertThat(result).isEqualTo("test@example.com");
  }

  @Test
  void toEmailShouldReturnEmptyWhenEmailVoIsNull() {
    final String result = EmailConverter.toEmail(null);

    Assertions.assertThat(result).isEmpty();
  }
}
