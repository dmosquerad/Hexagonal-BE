package com.architecture.hexagonal.domain.model.vo.email.factory;

import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.InvalidValueException;
import com.architecture.hexagonal.domain.model.vo.email.EmailVo;
import com.architecture.hexagonal.domain.model.vo.email.predicate.EmailVoPredicate;
import com.architecture.hexagonal.domain.model.vo.exception.FailureType;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EmailVoFactory {

  public static EmailVo from(@NonNull final String email) {
    if (!EmailVoPredicate.IS_VALID_MAIL.test(email)) {
      throw new InvalidValueException(
          ExceptionMessage.INVALID_EMAIL_FORMAT + email, FailureType.NON_RETRYABLE);
    }

    final String atSign = "@";
    final String username = email.substring(0, email.indexOf(atSign));
    final String domain = email.substring(email.indexOf(atSign) + 1);

    final int firstDot = domain.indexOf('.');
    final String host = domain.substring(0, firstDot);
    final String tld = domain.substring(firstDot + 1);

    return EmailVo.builder().username(username).host(host).tld(tld).build();
  }
}
