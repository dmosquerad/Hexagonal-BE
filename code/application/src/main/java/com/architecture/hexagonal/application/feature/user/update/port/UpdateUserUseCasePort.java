package com.architecture.hexagonal.application.feature.user.update.port;

import com.architecture.hexagonal.application.feature.user.update.command.UpdateUserCommand;
import com.architecture.hexagonal.domain.exception.InvalidValueException;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface UpdateUserUseCasePort {
  User execute(@Valid UpdateUserCommand updateUserCommand) throws ResourceNotFoundException, InvalidValueException;
}
