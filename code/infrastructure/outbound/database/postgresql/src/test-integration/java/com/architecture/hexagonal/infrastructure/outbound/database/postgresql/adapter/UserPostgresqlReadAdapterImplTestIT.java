package com.architecture.hexagonal.infrastructure.outbound.database.postgresql.adapter;

import com.architecture.hexagonal.domain.model.projector.user.UserEmailProjector;
import com.architecture.hexagonal.domain.model.aggregate.user.User;
import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.config.PostgresqlIT;
import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.config.PostgresqlTestApplication;
import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.data.UserDao;
import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.mapper.user.UserFromPostgresqlMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.repository.UserPostgresqlReadRepository;
import com.architecture.hexagonal.domain.model.aggregate.pagination.Pagination;
import com.architecture.hexagonal.domain.model.aggregate.pagination.PaginationResult;
import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.testutils.data.aggregate.user.UserTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.testutils.data.aggregate.pagination.PaginationTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.testutils.data.vo.EmailBlockRulesVoTestDataBuilder;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = {UserPostgresqlReadAdapterImpl.class})
@Transactional
@Sql(scripts = "/user/user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@ContextConfiguration(classes = PostgresqlTestApplication.class)
class UserPostgresqlReadAdapterImplTestIT extends PostgresqlIT {

  @Autowired
  UserPostgresqlReadAdapterImpl userPostgresqlReadAdapterImpl;

  @MockitoSpyBean
  UserPostgresqlReadRepository userPostgresqlReadRepository;

  @MockitoSpyBean
  UserFromPostgresqlMapper userFromPostgresqlMapper;

  @Test
  void findUserById_shouldReturnUser_whenUserExists() {
    final User user = UserTestDataBuilder
            .builder()
            .build()
            .user();

    final Optional<User> result = userPostgresqlReadAdapterImpl.findUserById(user.userId());

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(Optional.of(user));

    Mockito.verify(userPostgresqlReadRepository).findByUserId(user.userId());
    Mockito.verify(userFromPostgresqlMapper).toUser(ArgumentMatchers.any(UserDao.class));
  }

  @Test
  void getAllUsers_shouldReturnOnlyBlockedHostUsers_whenBlockHostFilterIsApplied() {
    final Pagination pagination = PaginationTestDataBuilder.builder().build().pagination();

    final PaginationResult<User> result = userPostgresqlReadAdapterImpl.getAllUsers(
        UserEmailProjector.builder()
            .host("example")
            .blockEmail(true)
            .blockedRules(EmailBlockRulesVoTestDataBuilder.builder()
                .host(Set.of("example"))
                .build().emailBlockRulesVo())
            .build(),
        pagination);

    AssertionsForClassTypes.assertThat(result.data())
        .usingRecursiveComparison()
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(Collections.singleton(UserTestDataBuilder
            .builder()
            .build()
            .user()));
    AssertionsForClassTypes.assertThat(result.totalElements()).isEqualTo(1);
  }

}
