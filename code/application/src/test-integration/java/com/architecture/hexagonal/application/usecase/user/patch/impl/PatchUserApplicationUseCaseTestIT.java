package com.architecture.hexagonal.application.usecase.user.patch.impl;

import com.architecture.hexagonal.application.port.out.email.EmailConfigurationPort;
import com.architecture.hexagonal.application.port.out.user.UserRepositoryReadPort;
import com.architecture.hexagonal.application.port.out.user.UserRepositoryWritePort;
import com.architecture.hexagonal.application.port.out.user.UserSenderPort;
import com.architecture.hexagonal.application.service.email.EmailService;
import com.architecture.hexagonal.application.service.email.impl.EmailServiceImpl;
import com.architecture.hexagonal.application.service.user.UserService;
import com.architecture.hexagonal.application.service.user.impl.UserServiceImpl;
import com.architecture.hexagonal.domain.testutils.data.model.entity.user.UserTestDataBuilder;
import com.architecture.hexagonal.domain.testutils.data.model.vo.email.EmailBlockRulesVoTestDataBuilder;
import com.architecture.hexagonal.application.testutils.usecase.command.PatchUserInputTestDataBuilder;
import com.architecture.hexagonal.application.testutils.transaction.SpyTransactionWriteBoundary;
import com.architecture.hexagonal.contract.bus.model.user.PatchUserCommand;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.email.EmailVo;
import java.util.Optional;
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
class PatchUserApplicationUseCaseTestIT {

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
  @InjectMocks private PatchUserUseCaseImpl patchUserUseCase;

  @Test
  void patchUserUseCaseShouldReturnPatchedUserWhenCommandIsExecuted() {
    final PatchUserCommand command =
        PatchUserInputTestDataBuilder.builder().build().patchUserInput();
    Mockito.when(emailConfigurationPort.getBlockedRules())
        .thenReturn(EmailBlockRulesVoTestDataBuilder.builder().build().emailBlockRulesVo());
    Mockito.when(
            emailService.isEmailBlocked(
                ArgumentMatchers.any(EmailVo.class)))
        .thenAnswer(
            AdditionalAnswers.answer(
                (EmailVo email) -> emailServiceImpl.isEmailBlocked(email)));
    Mockito.when(userService.patchUser(ArgumentMatchers.any(User.class)))
        .thenAnswer(
            AdditionalAnswers.answer((User user) -> userServiceImpl.patchUser(user)));
    Mockito.when(userRepositoryWritePort.saveUser(ArgumentMatchers.any(User.class)))
        .thenAnswer(AdditionalAnswers.returnsFirstArg());
    final User existingUser =
        UserTestDataBuilder.builder()
            .userId(command.getUserId())
            .build()
            .user();
    Mockito.when(userRepositoryReadPort.findUserById(command.getUserId()))
        .thenReturn(Optional.of(existingUser));

    final User result = patchUserUseCase.handle(command);

    AssertionsForClassTypes.assertThat(result.userId()).isEqualTo(command.getUserId());
    Mockito.verify(transactionWriteBoundary).execute(ArgumentMatchers.any());
    AssertionsForClassTypes.assertThat(result.name()).isEqualTo(command.getName());
    AssertionsForClassTypes.assertThat(result.email().getEmail()).isEqualTo(command.getEmail());
    Mockito.verify(emailService)
        .isEmailBlocked(ArgumentMatchers.any(EmailVo.class));
    Mockito.verify(userService).patchUser(ArgumentMatchers.any(User.class));
    Mockito.verify(userRepositoryReadPort).findUserById(command.getUserId());
    Mockito.verify(userRepositoryWritePort).saveUser(ArgumentMatchers.any(User.class));
    Mockito.verify(userSenderPort).userSenderUpdated(result);
  }
}
