package com.architecture.hexagonal.domain.model.vo.email.predicate;

import com.architecture.hexagonal.domain.model.vo.email.EmailVo;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import java.util.function.Predicate;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class EmailVoPredicate {

  public static final Predicate<String> IS_VALID_MAIL =
      email -> {
        try {
          new InternetAddress(email).validate();
          return true;
        } catch (AddressException ex) {
          return false;
        }
      };

  public static final Predicate<EmailVo> CAN_FORM_EMAIL =
      email -> StringUtils.isNoneBlank(email.username(), email.host(), email.tld());

  public static final Predicate<EmailVo> CAN_FORM_DOMAIN =
      email -> StringUtils.isNoneBlank(email.host(), email.tld());

  public static Predicate<EmailVo> hostEquals(@NonNull final String host) {
    return email ->
        StringUtils.isNotBlank(host)
            && StringUtils.isNotBlank(email.host())
            && host.equalsIgnoreCase(email.host());
  }
}
