package com.architecture.hexagonal.application.usecase.user.patch.impl;

import com.architecture.hexagonal.application.port.out.transaction.TransactionWriteBoundary;
import com.architecture.hexagonal.application.service.email.EmailService;
import com.architecture.hexagonal.application.service.user.UserService;
import com.architecture.hexagonal.application.testutils.transaction.SpyTransactionWriteBoundary;
import com.architecture.hexagonal.application.testutils.usecase.command.PatchUserInputTestDataBuilder;
import com.architecture.hexagonal.contract.bus.model.user.PatchUserCommand;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.InvalidValueException;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.email.EmailVo;
import com.architecture.hexagonal.domain.testutils.data.model.entity.user.UserTestDataBuilder;
import com.architecture.hexagonal.domain.testutils.data.model.vo.email.EmailVoTestDataBuilder;
import java.util.function.Supplier;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PatchUserUseCaseImplTest {

  @Mock private UserService userService;

  @Mock private EmailService emailService;

  @Spy private TransactionWriteBoundary transactionBoundary = new SpyTransactionWriteBoundary();

  @InjectMocks private PatchUserUseCaseImpl useCase;

  @Test
  void handleShouldReturnPatchedUserWhenCommandIsExecuted() {
    final PatchUserCommand command =
        PatchUserInputTestDataBuilder.builder().build().patchUserInput();
    final User input =
        UserTestDataBuilder.builder()
            .name(command.getName())
            .email(EmailVoTestDataBuilder.builder().username("patched").build().emailVo())
            .build()
            .user();
    final User user = UserTestDataBuilder.builder().build().user();
    Mockito.when(userService.patchUser(input)).thenReturn(user);

    final User result = useCase.handle(command);

    AssertionsForClassTypes.assertThat(result).isSameAs(user);
    Mockito.verify(emailService).isEmailBlocked(input.email());
    Mockito.verify(userService).patchUser(input);
    Mockito.verify(transactionBoundary).execute(Mockito.<Supplier<User>>any());
  }

  @Test
  void handleShouldRejectBlockedEmailBeforePatchingUser() {
    final String email = "blocked@example.com";
    final PatchUserCommand command =
        PatchUserInputTestDataBuilder.builder().email(email).build().patchUserInput();
    final EmailVo emailVo = EmailVoTestDataBuilder.builder().username("blocked").build().emailVo();
    Mockito.when(emailService.isEmailBlocked(emailVo)).thenReturn(true);

    AssertionsForClassTypes.assertThatThrownBy(() -> useCase.handle(command))
        .isInstanceOf(InvalidValueException.class)
        .hasMessage(ExceptionMessage.EMAIL_NO_ALLOWED_MESSAGE + email);

    Mockito.verifyNoInteractions(userService);
  }
}
