package com.architecture.hexagonal.application.usercase;

import com.architecture.hexagonal.application.cqrs.command.request.UpdateUserCommand;
import com.architecture.hexagonal.application.port.out.EmailConfigurationPort;
import com.architecture.hexagonal.application.port.out.UserRepositoryReadPort;
import com.architecture.hexagonal.application.port.out.UserRepositoryWritePort;
import com.architecture.hexagonal.application.port.out.UserSenderPort;
import com.architecture.hexagonal.application.testutils.data.entity.UserTestDataBuilder;
import com.architecture.hexagonal.application.testutils.data.input.command.UpdateUserCommandTestDataBuilder;
import com.architecture.hexagonal.application.testutils.data.vo.EmailBlockRulesVoTestDataBuilder;
import com.architecture.hexagonal.application.usecase.UpdateUserUseCase;
import com.architecture.hexagonal.domain.exception.InvalidValueException;
import com.architecture.hexagonal.domain.model.entity.User;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import java.util.Optional;
import java.util.Set;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateUserUseCaseTest {

  @InjectMocks
  UpdateUserUseCase updateUserUseCase;

  @Mock
  UserRepositoryReadPort userRepositoryReadPort;

  @Mock
  UserRepositoryWritePort userRepositoryWritePort;

  @Mock
  UserSenderPort userSenderPort;

  @Mock
  EmailConfigurationPort emailConfigurationPort;

  @Test
  void execute_shouldUpdateUser_whenUserExists() throws ResourceNotFoundException, InvalidValueException {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();
    final UpdateUserCommand updateUserCommand = UpdateUserCommandTestDataBuilder
        .builder()
        .build()
        .updateUserCommand();

    Mockito.when(userRepositoryReadPort.findUserById(updateUserCommand.getUserId()))
        .thenReturn(Optional.of(user));
    Mockito.when(emailConfigurationPort.getBlockedRules())
        .thenReturn(EmailBlockRulesVoTestDataBuilder.builder().build().emailBlockRulesVo());
    Mockito.when(userRepositoryWritePort.saveUser(user))
        .thenReturn(user);

    final User result = updateUserUseCase.execute(updateUserCommand);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(user);

    Mockito.verify(userRepositoryReadPort).findUserById(updateUserCommand.getUserId());
    Mockito.verify(emailConfigurationPort).getBlockedRules();
    Mockito.verify(userRepositoryWritePort).saveUser(user);
    Mockito.verify(userSenderPort).userSenderUpdated(user);
  }

  @Test
  void execute_shouldThrowPolicyViolationException_whenEmailIsBlocked() {
    final User user = UserTestDataBuilder.builder().build().user();
    final UpdateUserCommand updateUserCommand = UpdateUserCommandTestDataBuilder
        .builder()
        .build()
        .updateUserCommand();

    Mockito.when(userRepositoryReadPort.findUserById(updateUserCommand.getUserId()))
        .thenReturn(Optional.of(user));
    Mockito.when(emailConfigurationPort.getBlockedRules())
        .thenReturn(EmailBlockRulesVoTestDataBuilder.builder()
            .email(Set.of("test@example.com"))
            .build()
            .emailBlockRulesVo());

    AssertionsForClassTypes.assertThatThrownBy(() -> updateUserUseCase.execute(updateUserCommand))
        .isInstanceOf(InvalidValueException.class);

    Mockito.verifyNoInteractions(userRepositoryWritePort);
    Mockito.verifyNoInteractions(userSenderPort);
  }

  @Test
  void execute_shouldThrowResourceNotFoundException_whenUserNotFound() {
    final UpdateUserCommand updateUserCommand = UpdateUserCommandTestDataBuilder
        .builder()
        .build()
        .updateUserCommand();

    Mockito.when(userRepositoryReadPort.findUserById(updateUserCommand.getUserId()))
        .thenReturn(Optional.empty());

    AssertionsForClassTypes.assertThatThrownBy(() -> updateUserUseCase.execute(updateUserCommand))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + updateUserCommand.getUserId());

    Mockito.verify(userRepositoryReadPort).findUserById(updateUserCommand.getUserId());
    Mockito.verifyNoInteractions(userRepositoryWritePort);
    Mockito.verifyNoInteractions(userSenderPort);
  }

}