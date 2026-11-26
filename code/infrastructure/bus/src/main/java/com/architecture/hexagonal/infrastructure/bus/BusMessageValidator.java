package com.architecture.hexagonal.infrastructure.bus;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BusMessageValidator {

  private final Validator validator;

  public void validate(final @NonNull Object message) {
    final Set<ConstraintViolation<Object>> violations = validator.validate(message);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }
}
