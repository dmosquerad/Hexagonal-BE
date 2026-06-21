package com.architecture.hexagonal.application.business.user.findbyid.usecase;

import com.architecture.hexagonal.application.business.user.findbyid.input.FindUserByUserIdQuery;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.model.aggregate.User;

public interface FindUserByUserIdUseCase {
  User execute(FindUserByUserIdQuery findUserByUserIdQuery) throws ResourceNotFoundException;
}
