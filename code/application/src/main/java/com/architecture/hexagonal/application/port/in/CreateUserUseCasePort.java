package com.architecture.hexagonal.application.port.in;

import com.architecture.hexagonal.application.input.command.CreateUserCommand;
import com.architecture.hexagonal.domain.data.entity.User;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface CreateUserUseCasePort {
  User execute(@Valid CreateUserCommand createUserCommand);
}
