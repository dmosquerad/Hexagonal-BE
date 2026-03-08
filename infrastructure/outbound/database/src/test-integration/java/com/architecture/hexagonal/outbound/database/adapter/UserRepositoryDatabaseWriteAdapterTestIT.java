package com.architecture.hexagonal.outbound.database.adapter;

import com.architecture.hexagonal.domain.data.UserDo;
import com.architecture.hexagonal.outbound.database.data.UserDao;
import com.architecture.hexagonal.outbound.database.mapper.UserDaoMapper;
import com.architecture.hexagonal.outbound.database.mapper.UserDoMapper;
import com.architecture.hexagonal.outbound.database.repository.UserDatabaseWriteRepository;
import com.architecture.hexagonal.outbound.database.config.DatabaseIT;
import com.architecture.hexagonal.outbound.database.config.TestApplication;
import com.hexagonal.outbound.database.testutils.data.entity.UserDoTestDataBuilder;
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
  UserDoMapper userDoMapper;

  @MockitoSpyBean
  UserDaoMapper userDaoMapper;

  @Test
  void saveUser_create() {
    final UserDo userDo = UserDoTestDataBuilder.builder()
        .userId(null)
        .email("pepe@test.com")
        .build()
        .userDo();

    UserDo result = userRepositoryDatabaseWriteAdapter.saveUser(userDo);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(userDo);

    Mockito.verify(userDaoMapper).toUserDao(ArgumentMatchers.any(UserDo.class));
    Mockito.verify(userDatabaseWriteRepository).save(ArgumentMatchers.any(UserDao.class));
    Mockito.verify(userDoMapper).toUserDo(ArgumentMatchers.any(UserDao.class));
  }

  @Test
  void saveUser_update() {
    final UserDo userDo = UserDoTestDataBuilder
        .builder()
        .build()
        .userDo();

    UserDo result = userRepositoryDatabaseWriteAdapter.saveUser(userDo);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(userDo);

    Mockito.verify(userDaoMapper).toUserDao(ArgumentMatchers.any(UserDo.class));
    Mockito.verify(userDatabaseWriteRepository).save(ArgumentMatchers.any(UserDao.class));
    Mockito.verify(userDoMapper).toUserDo(ArgumentMatchers.any(UserDao.class));
  }

}
