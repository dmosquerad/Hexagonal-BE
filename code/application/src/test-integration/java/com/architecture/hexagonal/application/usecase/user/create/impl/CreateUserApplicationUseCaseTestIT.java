package com.architecture.hexagonal.application.usecase.user.create.impl;

import com.architecture.hexagonal.application.port.out.email.EmailConfigurationPort;
import com.architecture.hexagonal.application.port.out.user.UserRepositoryReadPort;
import com.architecture.hexagonal.application.port.out.user.UserRepositoryWritePort;
import com.architecture.hexagonal.application.port.out.user.UserSenderPort;
import com.architecture.hexagonal.application.service.email.EmailService;
import com.architecture.hexagonal.application.service.email.impl.EmailServiceImpl;
import com.architecture.hexagonal.application.service.user.UserService;
import com.architecture.hexagonal.application.service.user.impl.UserServiceImpl;
import com.architecture.hexagonal.domain.testutils.data.model.vo.email.EmailBlockRulesVoTestDataBuilder;
import com.architecture.hexagonal.domain.testutils.data.model.entity.user.UserTestDataBuilder;
import com.architecture.hexagonal.application.testutils.usecase.command.CreateUserInputTestDataBuilder;
import com.architecture.hexagonal.application.testutils.transaction.SpyTransactionWriteBoundary;
import com.architecture.hexagonal.contract.bus.model.user.CreateUserCommand;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.email.EmailVo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.assertj.core.api.AssertionsForClassTypes;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateUserApplicationUseCaseTestIT {

  @Mock private UserRepositoryReadPort userRepositoryReadPort;
  @Mock private UserRepositoryWritePort userRepositoryWritePort;
  @Mock private UserSenderPort userSenderPort;
  @Mock private EmailConfigurationPort emailConfigurationPort;
  @Mock private UserService userService;
  @Mock private EmailService emailService;

  @Spy private SpyTransactionWriteBoundary transactionWriteBoundary =
      new SpyTransactionWriteBoundary();

  @InjectMocks private UserServiceImpl userServiceImpl;
  @InjectMocks private EmailServiceImpl emailServiceImpl;
  @InjectMocks private CreateUserUseCaseImpl createUserUseCase;

  @Test
  void createUserUseCaseShouldReturnCreatedUserWhenCommandIsExecuted() {
    final CreateUserCommand command =
        CreateUserInputTestDataBuilder.builder().build().createUserInput();
    final User user = UserTestDataBuilder.builder().build().user();

    Mockito.when(emailConfigurationPort.getBlockedRules())
        .thenReturn(EmailBlockRulesVoTestDataBuilder.builder().build().emailBlockRulesVo());
    Mockito.when(emailService.isEmailBlocked(ArgumentMatchers.any(EmailVo.class)))
        .thenAnswer(
            AdditionalAnswers.answer(
                (EmailVo email) -> emailServiceImpl.isEmailBlocked(email)));
    Mockito.when(userRepositoryWritePort.saveUser(ArgumentMatchers.any(User.class)))
        .thenReturn(user);
    Mockito.when(userService.createUser(ArgumentMatchers.any(User.class)))
        .thenAnswer(
            AdditionalAnswers.answer((User candidate) -> userServiceImpl.createUser(candidate)));
    final User result = createUserUseCase.handle(command);

    AssertionsForClassTypes.assertThat(result).isSameAs(user);
    Mockito.verify(transactionWriteBoundary).execute(ArgumentMatchers.any());
    Mockito.verify(emailService).isEmailBlocked(ArgumentMatchers.any(EmailVo.class));
    Mockito.verify(userService).createUser(ArgumentMatchers.any(User.class));
    Mockito.verify(userRepositoryWritePort).saveUser(ArgumentMatchers.any(User.class));
    Mockito.verify(userSenderPort).userSenderCreated(user);
  }
}
