package com.architecture.hexagonal.infrastructure.outbound.database.repository.specification;

import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import com.architecture.hexagonal.infrastructure.outbound.database.data.UserDao;
import com.architecture.hexagonal.infrastructure.outbound.database.repository.predicate.UserPredicates;
import com.architecture.hexagonal.infrastructure.outbound.database.testutils.data.vo.EmailBlockRulesVoTestDataBuilder;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Set;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class UserSpecificationsTest {

  private static MockedStatic<UserPredicates> predicates;

  @Mock Root<UserDao> root;

  @Mock CriteriaQuery<?> criteriaQuery;

  @Mock CriteriaBuilder criteriaBuilder;

  @Mock Path<String> emailExpression;

  @Mock Predicate expectedPredicate;

  @Mock Predicate blockedPredicate;

  @Mock Predicate negatedPredicate;

  @BeforeAll
  static void setUpStaticMocks() {
    predicates = Mockito.mockStatic(UserPredicates.class);
  }

  @AfterAll
  static void closeStaticMocks() {
    predicates.close();
  }

  @Test
  void hostEquals_shouldReturnNull_whenHostIsBlank() {
    final Specification<UserDao> specification = UserSpecifications.hostEquals("");

    AssertionsForClassTypes.assertThat(
            specification.toPredicate(root, criteriaQuery, criteriaBuilder))
        .isNull();
  }

  @Test
  void hostEquals_shouldBuildPredicate_whenHostIsNotBlank() {
    final String host = "example";

    Mockito.when(root.get(UserDao.Fields.email)).thenReturn((Path) emailExpression);
    Mockito.when(criteriaBuilder.lower(emailExpression)).thenReturn(emailExpression);

    predicates
        .when(
            () ->
                UserPredicates.emailBelongsToHost(criteriaBuilder, emailExpression, List.of(host)))
        .thenReturn(List.of(expectedPredicate));

    final Specification<UserDao> specification = UserSpecifications.hostEquals(host);
    final Predicate actual = specification.toPredicate(root, criteriaQuery, criteriaBuilder);

    AssertionsForClassTypes.assertThat(actual).isSameAs(expectedPredicate);
  }

  @Test
  void blockedEmail_shouldReturnNull_whenNoRulesArePresent() {
    final EmailBlockRulesVo rules =
        EmailBlockRulesVoTestDataBuilder.builder().build().emailBlockRulesVo();

    Mockito.when(root.get(UserDao.Fields.email)).thenReturn((Path) emailExpression);
    Mockito.when(criteriaBuilder.lower(emailExpression)).thenReturn(emailExpression);

    predicates
        .when(
            () ->
                UserPredicates.emailMatchesExact(
                    criteriaBuilder, emailExpression, rules.getEmail()))
        .thenReturn(List.of());
    predicates
        .when(
            () ->
                UserPredicates.emailEndsWithDomain(
                    criteriaBuilder, emailExpression, rules.getDomain()))
        .thenReturn(List.of());
    predicates
        .when(
            () ->
                UserPredicates.emailContainsHost(criteriaBuilder, emailExpression, rules.getHost()))
        .thenReturn(List.of());
    predicates
        .when(
            () -> UserPredicates.emailEndsWithTld(criteriaBuilder, emailExpression, rules.getTld()))
        .thenReturn(List.of());
    predicates
        .when(
            () ->
                UserPredicates.emailStartsWithUsername(
                    criteriaBuilder, emailExpression, rules.getUsername()))
        .thenReturn(List.of());

    final Specification<UserDao> specification = UserSpecifications.blockedEmail(true, rules);
    AssertionsForClassTypes.assertThat(
            specification.toPredicate(root, criteriaQuery, criteriaBuilder))
        .isNull();
  }

  @Test
  void blockedEmail_shouldReturnPredicate_whenBlockEmailIsTrue() {
    final EmailBlockRulesVo rules =
        EmailBlockRulesVoTestDataBuilder.builder()
            .email(Set.of("blocked@example.com"))
            .build()
            .emailBlockRulesVo();

    Mockito.when(root.get(UserDao.Fields.email)).thenReturn((Path) emailExpression);
    Mockito.when(criteriaBuilder.lower(emailExpression)).thenReturn(emailExpression);

    predicates
        .when(
            () ->
                UserPredicates.emailMatchesExact(
                    criteriaBuilder, emailExpression, rules.getEmail()))
        .thenReturn(List.of(blockedPredicate));
    predicates
        .when(
            () ->
                UserPredicates.emailEndsWithDomain(
                    criteriaBuilder, emailExpression, rules.getDomain()))
        .thenReturn(List.of());
    predicates
        .when(
            () ->
                UserPredicates.emailContainsHost(criteriaBuilder, emailExpression, rules.getHost()))
        .thenReturn(List.of());
    predicates
        .when(
            () -> UserPredicates.emailEndsWithTld(criteriaBuilder, emailExpression, rules.getTld()))
        .thenReturn(List.of());
    predicates
        .when(
            () ->
                UserPredicates.emailStartsWithUsername(
                    criteriaBuilder, emailExpression, rules.getUsername()))
        .thenReturn(List.of());

    final Specification<UserDao> specification = UserSpecifications.blockedEmail(true, rules);
    final Predicate actual = specification.toPredicate(root, criteriaQuery, criteriaBuilder);

    AssertionsForClassTypes.assertThat(actual).isSameAs(blockedPredicate);
  }

  @Test
  void blockedEmail_shouldReturnNegatedPredicate_whenBlockEmailIsFalse() {
    final EmailBlockRulesVo rules =
        EmailBlockRulesVoTestDataBuilder.builder()
            .email(Set.of("blocked@example.com"))
            .build()
            .emailBlockRulesVo();

    Mockito.when(root.get(UserDao.Fields.email)).thenReturn((Path) emailExpression);
    Mockito.when(criteriaBuilder.lower(emailExpression)).thenReturn(emailExpression);
    Mockito.when(criteriaBuilder.not(blockedPredicate)).thenReturn(negatedPredicate);

    predicates
        .when(
            () ->
                UserPredicates.emailMatchesExact(
                    criteriaBuilder, emailExpression, rules.getEmail()))
        .thenReturn(List.of(blockedPredicate));
    predicates
        .when(
            () ->
                UserPredicates.emailEndsWithDomain(
                    criteriaBuilder, emailExpression, rules.getDomain()))
        .thenReturn(List.of());
    predicates
        .when(
            () ->
                UserPredicates.emailContainsHost(criteriaBuilder, emailExpression, rules.getHost()))
        .thenReturn(List.of());
    predicates
        .when(
            () -> UserPredicates.emailEndsWithTld(criteriaBuilder, emailExpression, rules.getTld()))
        .thenReturn(List.of());
    predicates
        .when(
            () ->
                UserPredicates.emailStartsWithUsername(
                    criteriaBuilder, emailExpression, rules.getUsername()))
        .thenReturn(List.of());

    final Specification<UserDao> specification = UserSpecifications.blockedEmail(false, rules);
    final Predicate actual = specification.toPredicate(root, criteriaQuery, criteriaBuilder);

    AssertionsForClassTypes.assertThat(actual).isSameAs(negatedPredicate);
  }
}
