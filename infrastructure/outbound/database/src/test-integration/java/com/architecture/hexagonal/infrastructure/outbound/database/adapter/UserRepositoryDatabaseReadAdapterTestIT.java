package com.architecture.hexagonal.infrastructure.outbound.database.adapter;

import com.architecture.hexagonal.domain.data.User;
import com.architecture.hexagonal.infrastructure.outbound.database.data.UserDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mapper.UserMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.repository.UserDatabaseReadRepository;
import com.architecture.hexagonal.infrastructure.outbound.database.config.DatabaseIT;
import com.architecture.hexagonal.infrastructure.outbound.database.config.TestApplication;
import com.architecture.hexagonal.infrastructure.outbound.database.testutils.data.dao.UserDaoTestDataBuilder;
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

@SpringBootTest(classes = {UserRepositoryDatabaseReadAdapter.class})
@Transactional
@Sql(scripts = "user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@ContextConfiguration(classes = TestApplication.class)
class UserRepositoryDatabaseReadAdapterTestIT extends DatabaseIT {

  @Autowired
  UserRepositoryDatabaseReadAdapter userRepositoryDatabaseReadAdapter;

  @MockitoSpyBean
  UserDatabaseReadRepository userDatabaseReadRepository;

  @MockitoSpyBean
  UserMapper userMapper;

  @Test
  void getAllUsers() {
    Set<User> result = userRepositoryDatabaseReadAdapter.getAllUsers();

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(Collections.singleton(UserDaoTestDataBuilder.builder().build().userDao()));

    Mockito.verify(userDatabaseReadRepository).findAll();
    Mockito.verify(userMapper).toUserSet(ArgumentMatchers.anyList());
  }

  @Test
  void findUserById() {
    final UserDao userDao = UserDaoTestDataBuilder
        .builder()
        .build()
        .userDao();

    Optional<User> result = userRepositoryDatabaseReadAdapter.findUserById(userDao.getUserId());

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(Optional.of(userDao));

    Mockito.verify(userDatabaseReadRepository).findByUserId(ArgumentMatchers.any(UUID.class));
    Mockito.verify(userMapper).toUser(ArgumentMatchers.any(UserDao.class));
  }

}
