package com.architecture.hexagonal.domain.service;

import com.architecture.hexagonal.domain.model.vo.email.EmailBlockRulesVo;
import com.architecture.hexagonal.domain.model.vo.email.EmailVo;
import com.architecture.hexagonal.domain.model.vo.email.predicate.EmailVoPredicate;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EmailBlockPolicy {

  public static boolean isBlocked(
      @NonNull final EmailVo emailVo, @NonNull final EmailBlockRulesVo rules) {
    if (!EmailVoPredicate.CAN_FORM_EMAIL.test(emailVo)
        || !EmailVoPredicate.CAN_FORM_DOMAIN.test(emailVo)) {
      return false;
    }

    return rules.email().stream().anyMatch(b -> b.equals(emailVo.getEmail().toLowerCase()))
        || rules.host().stream().anyMatch(b -> b.equals(emailVo.host().toLowerCase()))
        || rules.tld().stream().anyMatch(b -> b.equals(emailVo.tld().toLowerCase()))
        || rules.domain().stream().anyMatch(b -> b.equals(emailVo.getDomain().toLowerCase()))
        || rules.username().stream().anyMatch(b -> b.equals(emailVo.username().toLowerCase()));
  }
}
