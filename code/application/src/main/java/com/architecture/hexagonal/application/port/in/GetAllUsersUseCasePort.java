package com.architecture.hexagonal.application.port.in;

import com.architecture.hexagonal.application.input.query.GetAllUserQuery;
import com.architecture.hexagonal.domain.model.entity.User;
import jakarta.validation.Valid;
import java.util.Set;
import org.springframework.validation.annotation.Validated;

@Validated
public interface GetAllUsersUseCasePort {
  Set<User> execute(@Valid GetAllUserQuery getAllUserQuery);
}
