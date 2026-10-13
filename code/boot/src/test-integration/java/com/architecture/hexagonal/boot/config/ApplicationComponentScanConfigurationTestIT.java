package com.architecture.hexagonal.boot.config;

import com.architecture.hexagonal.application.port.out.email.EmailConfigurationPort;
import com.architecture.hexagonal.application.port.out.scheduler.SchedulerConfigurationPort;
import com.architecture.hexagonal.application.port.out.outbox.OutboxRepositoryReadPort;
import com.architecture.hexagonal.application.port.out.outbox.OutboxRepositoryWritePort;
import com.architecture.hexagonal.application.port.out.user.UserRepositoryReadPort;
import com.architecture.hexagonal.application.port.out.user.UserRepositoryWritePort;
import com.architecture.hexagonal.application.port.out.user.UserSenderPort;
import com.architecture.hexagonal.application.port.out.outbox.OutboxProcess;
import com.architecture.hexagonal.application.port.out.transaction.TransactionReadBoundary;
import com.architecture.hexagonal.application.port.out.transaction.TransactionWriteBoundary;
import com.architecture.hexagonal.application.service.business.email.impl.EmailServiceImpl;
import com.architecture.hexagonal.application.service.business.user.impl.UserServiceImpl;
import com.architecture.hexagonal.application.service.technical.outbox.impl.OutboxServiceImpl;
import com.architecture.hexagonal.application.usecase.email.getblockedrules.impl.GetBlockedRulesUseCaseImpl;
import com.architecture.hexagonal.application.usecase.user.create.impl.CreateUserUseCaseImpl;
import com.architecture.hexagonal.application.usecase.user.delete.impl.DeleteUserUseCaseImpl;
import com.architecture.hexagonal.application.usecase.user.exists.impl.UserExistsUseCaseImpl;
import com.architecture.hexagonal.application.usecase.user.findbyid.impl.FindUserByUserIdUseCaseImpl;
import com.architecture.hexagonal.application.usecase.user.getall.impl.GetAllUsersUseCaseImpl;
import com.architecture.hexagonal.application.usecase.user.patch.impl.PatchUserUseCaseImpl;
import com.architecture.hexagonal.application.usecase.user.update.impl.UpdateUserUseCaseImpl;
import com.architecture.hexagonal.application.usecase.outbox.retry.impl.RetryOutboxUseCaseImpl;
import java.time.Clock;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(
    classes = {
      ApplicationServiceComponentScanConfiguration.class,
      ApplicationUseCaseComponentScanConfiguration.class,
      TimeConfig.class
    })
class ApplicationComponentScanConfigurationTestIT {

  @Autowired private Clock clock;

  @Autowired private EmailServiceImpl emailService;

  @Autowired private UserServiceImpl userService;

  @Autowired private OutboxServiceImpl outboxService;

  @Autowired private GetBlockedRulesUseCaseImpl getBlockedRulesUseCase;

  @Autowired private CreateUserUseCaseImpl createUserUseCase;

  @Autowired private DeleteUserUseCaseImpl deleteUserUseCase;

  @Autowired private UserExistsUseCaseImpl userExistsUseCase;

  @Autowired private FindUserByUserIdUseCaseImpl findUserByUserIdUseCase;

  @Autowired private GetAllUsersUseCaseImpl getAllUsersUseCase;

  @Autowired private PatchUserUseCaseImpl patchUserUseCase;

  @Autowired private UpdateUserUseCaseImpl updateUserUseCase;

  @Autowired private RetryOutboxUseCaseImpl retryOutboxUseCase;

  @MockitoBean private EmailConfigurationPort emailConfigurationPort;

  @MockitoBean private UserRepositoryReadPort userRepositoryReadPort;

  @MockitoBean private UserRepositoryWritePort userRepositoryWritePort;

  @MockitoBean private UserSenderPort userSenderPort;

  @MockitoBean private OutboxRepositoryReadPort outboxRepositoryReadPort;

  @MockitoBean private OutboxRepositoryWritePort outboxRepositoryWritePort;

  @MockitoBean private OutboxProcess outboxProcess;

  @MockitoBean private SchedulerConfigurationPort schedulerConfigurationPort;

  @MockitoBean private TransactionReadBoundary transactionReadBoundary;

  @MockitoBean private TransactionWriteBoundary transactionWriteBoundary;

  @Test
  void shouldRegisterApplicationServicesUseCasesAndUtcClock() {
    Assertions.assertThat(clock.getZone()).isEqualTo(Clock.systemUTC().getZone());
    Assertions.assertThat(emailService).isNotNull();
    Assertions.assertThat(userService).isNotNull();
    Assertions.assertThat(outboxService).isNotNull();
    Assertions.assertThat(getBlockedRulesUseCase).isNotNull();
    Assertions.assertThat(createUserUseCase).isNotNull();
    Assertions.assertThat(deleteUserUseCase).isNotNull();
    Assertions.assertThat(userExistsUseCase).isNotNull();
    Assertions.assertThat(findUserByUserIdUseCase).isNotNull();
    Assertions.assertThat(getAllUsersUseCase).isNotNull();
    Assertions.assertThat(patchUserUseCase).isNotNull();
    Assertions.assertThat(updateUserUseCase).isNotNull();
    Assertions.assertThat(retryOutboxUseCase).isNotNull();
  }
}
