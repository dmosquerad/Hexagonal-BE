package com.architecture.hexagonal.application.feature.user.findbyid.port;

import com.architecture.hexagonal.application.feature.user.findbyid.query.FindUserByUserIdQuery;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface FindUserByUserIdUseCasePort {
  User execute(@Valid FindUserByUserIdQuery findUserByUserIdQuery) throws ResourceNotFoundException;
}
