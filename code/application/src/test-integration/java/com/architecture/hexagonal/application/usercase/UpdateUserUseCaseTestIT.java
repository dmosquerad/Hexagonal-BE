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
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import java.util.Optional;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(classes = UpdateUserUseCase.class)
class UpdateUserUseCaseTestIT {

  @Autowired
  UpdateUserUseCase updateUserUseCase;

  @MockitoBean
  UserRepositoryReadPort userRepositoryReadPort;

  @MockitoBean
  UserRepositoryWritePort userRepositoryWritePort;

  @MockitoBean
  UserSenderPort userSenderPort;

  @MockitoBean
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
}