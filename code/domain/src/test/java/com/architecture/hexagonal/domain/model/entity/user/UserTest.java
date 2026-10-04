package com.architecture.hexagonal.domain.model.entity.user;

import com.architecture.hexagonal.domain.testutils.data.model.entity.user.UserTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class UserTest {

  @Test
  void getIdShouldReturnUserId() {
    final User user = UserTestDataBuilder.builder().build().user();

    Assertions.assertThat(user.getId()).isEqualTo(user.userId());
  }
}
