package com.architecture.hexagonal.domain.model.entity.predicate;

import com.architecture.hexagonal.domain.model.entity.User;
import com.architecture.hexagonal.domain.model.vo.EmailVo;
import com.architecture.hexagonal.domain.model.vo.predicate.EmailVoPredicate;
import java.util.function.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserPredicate {

  public static Predicate<User> hasEmail(Predicate<EmailVo> emailPredicate) {
    return user -> emailPredicate.test(user.getEmail());
  }

  public static Predicate<User> hasEmailHost(String host) {
    return hasEmail(EmailVoPredicate.hostEquals(host));
  }
}
