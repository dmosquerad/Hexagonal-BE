package com.architecture.hexagonal.domain.data.vo.factory;

import static org.assertj.core.api.Assertions.assertThat;

import com.architecture.hexagonal.domain.data.vo.EmailVo;
import com.architecture.hexagonal.domain.testutils.data.vo.EmailVoTestDataBuilder;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class EmailVoFactoryTest {

  @Test
  void fromShouldReturnEmailVoWhenEmailIsValid() {
    final EmailVo expected = EmailVoTestDataBuilder.builder()
      .build()
      .emailVo();

    final Optional<EmailVo> result = EmailVoFactory.from("test@example.com");

    assertThat(result).isEqualTo(Optional.of(expected));
  }

  @Test
  void fromShouldReturnEmptyWhenEmailIsInvalid() {
    final Optional<EmailVo> result = EmailVoFactory.from(null);

    assertThat(result).isEmpty();
  }
}