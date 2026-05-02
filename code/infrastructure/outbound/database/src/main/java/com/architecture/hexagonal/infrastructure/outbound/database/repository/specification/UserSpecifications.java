package com.architecture.hexagonal.infrastructure.outbound.database.repository.specification;

import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import com.architecture.hexagonal.infrastructure.outbound.database.data.UserDao;
import com.architecture.hexagonal.infrastructure.outbound.database.repository.predicate.UserPredicates;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import java.util.List;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserSpecifications {

  public static Specification<UserDao> hostEquals() {
    return (root, query, cb) -> null;
  }

  public static Specification<UserDao> hostEquals(final String host) {
    if (StringUtils.isBlank(host)) {
      return hostEquals();
    }
    return (root, query, cb) -> {
      final Expression<String> email = cb.lower(root.get(UserDao.Fields.email));
      return UserPredicates.emailBelongsToHost(cb, email, List.of(host)).stream()
          .reduce(cb::or)
          .orElse(null);
    };
  }

  public static Specification<UserDao> blockedEmail() {
    return (root, query, cb) -> null;
  }

  public static Specification<UserDao> blockedEmail(
      final Boolean blockEmail, final EmailBlockRulesVo rules) {
    return (root, query, cb) -> {
      final Expression<String> email = cb.lower(root.get(UserDao.Fields.email));
      final List<Predicate> blockedPredicates =
          Stream.of(
                  UserPredicates.emailMatchesExact(cb, email, rules.getEmail()),
                  UserPredicates.emailEndsWithDomain(cb, email, rules.getDomain()),
                  UserPredicates.emailContainsHost(cb, email, rules.getHost()),
                  UserPredicates.emailEndsWithTld(cb, email, rules.getTld()),
                  UserPredicates.emailStartsWithUsername(cb, email, rules.getUsername()))
              .flatMap(List::stream)
              .toList();

      final Predicate emailMatchesBlockedRule =
          blockedPredicates.stream().reduce(cb::or).orElse(null);

      if (emailMatchesBlockedRule == null) {
        return null;
      }

      return blockEmail ? emailMatchesBlockedRule : cb.not(emailMatchesBlockedRule);
    };
  }
}
