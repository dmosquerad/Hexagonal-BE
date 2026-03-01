package com.hexagonal.outbound.memory.adapter;

import com.architecture.hexagonal.domain.data.UserDo;
import com.architecture.hexagonal.outbound.memory.adapter.UserRepositoryMemoryReadAdapter;
import com.architecture.hexagonal.outbound.memory.data.UserDao;
import com.architecture.hexagonal.outbound.memory.mapper.UserDoMapper;
import com.architecture.hexagonal.outbound.memory.repository.UserMemoryReadRepository;
import com.hexagonal.outbound.memory.stub.UserDaoStub;
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
class UserRepositoryMemoryReadAdapterTest {

  @InjectMocks
  UserRepositoryMemoryReadAdapter userRepositoryMemoryReadAdapter;

  @Mock
  UserMemoryReadRepository userMemoryReadRepository;

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

    Mockito.when(userMemoryReadRepository.getAllUsers()).thenReturn(Collections.singleton(UserDaoStub.userDao()));

    Set<UserDo> result = userRepositoryMemoryReadAdapter.getAllUsers();

    Assertions.assertEquals(expectedUsers, result);

    Mockito.verify(userMemoryReadRepository).getAllUsers();
    Mockito.verify(userDoMapper).toUserDoSet(ArgumentMatchers.anySet());
  }

  @Test
  void findUserById() {
    final UserDo expectedUser = UserDo.builder()
        .userId(UserDaoStub.userDao().getUserId())
        .email(UserDaoStub.userDao().getEmail())
        .name(UserDaoStub.userDao().getName())
        .build();

    Mockito.when(userMemoryReadRepository.findUserById(ArgumentMatchers.any(UUID.class)))
        .thenReturn(Optional.of(UserDaoStub.userDao()));

    UserDo result = userRepositoryMemoryReadAdapter.findUserById(UserDaoStub.userDao().getUserId());

    Assertions.assertEquals(expectedUser, result);

    Mockito.verify(userMemoryReadRepository).findUserById(ArgumentMatchers.any(UUID.class));
    Mockito.verify(userDoMapper).toUserDo(ArgumentMatchers.any(UserDao.class));
  }

}
