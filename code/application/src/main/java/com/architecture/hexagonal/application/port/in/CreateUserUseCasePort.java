package com.architecture.hexagonal.application.port.in;

import com.architecture.hexagonal.application.cqrs.command.request.CreateUserCommand;
import com.architecture.hexagonal.domain.exception.InvalidValueException;
import com.architecture.hexagonal.domain.model.entity.User;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface CreateUserUseCasePort {
  User execute(@Valid CreateUserCommand createUserCommand) throws InvalidValueException;
}
