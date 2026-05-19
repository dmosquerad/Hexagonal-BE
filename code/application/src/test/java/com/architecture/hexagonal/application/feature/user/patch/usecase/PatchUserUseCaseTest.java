package com.architecture.hexagonal.application.feature.user.patch.usecase;

import com.architecture.hexagonal.application.feature.user.patch.command.PatchUserCommand;
import com.architecture.hexagonal.application.port.configuration.EmailConfigurationPort;
import com.architecture.hexagonal.application.port.message.UserSenderPort;
import com.architecture.hexagonal.application.port.database.UserRepositoryReadPort;
import com.architecture.hexagonal.application.port.database.UserRepositoryWritePort;
import com.architecture.hexagonal.application.testutils.data.aggregate.UserTestDataBuilder;
import com.architecture.hexagonal.application.testutils.data.vo.EmailBlockRulesVoTestDataBuilder;
import com.architecture.hexagonal.application.testutils.feature.user.patch.command.PatchUserCommandTestDataBuilder;
import com.architecture.hexagonal.domain.exception.InvalidValueException;
import com.architecture.hexagonal.domain.model.aggregate.User;
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
class PatchUserUseCaseTest {

  @InjectMocks
  PatchUserUseCase patchUserUseCase;

  @Mock
  UserRepositoryReadPort userRepositoryReadPort;

  @Mock
  UserRepositoryWritePort userRepositoryWritePort;

  @Mock
  UserSenderPort userSenderPort;

  @Mock
  EmailConfigurationPort emailConfigurationPort;

  @Test
  void execute_shouldPatchUserName_whenRequestIsValid() throws ResourceNotFoundException, InvalidValueException {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();
    final PatchUserCommand patchUserCommand = PatchUserCommandTestDataBuilder
        .builder()
        .build()
        .patchUserCommand();

    Mockito.when(userRepositoryReadPort.findUserById(patchUserCommand.getUserId()))
        .thenReturn(Optional.of(user));
    Mockito.when(emailConfigurationPort.getBlockedRules())
        .thenReturn(EmailBlockRulesVoTestDataBuilder.builder().build().emailBlockRulesVo());
    Mockito.when(userRepositoryWritePort.saveUser(user))
        .thenReturn(user);

    final User result = patchUserUseCase.execute(patchUserCommand);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(user);

    Mockito.verify(userRepositoryReadPort).findUserById(patchUserCommand.getUserId());
    Mockito.verify(emailConfigurationPort).getBlockedRules();
    Mockito.verify(userRepositoryWritePort).saveUser(user);
    Mockito.verify(userSenderPort).userSenderUpdated(user);
  }

  @Test
  void execute_shouldThrowPolicyViolationException_whenEmailIsBlocked() {
    final User user = UserTestDataBuilder.builder().build().user();
    final PatchUserCommand patchUserCommand = PatchUserCommandTestDataBuilder
        .builder()
        .build()
        .patchUserCommand();

    Mockito.when(userRepositoryReadPort.findUserById(patchUserCommand.getUserId()))
        .thenReturn(Optional.of(user));
    Mockito.when(emailConfigurationPort.getBlockedRules())
        .thenReturn(EmailBlockRulesVoTestDataBuilder.builder()
            .email(Set.of("test@example.com"))
            .build()
            .emailBlockRulesVo());

    AssertionsForClassTypes.assertThatThrownBy(() -> patchUserUseCase.execute(patchUserCommand))
        .isInstanceOf(InvalidValueException.class);

    Mockito.verifyNoInteractions(userRepositoryWritePort);
    Mockito.verifyNoInteractions(userSenderPort);
  }

  @Test
  void execute_shouldThrowResourceNotFoundException_whenUserNotFound() {
    final PatchUserCommand patchUserCommand = PatchUserCommandTestDataBuilder
        .builder()
        .build()
        .patchUserCommand();

    Mockito.when(userRepositoryReadPort.findUserById(patchUserCommand.getUserId()))
        .thenReturn(Optional.empty());

    AssertionsForClassTypes.assertThatThrownBy(() ->
        patchUserUseCase.execute(patchUserCommand))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + patchUserCommand.getUserId());

    Mockito.verify(userRepositoryReadPort).findUserById(patchUserCommand.getUserId());
    Mockito.verifyNoInteractions(userRepositoryWritePort);
    Mockito.verifyNoInteractions(userSenderPort);
  }
}