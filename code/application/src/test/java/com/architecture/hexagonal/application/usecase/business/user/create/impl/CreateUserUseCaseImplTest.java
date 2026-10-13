package com.architecture.hexagonal.application.usecase.business.user.create.impl;

import com.architecture.hexagonal.application.port.out.transaction.TransactionWriteBoundary;
import com.architecture.hexagonal.application.service.business.email.EmailService;
import com.architecture.hexagonal.application.service.business.user.UserService;
import com.architecture.hexagonal.application.testutils.model.aggregate.user.UserTestDataBuilder;
import com.architecture.hexagonal.application.testutils.model.vo.email.EmailVoTestDataBuilder;
import com.architecture.hexagonal.application.testutils.orchestration.data.command.CreateUserInputTestDataBuilder;
import com.architecture.hexagonal.application.testutils.transaction.SpyTransactionWriteBoundary;
import com.architecture.hexagonal.contract.application.generated.user.CreateUserCommand;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.InvalidValueException;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.email.EmailVo;
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
class CreateUserUseCaseImplTest {

  @Mock private UserService userService;

  @Mock private EmailService emailService;

  @Spy private TransactionWriteBoundary transactionBoundary = new SpyTransactionWriteBoundary();

  @InjectMocks private CreateUserUseCaseImpl useCase;

  @Test
  void handleShouldReturnCreatedUserWhenCommandIsExecuted() {
    final CreateUserCommand command =
        CreateUserInputTestDataBuilder.builder().build().createUserInput();
    final User input = UserTestDataBuilder.builder().userId(null).build().user();
    final User user = UserTestDataBuilder.builder().build().user();
    Mockito.when(userService.createUser(input)).thenReturn(user);

    final User result = useCase.handle(command);

    AssertionsForClassTypes.assertThat(result).isSameAs(user);
    Mockito.verify(emailService).isEmailBlocked(input.email());
    Mockito.verify(userService).createUser(input);
    Mockito.verify(transactionBoundary).execute(Mockito.<Supplier<User>>any());
  }

  @Test
  void handleShouldRejectBlockedEmailBeforeCreatingUser() {
    final String email = "blocked@example.com";
    final CreateUserCommand command =
        CreateUserInputTestDataBuilder.builder().email(email).build().createUserInput();
    final EmailVo emailVo = EmailVoTestDataBuilder.builder().username("blocked").build().emailVo();
    Mockito.when(emailService.isEmailBlocked(emailVo)).thenReturn(true);

    AssertionsForClassTypes.assertThatThrownBy(() -> useCase.handle(command))
        .isInstanceOf(InvalidValueException.class)
        .hasMessage(ExceptionMessage.EMAIL_NO_ALLOWED_MESSAGE + email);

    Mockito.verifyNoInteractions(userService);
  }
}
