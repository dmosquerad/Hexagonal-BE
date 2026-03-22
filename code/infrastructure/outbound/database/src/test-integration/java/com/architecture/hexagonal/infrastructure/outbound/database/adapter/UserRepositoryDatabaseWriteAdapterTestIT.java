package com.architecture.hexagonal.infrastructure.outbound.database.adapter;

import com.architecture.hexagonal.domain.data.entity.User;
import com.architecture.hexagonal.infrastructure.outbound.database.data.UserDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mapper.UserDaoMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.mapper.UserMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.repository.UserDatabaseWriteRepository;
import com.architecture.hexagonal.infrastructure.outbound.database.config.DatabaseIT;
import com.architecture.hexagonal.infrastructure.outbound.database.config.TestApplication;
import com.architecture.hexagonal.infrastructure.outbound.database.testutils.data.entity.UserTestDataBuilder;
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

@SpringBootTest(classes = {UserRepositoryDatabaseWriteAdapter.class})
@Transactional
@Sql(scripts = "user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@ContextConfiguration(classes = TestApplication.class)
class UserRepositoryDatabaseWriteAdapterTestIT extends DatabaseIT {

  @Autowired
  UserRepositoryDatabaseWriteAdapter userRepositoryDatabaseWriteAdapter;

  @MockitoSpyBean
  UserDatabaseWriteRepository userDatabaseWriteRepository;

  @MockitoSpyBean
  UserMapper userMapper;

  @MockitoSpyBean
  UserDaoMapper userDaoMapper;

  @Test
  void saveUser_create() {
    final User user = UserTestDataBuilder.builder()
        .build()
        .user();

    User result = userRepositoryDatabaseWriteAdapter.saveUser(user);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(user);

    Mockito.verify(userDaoMapper).toUserDao(user);
    Mockito.verify(userDatabaseWriteRepository).save(ArgumentMatchers.any(UserDao.class));
    Mockito.verify(userMapper).toUser(ArgumentMatchers.any(UserDao.class));
  }

  @Test
  void deleteUser() {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();

    final Optional<User> result = userRepositoryDatabaseWriteAdapter.deleteUser(user.getUserId());

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(Optional.of(user));

    Mockito.verify(userDatabaseWriteRepository).deleteByUserId(user.getUserId());
    Mockito.verify(userMapper).toUser(ArgumentMatchers.any(UserDao.class));
  }

}
