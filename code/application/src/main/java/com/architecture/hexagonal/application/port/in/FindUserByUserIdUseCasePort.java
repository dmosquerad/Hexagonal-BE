package com.architecture.hexagonal.application.port.in;

import com.architecture.hexagonal.application.input.query.FindUserByUserIdQuery;
import com.architecture.hexagonal.domain.data.entity.User;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface FindUserByUserIdUseCasePort {
  User execute(@Valid FindUserByUserIdQuery findUserByUserIdQuery) throws ResourceNotFoundException;
}
