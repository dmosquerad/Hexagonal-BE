package com.architecture.hexagonal.infrastructure.outbound.database.postgresql.repository.specification;

import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.data.UserDao;
import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.repository.predicate.UserPredicates;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import java.util.List;
import java.util.Objects;
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
    if (Objects.isNull(rules)) {
      return blockedEmail();
    }
    return (root, query, cb) -> {
      final Expression<String> email = cb.lower(root.get(UserDao.Fields.email));
      final List<Predicate> blockedPredicates =
          Stream.of(
                  UserPredicates.emailMatchesExact(cb, email, rules.email()),
                  UserPredicates.emailEndsWithDomain(cb, email, rules.domain()),
                  UserPredicates.emailContainsHost(cb, email, rules.host()),
                  UserPredicates.emailEndsWithTld(cb, email, rules.tld()),
                  UserPredicates.emailStartsWithUsername(cb, email, rules.username()))
              .flatMap(List::stream)
              .toList();

      final Predicate emailMatchesBlockedRule =
          blockedPredicates.stream().reduce(cb::or).orElse(null);

      if (Objects.isNull(emailMatchesBlockedRule)) {
        return null;
      }

      final boolean shouldBlock = Boolean.TRUE.equals(blockEmail);
      return shouldBlock ? emailMatchesBlockedRule : cb.not(emailMatchesBlockedRule);
    };
  }
}
