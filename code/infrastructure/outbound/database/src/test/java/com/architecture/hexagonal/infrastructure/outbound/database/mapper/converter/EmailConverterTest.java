package com.architecture.hexagonal.infrastructure.outbound.database.mapper.converter;

import static org.assertj.core.api.Assertions.assertThat;

import com.architecture.hexagonal.domain.model.vo.EmailVo;
import com.architecture.hexagonal.infrastructure.outbound.database.testutils.data.vo.EmailVoTestDataBuilder;
import org.junit.jupiter.api.Test;

class EmailConverterTest {

  @Test
  void toEmail_shouldConvertEmailVo_whenEmailVoIsValid() {
    final EmailVo emailVo = EmailVoTestDataBuilder.builder()
      .build()
      .emailVo();

    final String result = EmailConverter.toEmail(emailVo);

    assertThat(result).isEqualTo("test@example.com");
  }

  @Test
  void toEmail_shouldReturnEmpty_whenEmailVoIsNull() {
    final String result = EmailConverter.toEmail(null);

    assertThat(result).isEmpty();
  }
}
