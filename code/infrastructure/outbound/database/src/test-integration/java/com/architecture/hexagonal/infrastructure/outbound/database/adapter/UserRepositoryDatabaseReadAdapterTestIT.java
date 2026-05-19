package com.architecture.hexagonal.infrastructure.outbound.database.adapter;

import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.infrastructure.outbound.database.config.DatabaseIT;
import com.architecture.hexagonal.infrastructure.outbound.database.config.TestApplication;
import com.architecture.hexagonal.infrastructure.outbound.database.data.UserDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mapper.UserMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.repository.UserDatabaseReadRepository;
import com.architecture.hexagonal.application.common.pagination.Pagination;
import com.architecture.hexagonal.application.common.pagination.PaginationResult;
import com.architecture.hexagonal.infrastructure.outbound.database.testutils.data.aggregate.UserTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.testutils.data.pagination.PaginationTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.testutils.data.vo.EmailBlockRulesVoTestDataBuilder;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = {UserRepositoryDatabaseReadAdapter.class})
@Transactional
@Sql(scripts = "/user/user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@ContextConfiguration(classes = TestApplication.class)
class UserRepositoryDatabaseReadAdapterTestIT extends DatabaseIT {

  @Autowired
  UserRepositoryDatabaseReadAdapter userRepositoryDatabaseReadAdapter;

  @MockitoSpyBean
  UserDatabaseReadRepository userDatabaseReadRepository;

  @MockitoSpyBean
  UserMapper userMapper;

  @Test
  void getAllUsers_shouldReturnAllUsers_whenDatabaseHasUsers() {
    final Pagination pagination = PaginationTestDataBuilder.builder().build().pagination();

    final PaginationResult<User> result = userRepositoryDatabaseReadAdapter.getAllUsers("", pagination);

    AssertionsForClassTypes.assertThat(result.getData())
        .usingRecursiveComparison()
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(Collections.singleton(UserTestDataBuilder
                .builder()
                .build()
                .user()));
    AssertionsForClassTypes.assertThat(result.getTotalElements()).isEqualTo(1);

    Mockito.verify(userDatabaseReadRepository).findAll(
        ArgumentMatchers.any(Specification.class),
        ArgumentMatchers.any(Pageable.class));
    Mockito.verify(userMapper).toUser(ArgumentMatchers.any(UserDao.class));
  }

  @Test
  void findUserById_shouldReturnUser_whenUserExists() {
    final User user = UserTestDataBuilder
            .builder()
            .build()
            .user();

    final Optional<User> result = userRepositoryDatabaseReadAdapter.findUserById(user.getUser().getUserId());

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(Optional.of(user));

    Mockito.verify(userDatabaseReadRepository).findByUserId(user.getUser().getUserId());
    Mockito.verify(userMapper).toUser(ArgumentMatchers.any(UserDao.class));
  }

  @Test
  void getAllUsers_shouldReturnOnlyBlockedHostUsers_whenBlockHostFilterIsApplied() {
    final Pagination pagination = PaginationTestDataBuilder.builder().build().pagination();

    final PaginationResult<User> result = userRepositoryDatabaseReadAdapter.getAllUsers(
        "example",
        true,
        EmailBlockRulesVoTestDataBuilder.builder().host(Set.of("example")).build().emailBlockRulesVo(),
        pagination);

    AssertionsForClassTypes.assertThat(result.getData())
        .usingRecursiveComparison()
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(Collections.singleton(UserTestDataBuilder
            .builder()
            .build()
            .user()));
    AssertionsForClassTypes.assertThat(result.getTotalElements()).isEqualTo(1);
  }

}
