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
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(classes = CreateUserUseCase.class)
class CreateUserUseCaseTestIT {

  @Autowired
  CreateUserUseCase createUserUseCase;

  @MockitoBean
  UserRepositoryWritePort userRepositoryWritePort;

  @MockitoBean
  UserSenderPort userSenderPort;

  @MockitoBean
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
        .thenReturn(EmailBlockRulesVoTestDataBuilder
            .builder()
            .build()
            .emailBlockRulesVo());
    Mockito.when(userRepositoryWritePort.saveUser(ArgumentMatchers.any(User.class)))
        .thenReturn(user);

    User result = createUserUseCase.execute(createUserCommand);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(user);

    Mockito.verify(emailConfigurationPort).getBlockedRules();
    Mockito.verify(userRepositoryWritePort).saveUser(ArgumentMatchers.any(User.class));
    Mockito.verify(userSenderPort).userSenderCreated(ArgumentMatchers.any(User.class));
  }

}
