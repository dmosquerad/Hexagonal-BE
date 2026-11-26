package com.architecture.hexagonal.application.mapper.converter;

import com.architecture.hexagonal.application.databuilder.model.command.user.CreateUserInputTestDataBuilder;
import com.architecture.hexagonal.application.databuilder.model.command.user.PatchUserInputTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class EmailVoConverterTest {
  @Test
  void toEmailVoShouldConvertEmail() {
    final String email = CreateUserInputTestDataBuilder.builder().build().createUserInput().email();

    final var result = EmailVoConverter.toEmailVo(email);

    Assertions.assertThat(result.getEmail()).isEqualTo(email);
  }

  @Test
  void toOptionalEmailVoShouldReturnNullForBlankEmail() {
    final String blankEmail =
        PatchUserInputTestDataBuilder.builder().email(" ").build().patchUserInput().email();

    final var result = EmailVoConverter.toOptionalEmailVo(blankEmail);

    Assertions.assertThat(result).isNull();
  }
}
