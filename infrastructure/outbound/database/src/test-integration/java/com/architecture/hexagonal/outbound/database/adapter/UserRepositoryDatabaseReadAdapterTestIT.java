package com.architecture.hexagonal.outbound.database.adapter;

import com.architecture.hexagonal.domain.data.UserDo;
import com.architecture.hexagonal.outbound.database.data.UserDao;
import com.architecture.hexagonal.outbound.database.mapper.UserDoMapper;
import com.architecture.hexagonal.outbound.database.repository.UserDatabaseReadRepository;
import com.architecture.hexagonal.outbound.database.config.DatabaseIT;
import com.architecture.hexagonal.outbound.database.config.TestApplication;
import com.hexagonal.outbound.database.testutils.data.dao.UserDaoTestDataBuilder;
import java.util.Collections;
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
  UserDoMapper userDoMapper;

  @Test
  void getAllUsers() {
    Set<UserDo> result = userRepositoryDatabaseReadAdapter.getAllUsers();

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(Collections.singleton(UserDaoTestDataBuilder.builder().build().userDao()));

    Mockito.verify(userDatabaseReadRepository).findAll();
    Mockito.verify(userDoMapper).toUserDoSet(ArgumentMatchers.anyList());
  }

  @Test
  void findUserById() {
    final UserDao userDao = UserDaoTestDataBuilder
        .builder()
        .build()
        .userDao();

    UserDo result = userRepositoryDatabaseReadAdapter.findUserById(userDao.getUserId());

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(userDao);

    Mockito.verify(userDatabaseReadRepository).findByUserId(ArgumentMatchers.any(UUID.class));
    Mockito.verify(userDoMapper).toUserDo(ArgumentMatchers.any(UserDao.class));
  }

}
