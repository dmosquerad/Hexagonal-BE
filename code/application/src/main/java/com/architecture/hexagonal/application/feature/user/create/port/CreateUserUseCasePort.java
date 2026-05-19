package com.architecture.hexagonal.application.feature.user.create.port;

import com.architecture.hexagonal.application.feature.user.create.command.CreateUserCommand;
import com.architecture.hexagonal.domain.exception.InvalidValueException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface CreateUserUseCasePort {
  User execute(@Valid CreateUserCommand createUserCommand) throws InvalidValueException;
}
