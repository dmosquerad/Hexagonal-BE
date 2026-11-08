package com.architecture.hexagonal.infrastructure.outbound.database.postgresql.repository.predicate;

import com.architecture.hexagonal.domain.data.model.vo.email.EmailVoTestDataBuilder;
import com.architecture.hexagonal.domain.model.vo.email.EmailVo;
import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.repository.custom.predicate.UserPredicates;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserPredicatesTest {

  @Mock private CriteriaBuilder criteriaBuilder;
  @Mock private Expression<String> emailExpression;
  @Mock private Predicate expectedPredicate;
  final EmailVo emailVo =
      EmailVoTestDataBuilder.builder()
          .username("User")
          .host("Example.com")
          .tld("ORG")
          .build()
          .emailVo();

  @Test
  void emailMatchesExactShouldNormalizeValuesAndIgnoreBlanks() {
    final String expectedLike = emailVo.getEmail().toLowerCase(Locale.ROOT);
    Mockito.when(criteriaBuilder.like(emailExpression, expectedLike)).thenReturn(expectedPredicate);

    final List<Predicate> result =
        UserPredicates.emailMatchesExact(
            criteriaBuilder, emailExpression, Set.of(" " + emailVo.getEmail() + " ", "", "  "));

    Assertions.assertThat(result).containsExactly(expectedPredicate);
  }

  @Test
  void emailEndsWithDomainShouldNormalizeValuesAndIgnoreBlanks() {
    final String expectedLike = "%@" + emailVo.getDomain().toLowerCase(Locale.ROOT);
    Mockito.when(criteriaBuilder.like(emailExpression, expectedLike)).thenReturn(expectedPredicate);

    final List<Predicate> result =
        UserPredicates.emailEndsWithDomain(
            criteriaBuilder, emailExpression, Set.of(" " + emailVo.getDomain() + " ", "", "  "));

    Assertions.assertThat(result).containsExactly(expectedPredicate);
  }

  @Test
  void emailContainsHostShouldNormalizeValuesAndIgnoreBlanks() {
    final String expectedLike = "%@" + emailVo.host().toLowerCase(Locale.ROOT) + ".%";
    Mockito.when(criteriaBuilder.like(emailExpression, expectedLike)).thenReturn(expectedPredicate);

    final List<Predicate> result =
        UserPredicates.emailContainsHost(
            criteriaBuilder, emailExpression, Set.of(" " + emailVo.host() + " ", "", "  "));

    Assertions.assertThat(result).containsExactly(expectedPredicate);
  }

  @Test
  void emailEndsWithTldShouldNormalizeValuesAndIgnoreBlanks() {
    final String expectedLike = "%." + emailVo.tld().toLowerCase(Locale.ROOT);
    Mockito.when(criteriaBuilder.like(emailExpression, expectedLike)).thenReturn(expectedPredicate);

    final List<Predicate> result =
        UserPredicates.emailEndsWithTld(
            criteriaBuilder, emailExpression, Set.of(" " + emailVo.tld() + " ", "", "  "));

    Assertions.assertThat(result).containsExactly(expectedPredicate);
  }

  @Test
  void emailStartsWithUsernameShouldNormalizeValuesAndIgnoreBlanks() {
    final String expectedLike = emailVo.username().toLowerCase(Locale.ROOT) + "@%";
    Mockito.when(criteriaBuilder.like(emailExpression, expectedLike)).thenReturn(expectedPredicate);

    final List<Predicate> result =
        UserPredicates.emailStartsWithUsername(
            criteriaBuilder, emailExpression, Set.of(" " + emailVo.username() + " ", "", "  "));

    Assertions.assertThat(result).containsExactly(expectedPredicate);
  }

  @Test
  void emailBelongsToHostShouldNormalizeValuesAndIgnoreBlanks() {
    final String expectedLike = "%@" + emailVo.host().toLowerCase(Locale.ROOT) + ".%";
    Mockito.when(criteriaBuilder.like(emailExpression, expectedLike)).thenReturn(expectedPredicate);

    final List<Predicate> result =
        UserPredicates.emailBelongsToHost(
            criteriaBuilder, emailExpression, List.of(" " + emailVo.host() + " ", "", "  "));

    Assertions.assertThat(result).containsExactly(expectedPredicate);
  }
}
