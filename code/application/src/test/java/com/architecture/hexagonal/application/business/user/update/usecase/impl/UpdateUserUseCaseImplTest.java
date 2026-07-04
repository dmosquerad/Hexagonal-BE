package com.architecture.hexagonal.application.business.user.update.usecase.impl;

import com.architecture.hexagonal.application.business.user.update.input.UpdateUserInput;
import com.architecture.hexagonal.application.port.configuration.EmailConfigurationPort;
import com.architecture.hexagonal.application.port.database.UserRepositoryReadPort;
import com.architecture.hexagonal.application.port.database.UserRepositoryWritePort;
import com.architecture.hexagonal.application.port.message.UserSenderPort;
import com.architecture.hexagonal.application.testutils.data.aggregate.UserTestDataBuilder;
import com.architecture.hexagonal.application.testutils.data.vo.EmailBlockRulesVoTestDataBuilder;
import com.architecture.hexagonal.application.testutils.user.update.input.UpdateUserInputTestDataBuilder;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.InvalidValueException;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.model.aggregate.User;
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
class UpdateUserUseCaseImplTest {

  @InjectMocks UpdateUserUseCaseImpl updateUserUseCaseImpl;

  @Mock UserRepositoryReadPort userRepositoryReadPort;

  @Mock UserRepositoryWritePort userRepositoryWritePort;

  @Mock UserSenderPort userSenderPort;

  @Mock EmailConfigurationPort emailConfigurationPort;

  @Test
  void execute_shouldUpdateUser_whenUserExists()
      throws ResourceNotFoundException, InvalidValueException {
    final User user = UserTestDataBuilder.builder().build().user();
    final UpdateUserInput updateUserInput =
        UpdateUserInputTestDataBuilder.builder().build().updateUserInput();

    Mockito.when(userRepositoryReadPort.findUserById(updateUserInput.getUserId()))
        .thenReturn(Optional.of(user));
    Mockito.when(emailConfigurationPort.getBlockedRules())
        .thenReturn(EmailBlockRulesVoTestDataBuilder.builder().build().emailBlockRulesVo());
    Mockito.when(userRepositoryWritePort.saveUser(user)).thenReturn(user);

    final User result = updateUserUseCaseImpl.execute(updateUserInput);

    AssertionsForClassTypes.assertThat(result).usingRecursiveComparison().isEqualTo(user);

    Mockito.verify(userRepositoryReadPort).findUserById(updateUserInput.getUserId());
    Mockito.verify(emailConfigurationPort).getBlockedRules();
    Mockito.verify(userRepositoryWritePort).saveUser(user);
    Mockito.verify(userSenderPort).userSenderUpdated(user);
  }

  @Test
  void execute_shouldThrowPolicyViolationException_whenEmailIsBlocked() {
    final User user = UserTestDataBuilder.builder().build().user();
    final UpdateUserInput updateUserInput =
        UpdateUserInputTestDataBuilder.builder().build().updateUserInput();

    Mockito.when(userRepositoryReadPort.findUserById(updateUserInput.getUserId()))
        .thenReturn(Optional.of(user));
    Mockito.when(emailConfigurationPort.getBlockedRules())
        .thenReturn(
            EmailBlockRulesVoTestDataBuilder.builder()
                .email(Set.of("test@example.com"))
                .build()
                .emailBlockRulesVo());

    AssertionsForClassTypes.assertThatThrownBy(() -> updateUserUseCaseImpl.execute(updateUserInput))
        .isInstanceOf(InvalidValueException.class);

    Mockito.verifyNoInteractions(userRepositoryWritePort);
    Mockito.verifyNoInteractions(userSenderPort);
  }

  @Test
  void execute_shouldThrowResourceNotFoundException_whenUserNotFound() {
    final UpdateUserInput updateUserInput =
        UpdateUserInputTestDataBuilder.builder().build().updateUserInput();

    Mockito.when(userRepositoryReadPort.findUserById(updateUserInput.getUserId()))
        .thenReturn(Optional.empty());

    AssertionsForClassTypes.assertThatThrownBy(() -> updateUserUseCaseImpl.execute(updateUserInput))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + updateUserInput.getUserId());

    Mockito.verify(userRepositoryReadPort).findUserById(updateUserInput.getUserId());
    Mockito.verifyNoInteractions(userRepositoryWritePort);
    Mockito.verifyNoInteractions(userSenderPort);
  }
}
