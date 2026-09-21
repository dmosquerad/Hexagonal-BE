package com.architecture.hexagonal.infrastructure.outbound.database.postgresql.repository.custom.predicate;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class UserPredicates {

  public static List<Predicate> emailMatchesExact(
      final CriteriaBuilder cb, final Expression<String> email, final Set<String> values) {
    return Objects.requireNonNullElse(values, List.<String>of()).stream()
        .filter(StringUtils::isNotBlank)
        .map(value -> value.trim().toLowerCase(Locale.ROOT))
        .map(value -> cb.like(email, value))
        .toList();
  }

  public static List<Predicate> emailEndsWithDomain(
      final CriteriaBuilder cb, final Expression<String> email, final Set<String> values) {
    return Objects.requireNonNullElse(values, List.<String>of()).stream()
        .filter(StringUtils::isNotBlank)
        .map(value -> value.trim().toLowerCase(Locale.ROOT))
        .map(value -> cb.like(email, "%@" + value))
        .toList();
  }

  public static List<Predicate> emailContainsHost(
      final CriteriaBuilder cb, final Expression<String> email, final Set<String> values) {
    return Objects.requireNonNullElse(values, List.<String>of()).stream()
        .filter(StringUtils::isNotBlank)
        .map(value -> value.trim().toLowerCase(Locale.ROOT))
        .map(value -> cb.like(email, "%@" + value + ".%"))
        .toList();
  }

  public static List<Predicate> emailEndsWithTld(
      final CriteriaBuilder cb, final Expression<String> email, final Set<String> values) {
    return Objects.requireNonNullElse(values, List.<String>of()).stream()
        .filter(StringUtils::isNotBlank)
        .map(value -> value.trim().toLowerCase(Locale.ROOT))
        .map(value -> cb.like(email, "%." + value))
        .toList();
  }

  public static List<Predicate> emailStartsWithUsername(
      final CriteriaBuilder cb, final Expression<String> email, final Set<String> values) {
    return Objects.requireNonNullElse(values, List.<String>of()).stream()
        .filter(StringUtils::isNotBlank)
        .map(value -> value.trim().toLowerCase(Locale.ROOT))
        .map(value -> cb.like(email, value + "@%"))
        .toList();
  }

  public static List<Predicate> emailBelongsToHost(
      final CriteriaBuilder cb, final Expression<String> email, final List<String> values) {
    return Objects.requireNonNullElse(values, List.<String>of()).stream()
        .filter(StringUtils::isNotBlank)
        .map(value -> value.trim().toLowerCase(Locale.ROOT))
        .map(value -> cb.like(email, "%@" + value + ".%"))
        .toList();
  }
}
