package com.architecture.hexagonal.application.feature.user.patch.port;

import com.architecture.hexagonal.application.feature.user.patch.command.PatchUserCommand;
import com.architecture.hexagonal.domain.exception.InvalidValueException;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface PatchUserUseCasePort {
  User execute(@Valid PatchUserCommand patchUserCommand) throws ResourceNotFoundException, InvalidValueException;
}
