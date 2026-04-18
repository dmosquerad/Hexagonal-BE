package com.architecture.hexagonal.domain.service;

import com.architecture.hexagonal.domain.model.entity.User;
import com.architecture.hexagonal.domain.testutils.data.entity.UserTestDataBuilder;
import com.architecture.hexagonal.domain.testutils.data.vo.EmailVoTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class EmailPolicyTest {

  @Test
  void filterAllowedEmailHost_shouldReturnTrue_whenHostIsAllowed() {
    User user = UserTestDataBuilder.builder().build().user();

    boolean result = EmailPolicy.filterAllowedEmailHost(user);

    Assertions.assertThat(result).isTrue();
  }

  @Test
  void filterAllowedEmailHost_shouldReturnFalse_whenHostIsBlocked() {
    User user = UserTestDataBuilder.builder().email(
        EmailVoTestDataBuilder.builder()
            .host("test")
            .build().emailVo())
        .build().user();

    boolean result = EmailPolicy.filterAllowedEmailHost(user);

    Assertions.assertThat(result).isFalse();
  }

  @Test
  void filterAllowedEmailHost_shouldReturnFalse_whenHostIsBlank() {
    User user = UserTestDataBuilder.builder().email(
        EmailVoTestDataBuilder.builder()
            .host("")
            .build().emailVo())
        .build().user();

    boolean result = EmailPolicy.filterAllowedEmailHost(user);

    Assertions.assertThat(result).isFalse();
  }
}
