package com.architecture.hexagonal.infrastructure.outbound.database.adapter;

import com.architecture.hexagonal.domain.data.entity.User;
import com.architecture.hexagonal.infrastructure.outbound.database.config.DatabaseIT;
import com.architecture.hexagonal.infrastructure.outbound.database.config.TestApplication;
import com.architecture.hexagonal.infrastructure.outbound.database.data.UserDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mapper.UserMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.repository.UserDatabaseReadRepository;
import com.architecture.hexagonal.infrastructure.outbound.database.testutils.data.entity.UserTestDataBuilder;
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
    final Set<User> result = userRepositoryDatabaseReadAdapter.getAllUsers();

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(Collections.singleton(UserTestDataBuilder
                .builder()
                .build()
                .user()));

    Mockito.verify(userDatabaseReadRepository).findAll();
    Mockito.verify(userMapper).toUser(ArgumentMatchers.any(UserDao.class));
  }

  @Test
  void findUserById() {
    final User user = UserTestDataBuilder
            .builder()
            .build()
            .user();

    final Optional<User> result = userRepositoryDatabaseReadAdapter.findUserById(user.getUserId());

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(Optional.of(user));

    Mockito.verify(userDatabaseReadRepository).findByUserId(user.getUserId());
    Mockito.verify(userMapper).toUser(ArgumentMatchers.any(UserDao.class));
  }

}
