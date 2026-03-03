package com.hexagonal.outbound.database.adapter;

import com.architecture.hexagonal.domain.data.UserDo;
import com.architecture.hexagonal.outbound.database.adapter.UserRepositoryDatabaseWriteAdapter;
import com.architecture.hexagonal.outbound.database.data.UserDao;
import com.architecture.hexagonal.outbound.database.mapper.UserDaoMapper;
import com.architecture.hexagonal.outbound.database.mapper.UserDoMapper;
import com.architecture.hexagonal.outbound.database.repository.UserDatabaseWriteRepository;
import com.hexagonal.outbound.database.config.DatabaseIT;
import com.hexagonal.outbound.database.config.TestApplication;
import com.hexagonal.outbound.database.stub.UserDaoStub;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@SpringBootTest(classes = {UserRepositoryDatabaseWriteAdapter.class})
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
  void createUserSuccess() {
    final UserDo userDo = UserDo.builder()
        .userId(UserDaoStub.userDao().getUserId())
        .email(UserDaoStub.userDao().getEmail())
        .name(UserDaoStub.userDao().getName())
        .build();

    final UserDo expectedResult = UserDo.builder()
        .userId(UserDaoStub.userDao().getUserId())
        .email(UserDaoStub.userDao().getEmail())
        .name(UserDaoStub.userDao().getName())
        .build();

    UserDo result = userRepositoryDatabaseWriteAdapter.createUser(userDo);

    Assertions.assertEquals(expectedResult, result);

    Mockito.verify(userDaoMapper).toUserDao(ArgumentMatchers.any(UserDo.class));
    Mockito.verify(userDatabaseWriteRepository).save(ArgumentMatchers.any(UserDao.class));
    Mockito.verify(userDoMapper).toUserDo(ArgumentMatchers.any(UserDao.class));
  }

}
