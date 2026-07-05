package com.architecture.hexagonal.infrastructure.outbound.database.postgresql.mapper.converter;

import com.architecture.hexagonal.domain.model.vo.EmailVo;
import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.testutils.data.vo.EmailVoTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class EmailVoConverterTest {

  @Test
  void toEmailVo_shouldReturnEmailVo_whenEmailIsValid() {
    final EmailVo result = EmailVoConverter.toEmailVo("test@example.com");
    final EmailVo expected = EmailVoTestDataBuilder.builder().build().emailVo();

    Assertions.assertThat(result).isEqualTo(expected);
  }

  @Test
  void toEmailVo_shouldReturnEmpty_whenInputIsNull() {
    final EmailVo result = EmailVoConverter.toEmailVo(null);

    Assertions.assertThat(result)
        .isEqualTo(
            EmailVoTestDataBuilder.builder().username(null).host(null).tld(null).build().emailVo());
  }
}
