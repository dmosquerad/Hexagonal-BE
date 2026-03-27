package com.architecture.hexagonal.application.port.in;

import com.architecture.hexagonal.application.input.query.UserExistsQuery;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface UserExistsUseCasePort {
  void execute(@Valid UserExistsQuery userExistsQuery) throws ResourceNotFoundException;
}
