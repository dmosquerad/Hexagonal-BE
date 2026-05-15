package com.architecture.hexagonal.application.port.in;

import com.architecture.hexagonal.application.cqrs.command.request.UpdateUserCommand;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.domain.exception.InvalidValueException;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface UpdateUserUseCasePort {
  User execute(@Valid UpdateUserCommand updateUserCommand) throws ResourceNotFoundException, InvalidValueException;
}