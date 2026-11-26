package com.architecture.hexagonal.domain.model.vo.email.validation;

import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.InvalidValueException;
import com.architecture.hexagonal.domain.model.vo.exception.FailureType;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EmailVoValidator {

  public static void validate(@NonNull final String email) {
    try {
      new InternetAddress(email).validate();
    } catch (AddressException _) {
      throw new InvalidValueException(
          ExceptionMessage.INVALID_EMAIL_FORMAT + email, FailureType.NON_RETRYABLE);
    }
  }
}
