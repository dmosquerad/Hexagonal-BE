package com.architecture.hexagonal.boot.config;

import com.architecture.hexagonal.application.port.out.email.EmailConfigurationPort;
import com.architecture.hexagonal.application.port.out.inbox.InboxProcess;
import com.architecture.hexagonal.application.port.out.inbox.InboxRepositoryReadPort;
import com.architecture.hexagonal.application.port.out.inbox.InboxRepositoryWritePort;
import com.architecture.hexagonal.application.port.out.scheduler.SchedulerConfigurationPort;
import com.architecture.hexagonal.application.port.out.outbox.OutboxRepositoryReadPort;
import com.architecture.hexagonal.application.port.out.outbox.OutboxRepositoryWritePort;
import com.architecture.hexagonal.application.port.out.user.UserRepositoryReadPort;
import com.architecture.hexagonal.application.port.out.user.UserRepositoryWritePort;
import com.architecture.hexagonal.application.port.out.bus.event.EventBus;
import com.architecture.hexagonal.application.port.out.outbox.OutboxProcess;
import com.architecture.hexagonal.application.port.out.transaction.TransactionReadBoundary;
import com.architecture.hexagonal.application.port.out.transaction.TransactionWriteBoundary;
import com.architecture.hexagonal.application.service.email.impl.EmailServiceImpl;
import com.architecture.hexagonal.application.service.user.impl.UserServiceImpl;
import com.architecture.hexagonal.application.service.inbox.impl.InboxServiceImpl;
import com.architecture.hexagonal.application.service.outbox.impl.OutboxServiceImpl;
import com.architecture.hexagonal.application.usecase.email.query.getblockedrules.impl.GetBlockedRulesUseCaseImpl;
import com.architecture.hexagonal.application.usecase.inbox.command.persist.impl.PersistInboxUseCaseImpl;
import com.architecture.hexagonal.application.usecase.inbox.command.retry.impl.RetryInboxUseCaseImpl;
import com.architecture.hexagonal.application.usecase.user.command.create.impl.CreateUserUseCaseImpl;
import com.architecture.hexagonal.application.usecase.user.command.delete.impl.DeleteUserUseCaseImpl;
import com.architecture.hexagonal.application.usecase.user.query.exists.impl.UserExistsUseCaseImpl;
import com.architecture.hexagonal.application.usecase.user.query.findbyid.impl.FindUserByUserIdUseCaseImpl;
import com.architecture.hexagonal.application.usecase.user.query.getall.impl.GetAllUsersUseCaseImpl;
import com.architecture.hexagonal.application.usecase.user.command.patch.impl.PatchUserUseCaseImpl;
import com.architecture.hexagonal.application.usecase.user.command.update.impl.UpdateUserUseCaseImpl;
import com.architecture.hexagonal.application.usecase.outbox.command.retry.impl.RetryOutboxUseCaseImpl;
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

  @Autowired private InboxServiceImpl inboxService;

  @Autowired private GetBlockedRulesUseCaseImpl getBlockedRulesUseCase;

  @Autowired private CreateUserUseCaseImpl createUserUseCase;

  @Autowired private DeleteUserUseCaseImpl deleteUserUseCase;

  @Autowired private UserExistsUseCaseImpl userExistsUseCase;

  @Autowired private FindUserByUserIdUseCaseImpl findUserByUserIdUseCase;

  @Autowired private GetAllUsersUseCaseImpl getAllUsersUseCase;

  @Autowired private PatchUserUseCaseImpl patchUserUseCase;

  @Autowired private UpdateUserUseCaseImpl updateUserUseCase;

  @Autowired private RetryOutboxUseCaseImpl retryOutboxUseCase;

  @Autowired private PersistInboxUseCaseImpl persistInboxUseCase;

  @Autowired private RetryInboxUseCaseImpl retryInboxUseCase;

  @MockitoBean private EmailConfigurationPort emailConfigurationPort;

  @MockitoBean private UserRepositoryReadPort userRepositoryReadPort;

  @MockitoBean private UserRepositoryWritePort userRepositoryWritePort;

  @MockitoBean private EventBus eventBus;

  @MockitoBean private OutboxRepositoryReadPort outboxRepositoryReadPort;

  @MockitoBean private OutboxRepositoryWritePort outboxRepositoryWritePort;

  @MockitoBean private OutboxProcess outboxProcess;

  @MockitoBean private InboxRepositoryReadPort inboxRepositoryReadPort;

  @MockitoBean private InboxRepositoryWritePort inboxRepositoryWritePort;

  @MockitoBean private InboxProcess inboxProcess;

  @MockitoBean private SchedulerConfigurationPort schedulerConfigurationPort;

  @MockitoBean private TransactionReadBoundary transactionReadBoundary;

  @MockitoBean private TransactionWriteBoundary transactionWriteBoundary;

  @Test
  void shouldRegisterApplicationServicesUseCasesAndUtcClock() {
    Assertions.assertThat(clock.getZone()).isEqualTo(Clock.systemUTC().getZone());
    Assertions.assertThat(emailService).isNotNull();
    Assertions.assertThat(userService).isNotNull();
    Assertions.assertThat(outboxService).isNotNull();
    Assertions.assertThat(inboxService).isNotNull();
    Assertions.assertThat(getBlockedRulesUseCase).isNotNull();
    Assertions.assertThat(createUserUseCase).isNotNull();
    Assertions.assertThat(deleteUserUseCase).isNotNull();
    Assertions.assertThat(userExistsUseCase).isNotNull();
    Assertions.assertThat(findUserByUserIdUseCase).isNotNull();
    Assertions.assertThat(getAllUsersUseCase).isNotNull();
    Assertions.assertThat(patchUserUseCase).isNotNull();
    Assertions.assertThat(updateUserUseCase).isNotNull();
    Assertions.assertThat(retryOutboxUseCase).isNotNull();
    Assertions.assertThat(persistInboxUseCase).isNotNull();
    Assertions.assertThat(retryInboxUseCase).isNotNull();
  }
}
