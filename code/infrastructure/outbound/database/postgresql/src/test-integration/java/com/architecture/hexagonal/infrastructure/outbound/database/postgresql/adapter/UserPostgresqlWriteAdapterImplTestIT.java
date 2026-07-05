package com.architecture.hexagonal.infrastructure.outbound.database.postgresql.adapter;

import com.architecture.hexagonal.domain.model.aggregate.user.User;
import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.config.PostgresqlIT;
import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.config.PostgresqlTestApplication;
import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.data.UserDao;
import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.mapper.user.UserDaoMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.mapper.user.UserFromPostgresqlMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.repository.UserPostgresqlWriteRepository;
import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.testutils.data.aggregate.UserTestDataBuilder;
import java.util.Optional;
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

@SpringBootTest(classes = {UserPostgresqlWriteAdapterImpl.class})
@Transactional
@Sql(scripts = "/user/user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@ContextConfiguration(classes = PostgresqlTestApplication.class)
class UserPostgresqlWriteAdapterImplTestIT extends PostgresqlIT {

  @Autowired
  UserPostgresqlWriteAdapterImpl userPostgresqlWriteAdapterImpl;

  @MockitoSpyBean
  UserPostgresqlWriteRepository userPostgresqlWriteRepository;

  @MockitoSpyBean
  UserFromPostgresqlMapper userFromPostgresqlMapper;

  @MockitoSpyBean
  UserDaoMapper userDaoMapper;

  @Test
  void saveUser_shouldPersistUser_whenUserIsValid() {
    final User user = UserTestDataBuilder.builder()
        .build()
        .user();

    User result = userPostgresqlWriteAdapterImpl.saveUser(user);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(user);

    Mockito.verify(userDaoMapper).toUserDao(user);
    Mockito.verify(userPostgresqlWriteRepository).save(ArgumentMatchers.any(UserDao.class));
    Mockito.verify(userFromPostgresqlMapper).toUser(ArgumentMatchers.any(UserDao.class));
  }

  @Test
  void deleteUser_shouldReturnUser_whenUserExists() {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();

    final Optional<User> result = userPostgresqlWriteAdapterImpl.deleteUser(user.getUser().getUserId());

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(Optional.of(user));

    Mockito.verify(userPostgresqlWriteRepository).deleteByUserId(user.getUser().getUserId());
    Mockito.verify(userFromPostgresqlMapper).toUser(ArgumentMatchers.any(UserDao.class));
  }

}
