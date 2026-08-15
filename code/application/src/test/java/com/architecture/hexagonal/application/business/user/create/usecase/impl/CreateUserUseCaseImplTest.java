package com.architecture.hexagonal.application.business.user.create.usecase.impl;

import com.architecture.hexagonal.application.business.user.create.input.CreateUserInput;
import com.architecture.hexagonal.application.port.configuration.EmailConfigurationPort;
import com.architecture.hexagonal.application.port.database.UserRepositoryWritePort;
import com.architecture.hexagonal.application.port.message.UserSenderPort;
import com.architecture.hexagonal.application.testutils.data.aggregate.user.UserTestDataBuilder;
import com.architecture.hexagonal.application.testutils.data.vo.EmailBlockRulesVoTestDataBuilder;
import com.architecture.hexagonal.application.testutils.user.create.input.CreateUserInputTestDataBuilder;
import com.architecture.hexagonal.domain.exception.InvalidValueException;
import com.architecture.hexagonal.domain.model.aggregate.user.User;
import java.util.Set;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseImplTest {

  @InjectMocks CreateUserUseCaseImpl createUserUseCaseImpl;

  @Mock UserRepositoryWritePort userRepositoryWritePort;

  @Mock UserSenderPort userSenderPort;

  @Mock EmailConfigurationPort emailConfigurationPort;

  @Test
  void execute_shouldCreateUser_whenEmailIsAllowed() throws InvalidValueException {
    final User user = UserTestDataBuilder.builder().build().user();
    final CreateUserInput createUserInput =
        CreateUserInputTestDataBuilder.builder().build().createUserInput();

    Mockito.when(emailConfigurationPort.getBlockedRules())
        .thenReturn(EmailBlockRulesVoTestDataBuilder.builder().build().emailBlockRulesVo());
    Mockito.when(userRepositoryWritePort.saveUser(ArgumentMatchers.any(User.class)))
        .thenReturn(user);

    final User result = createUserUseCaseImpl.execute(createUserInput);

    AssertionsForClassTypes.assertThat(result).usingRecursiveComparison().isEqualTo(user);

    Mockito.verify(emailConfigurationPort).getBlockedRules();
    Mockito.verify(userRepositoryWritePort).saveUser(ArgumentMatchers.any(User.class));
    Mockito.verify(userSenderPort).userSenderCreated(user);
  }

  @Test
  void execute_shouldThrowInvalidValueException_whenEmailIsBlocked() {
    final CreateUserInput createUserInput =
        CreateUserInputTestDataBuilder.builder().build().createUserInput();

    Mockito.when(emailConfigurationPort.getBlockedRules())
        .thenReturn(
            EmailBlockRulesVoTestDataBuilder.builder()
                .email(Set.of("test@example.com"))
                .build()
                .emailBlockRulesVo());

    AssertionsForClassTypes.assertThatThrownBy(() -> createUserUseCaseImpl.execute(createUserInput))
        .isInstanceOf(InvalidValueException.class);

    Mockito.verifyNoInteractions(userRepositoryWritePort);
    Mockito.verifyNoInteractions(userSenderPort);
  }
}
