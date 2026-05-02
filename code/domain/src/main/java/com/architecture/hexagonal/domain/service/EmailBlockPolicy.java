package com.architecture.hexagonal.domain.service;

import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import com.architecture.hexagonal.domain.model.vo.EmailVo;
import com.architecture.hexagonal.domain.model.vo.predicate.EmailVoPredicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailBlockPolicy {

  public static boolean isBlocked(
      @NonNull final EmailVo emailVo, @NonNull final EmailBlockRulesVo rules) {
    if (!EmailVoPredicate.CAN_FORM_EMAIL.test(emailVo)
        || !EmailVoPredicate.CAN_FORM_DOMAIN.test(emailVo)) {
      return false;
    }

    return rules.getEmail().stream().anyMatch(b -> b.equals(emailVo.getEmail().toLowerCase()))
        || rules.getHost().stream().anyMatch(b -> b.equals(emailVo.getHost().toLowerCase()))
        || rules.getTld().stream().anyMatch(b -> b.equals(emailVo.getTld().toLowerCase()))
        || rules.getDomain().stream().anyMatch(b -> b.equals(emailVo.getDomain().toLowerCase()))
        || rules.getUsername().stream()
            .anyMatch(b -> b.equals(emailVo.getUsername().toLowerCase()));
  }
}
