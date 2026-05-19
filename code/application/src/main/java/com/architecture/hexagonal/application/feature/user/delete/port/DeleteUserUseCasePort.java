package com.architecture.hexagonal.application.feature.user.delete.port;

import com.architecture.hexagonal.application.feature.user.delete.command.DeleteUserCommand;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface DeleteUserUseCasePort {
  User execute(@Valid DeleteUserCommand deleteUserCommand) throws ResourceNotFoundException;
}
