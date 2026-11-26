package com.architecture.hexagonal.infrastructure.bus;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BusMessageValidatorTest {

  @Mock private Validator validator;
  @Mock private ConstraintViolation<Object> constraintViolation;
  @InjectMocks private BusMessageValidator messageValidator;

  @Test
  void validateShouldAcceptMessageWithoutViolations() {
    final Object message = new Object();
    Mockito.when(validator.validate(message)).thenReturn(Set.of());

    AssertionsForClassTypes.assertThatCode(() -> messageValidator.validate(message))
        .doesNotThrowAnyException();
    Mockito.verify(validator).validate(message);
  }

  @Test
  void validateShouldThrowWhenMessageHasConstraintViolations() {
    final Object message = new Object();
    Mockito.when(validator.validate(message))
        .thenReturn(Set.of(constraintViolation));

    AssertionsForClassTypes.assertThatThrownBy(() -> messageValidator.validate(message))
        .isInstanceOf(ConstraintViolationException.class);
    Mockito.verify(validator).validate(message);
  }
}
