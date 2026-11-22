package com.architecture.hexagonal.application.mapper.converter;

import com.architecture.hexagonal.application.databuilder.usecase.user.command.CreateUserInputTestDataBuilder;
import com.architecture.hexagonal.application.databuilder.usecase.user.command.PatchUserInputTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class EmailVoConverterTest {
  @Test
  void toEmailVoShouldConvertEmail() {
    final String email =
        CreateUserInputTestDataBuilder.builder().build().createUserInput().getEmail();

    final var result = EmailVoConverter.toEmailVo(email);

    Assertions.assertThat(result.getEmail()).isEqualTo(email);
  }

  @Test
  void toOptionalEmailVoShouldReturnNullForBlankEmail() {
    final String blankEmail =
        PatchUserInputTestDataBuilder.builder().email(" ").build().patchUserInput().getEmail();

    final var result = EmailVoConverter.toOptionalEmailVo(blankEmail);

    Assertions.assertThat(result).isNull();
  }
}
