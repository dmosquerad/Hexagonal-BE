package com.architecture.hexagonal.application.command;

import com.architecture.hexagonal.application.bus.command.CommandBus;
import com.architecture.hexagonal.application.bus.command.impl.CommandBusImpl;
import com.architecture.hexagonal.application.bus.command.impl.CreateUserCommandHandler;
import com.architecture.hexagonal.application.input.command.CreateUserCommand;
import com.architecture.hexagonal.application.port.in.CreateUserUseCasePort;
import com.architecture.hexagonal.application.port.in.GetBlockedRulesUseCasePort;
import com.architecture.hexagonal.domain.model.entity.User;
import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import com.architecture.hexagonal.application.testutils.data.entity.UserTestDataBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CommandBusTest {

  @Test
  void shouldDispatchCreateUserCommandToCreateUserHandler() throws Exception {
    final CreateUserUseCasePort createUserUseCasePort = Mockito.mock(CreateUserUseCasePort.class);
    final GetBlockedRulesUseCasePort getBlockedRulesUseCasePort = Mockito.mock(GetBlockedRulesUseCasePort.class);
    final CreateUserCommandHandler handler = new CreateUserCommandHandler(createUserUseCasePort);

    final CommandBus commandBus = new CommandBusImpl(List.of(handler));

    final CreateUserCommand command = CreateUserCommand.builder()
        .name("User name")
        .email("user@example.com")
        .build();

    final User expectedUser = UserTestDataBuilder.builder().build().user();

    Mockito.when(getBlockedRulesUseCasePort.execute()).thenReturn(EmailBlockRulesVo.builder().build());
    Mockito.when(createUserUseCasePort.execute(command)).thenReturn(expectedUser);

    final User actual = commandBus.execute(command);

    assertThat(actual).isEqualTo(expectedUser);
    Mockito.verify(createUserUseCasePort).execute(command);
  }
}
