package com.architecture.hexagonal.application.port.in;

import com.architecture.hexagonal.application.cqrs.command.request.DeleteUserCommand;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface DeleteUserUseCasePort {
  User execute(@Valid DeleteUserCommand deleteUserCommand) throws ResourceNotFoundException;
}
