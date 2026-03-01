package com.hexagonal.outbound.database.adapter;

import com.architecture.hexagonal.domain.data.UserDo;
import com.architecture.hexagonal.outbound.database.adapter.UserRepositoryDatabaseReadAdapter;
import com.architecture.hexagonal.outbound.database.data.UserDao;
import com.architecture.hexagonal.outbound.database.mapper.UserDoMapper;
import com.architecture.hexagonal.outbound.database.repository.UserDatabaseReadRepository;
import com.hexagonal.outbound.database.config.DatabaseIT;
import com.hexagonal.outbound.database.config.TestApplication;
import com.hexagonal.outbound.database.stub.UserDaoStub;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@SpringBootTest(classes = {UserRepositoryDatabaseReadAdapter.class})
@ContextConfiguration(classes = TestApplication.class)
class UserRepositoryDatabaseReadAdapterTestIT extends DatabaseIT {

  @Autowired
  UserRepositoryDatabaseReadAdapter userRepositoryDatabaseReadAdapter;

  @MockitoSpyBean
  UserDatabaseReadRepository userDatabaseReadRepository;

  @MockitoSpyBean
  UserDoMapper userDoMapper;

  @BeforeEach
  void setup() {
    userDatabaseReadRepository.deleteAll();
    userDatabaseReadRepository.save(UserDaoStub.userDao());
  }

  @Test
  void getAllUsers() {
    final Set<UserDo> expectedUsers = new HashSet<>();
    expectedUsers.add(UserDo.builder()
        .userId(UserDaoStub.userDao().getUserId())
        .email(UserDaoStub.userDao().getEmail())
        .name(UserDaoStub.userDao().getName())
        .build());

    Set<UserDo> result = userRepositoryDatabaseReadAdapter.getAllUsers();

    Assertions.assertEquals(expectedUsers, result);

    Mockito.verify(userDatabaseReadRepository).findAll();
    Mockito.verify(userDoMapper).toUserDoSet(ArgumentMatchers.anyList());
  }

  @Test
  void findUserById() {
    final UserDo expectedUser = UserDo.builder()
        .userId(UserDaoStub.userDao().getUserId())
        .email(UserDaoStub.userDao().getEmail())
        .name(UserDaoStub.userDao().getName())
        .build();

    UserDo result = userRepositoryDatabaseReadAdapter.findUserById(UserDaoStub.userDao().getUserId());

    Assertions.assertEquals(expectedUser, result);

    Mockito.verify(userDatabaseReadRepository).findByUserId(ArgumentMatchers.any(UUID.class));
    Mockito.verify(userDoMapper).toUserDo(ArgumentMatchers.any(UserDao.class));
  }

}
