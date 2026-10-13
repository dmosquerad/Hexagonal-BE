package com.architecture.hexagonal.application.usecase.user.update.impl;

import com.architecture.hexagonal.application.port.out.transaction.TransactionWriteBoundary;
import com.architecture.hexagonal.application.service.business.email.EmailService;
import com.architecture.hexagonal.application.service.business.user.UserService;
import com.architecture.hexagonal.application.testutils.transaction.SpyTransactionWriteBoundary;
import com.architecture.hexagonal.application.testutils.usecase.command.UpdateUserInputTestDataBuilder;
import com.architecture.hexagonal.contract.application.usecase.generated.user.UpdateUserCommand;
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
class UpdateUserUseCaseImplTest {

  @Mock private UserService userService;

  @Mock private EmailService emailService;

  @Spy private TransactionWriteBoundary transactionBoundary = new SpyTransactionWriteBoundary();

  @InjectMocks private UpdateUserUseCaseImpl useCase;

  @Test
  void handleShouldReturnUpdatedUserWhenCommandIsExecuted() {
    final UpdateUserCommand command =
        UpdateUserInputTestDataBuilder.builder().build().updateUserInput();
    final User input =
        UserTestDataBuilder.builder()
            .name(command.getName())
            .email(EmailVoTestDataBuilder.builder().username("updated").build().emailVo())
            .build()
            .user();
    final User user = UserTestDataBuilder.builder().build().user();
    Mockito.when(userService.updateUser(input)).thenReturn(user);

    final User result = useCase.handle(command);

    AssertionsForClassTypes.assertThat(result).isSameAs(user);
    Mockito.verify(emailService).isEmailBlocked(input.email());
    Mockito.verify(userService).updateUser(input);
    Mockito.verify(transactionBoundary).execute(Mockito.<Supplier<User>>any());
  }

  @Test
  void handleShouldRejectBlockedEmailBeforeUpdatingUser() {
    final String email = "blocked@example.com";
    final UpdateUserCommand command =
        UpdateUserInputTestDataBuilder.builder().email(email).build().updateUserInput();
    final EmailVo emailVo = EmailVoTestDataBuilder.builder().username("blocked").build().emailVo();
    Mockito.when(emailService.isEmailBlocked(emailVo)).thenReturn(true);

    AssertionsForClassTypes.assertThatThrownBy(() -> useCase.handle(command))
        .isInstanceOf(InvalidValueException.class)
        .hasMessage(ExceptionMessage.EMAIL_NO_ALLOWED_MESSAGE + email);

    Mockito.verifyNoInteractions(userService);
  }
}
