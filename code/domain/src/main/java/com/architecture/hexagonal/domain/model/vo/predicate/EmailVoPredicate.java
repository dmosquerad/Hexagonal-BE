package com.architecture.hexagonal.domain.model.vo.predicate;

import com.architecture.hexagonal.domain.model.vo.EmailVo;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import java.util.function.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailVoPredicate {

  public static final Predicate<String> IS_VALID_MAIL = email -> {
    try {
      new InternetAddress(email).validate();
      return true;
    } catch (AddressException ex) {
      return false;
    }
  };

  public static final Predicate<EmailVo> CAN_FORM_EMAIL = email ->
    StringUtils.isNoneBlank(email.getUsername(),
        email.getHost(),
        email.getTld());

  public static final Predicate<EmailVo> CAN_FORM_DOMAIN = email ->
    StringUtils.isNoneBlank(email.getHost(), email.getTld());

  public static Predicate<EmailVo> hostEquals(final String host) {
    return email -> StringUtils.isNotBlank(host)
        && StringUtils.isNotBlank(email.getHost())
        && host.equalsIgnoreCase(email.getHost());
  }

}
