package com.hexagonal.outbound.database.adapter;

import com.architecture.hexagonal.domain.data.UserDo;
import com.architecture.hexagonal.outbound.database.adapter.UserRepositoryDatabaseReadAdapter;
import com.architecture.hexagonal.outbound.database.data.UserDao;
import com.architecture.hexagonal.outbound.database.mapper.UserDoMapper;
import com.architecture.hexagonal.outbound.database.repository.UserDatabaseReadRepository;
import com.hexagonal.outbound.database.stub.UserDaoStub;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserRepositoryDatabaseReadAdapterTest
{

  @InjectMocks
  UserRepositoryDatabaseReadAdapter userRepositoryDatabaseReadAdapter;

  @Mock
  UserDatabaseReadRepository userDatabaseReadRepository;

  @Spy
  UserDoMapper userDoMapper = Mappers.getMapper(UserDoMapper.class);

  @Test
  void getAllUsers() {
    final Set<UserDo> expectedUsers = new HashSet<>();
    expectedUsers.add(UserDo.builder()
        .userId(UserDaoStub.userDao().getUserId())
        .email(UserDaoStub.userDao().getEmail())
        .name(UserDaoStub.userDao().getName())
        .build());

    Mockito.when(userDatabaseReadRepository.findAll()).thenReturn(Collections.singletonList(UserDaoStub.userDao()));

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

    Mockito.when(userDatabaseReadRepository.findByUserId(ArgumentMatchers.any(UUID.class)))
        .thenReturn(Optional.of(UserDaoStub.userDao()));

    UserDo result = userRepositoryDatabaseReadAdapter.findUserById(UserDaoStub.userDao().getUserId());

    Assertions.assertEquals(expectedUser, result);

    Mockito.verify(userDatabaseReadRepository).findByUserId(ArgumentMatchers.any(UUID.class));
    Mockito.verify(userDoMapper).toUserDo(ArgumentMatchers.any(UserDao.class));
  }

}
