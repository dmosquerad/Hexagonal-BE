package com.architecture.hexagonal.application.usercase;

import com.architecture.hexagonal.application.feature.user.create.command.CreateUserCommand;
import com.architecture.hexagonal.application.port.configuration.EmailConfigurationPort;
import com.architecture.hexagonal.application.port.message.UserSenderPort;
import com.architecture.hexagonal.application.port.database.UserRepositoryWritePort;
import com.architecture.hexagonal.application.testutils.data.aggregate.UserTestDataBuilder;
import com.architecture.hexagonal.application.testutils.data.input.command.CreateUserCommandTestDataBuilder;
import com.architecture.hexagonal.application.testutils.data.vo.EmailBlockRulesVoTestDataBuilder;
import com.architecture.hexagonal.application.feature.user.create.usecase.CreateUserUseCase;
import com.architecture.hexagonal.domain.exception.InvalidValueException;
import com.architecture.hexagonal.domain.model.aggregate.User;
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
class CreateUserUseCaseTest {

  @InjectMocks
  CreateUserUseCase createUserUseCase;

  @Mock
  UserRepositoryWritePort userRepositoryWritePort;

  @Mock
  UserSenderPort userSenderPort;

  @Mock
  EmailConfigurationPort emailConfigurationPort;

  @Test
  void execute_shouldCreateUser_whenCommandIsValid() throws InvalidValueException {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();

    final CreateUserCommand createUserCommand = CreateUserCommandTestDataBuilder
        .builder()
        .build()
        .createUserCommand();

    Mockito.when(emailConfigurationPort.getBlockedRules())
        .thenReturn(EmailBlockRulesVoTestDataBuilder.builder().build().emailBlockRulesVo());
    Mockito.when(userRepositoryWritePort.saveUser(ArgumentMatchers.any(User.class)))
        .thenReturn(user);

    User result = createUserUseCase.execute(createUserCommand);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(user);

    Mockito.verify(emailConfigurationPort).getBlockedRules();
    Mockito.verify(userRepositoryWritePort).saveUser(ArgumentMatchers.any(User.class));
    Mockito.verify(userSenderPort).userSenderCreated(user);
  }

  @Test
  void execute_shouldCreateUser_whenEmailIsAllowed() throws InvalidValueException {
    final User user = UserTestDataBuilder.builder().build().user();
    final CreateUserCommand createUserCommand = CreateUserCommandTestDataBuilder
            .builder()
            .build()
            .createUserCommand();

    Mockito.when(emailConfigurationPort.getBlockedRules())
            .thenReturn(EmailBlockRulesVoTestDataBuilder.builder().build().emailBlockRulesVo());
    Mockito.when(userRepositoryWritePort.saveUser(Mockito.any(User.class)))
            .thenReturn(user);

    final User result = createUserUseCase.execute(createUserCommand);

    AssertionsForClassTypes.assertThat(result).isEqualTo(user);
    Mockito.verify(emailConfigurationPort).getBlockedRules();
    Mockito.verify(userRepositoryWritePort).saveUser(Mockito.any(User.class));
    Mockito.verify(userSenderPort).userSenderCreated(user);
  }

  @Test
  void execute_shouldThrowPolicyViolationException_whenEmailIsBlocked() {
    final CreateUserCommand createUserCommand = CreateUserCommandTestDataBuilder
            .builder()
            .build()
            .createUserCommand();

    Mockito.when(emailConfigurationPort.getBlockedRules())
            .thenReturn(EmailBlockRulesVoTestDataBuilder.builder()
                .email(Set.of("test@example.com"))
                .build()
                .emailBlockRulesVo());

    AssertionsForClassTypes.assertThatThrownBy(() -> createUserUseCase.execute(createUserCommand))
            .isInstanceOf(InvalidValueException.class);

    Mockito.verifyNoInteractions(userRepositoryWritePort);
    Mockito.verifyNoInteractions(userSenderPort);
  }

}
