package com.architecture.hexagonal.infrastructure.outbound.database.postgresql.mapper.converter;

import com.architecture.hexagonal.domain.model.vo.EmailVo;
import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.testutils.data.vo.EmailVoTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class EmailConverterTest {

  @Test
  void toEmail_shouldConvertEmailVo_whenEmailVoIsValid() {
    final EmailVo emailVo = EmailVoTestDataBuilder.builder().build().emailVo();

    final String result = EmailConverter.toEmail(emailVo);

    Assertions.assertThat(result).isEqualTo("test@example.com");
  }

  @Test
  void toEmail_shouldReturnEmpty_whenEmailVoIsNull() {
    final String result = EmailConverter.toEmail(null);

    Assertions.assertThat(result).isEmpty();
  }
}
