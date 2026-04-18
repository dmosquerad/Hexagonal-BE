package com.architecture.hexagonal.domain.model.entity.predicate;

import com.architecture.hexagonal.domain.model.entity.User;
import com.architecture.hexagonal.domain.testutils.data.entity.UserTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class UserPredicateTest {

  @Test
  void hasEmailHost_shouldReturnTrue_whenHostMatches() {
    User user = UserTestDataBuilder.builder().build().user();

    boolean result = UserPredicate.hasEmail(email -> email.getHost().equals("example")).test(user);

    Assertions.assertThat(result).isTrue();
  }

  @Test
  void hasEmailHost_shouldReturnFalse_whenHostDoesNotMatch() {
    User user = UserTestDataBuilder.builder().build().user();

    boolean result = UserPredicate.hasEmail(email -> email.getHost().equals("test")).test(user);

    Assertions.assertThat(result).isFalse();
  }

  @Test
  void hasEmailHostFactory_shouldReturnTrue_whenHostMatchesCaseInsensitive() {
    User user = UserTestDataBuilder.builder().build().user();

    boolean result = UserPredicate.hasEmailHost("example").test(user);

    Assertions.assertThat(result).isTrue();
  }

  @Test
  void hasEmailHostFactory_shouldReturnFalse_whenHostIsBlank() {
    User user = UserTestDataBuilder.builder().build().user();

    boolean result = UserPredicate.hasEmailHost("").test(user);

    Assertions.assertThat(result).isFalse();
  }
}
