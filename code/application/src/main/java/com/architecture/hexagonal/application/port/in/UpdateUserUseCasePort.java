package com.architecture.hexagonal.application.port.in;

import com.architecture.hexagonal.application.input.command.UpdateUserCommand;
import com.architecture.hexagonal.domain.data.entity.User;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface UpdateUserUseCasePort {
  User execute(@Valid UpdateUserCommand updateUserCommand) throws ResourceNotFoundException;
}