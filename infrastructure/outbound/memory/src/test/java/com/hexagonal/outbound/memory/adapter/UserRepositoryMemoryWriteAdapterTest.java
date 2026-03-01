package com.hexagonal.outbound.memory.adapter;

import com.architecture.hexagonal.domain.data.UserDo;
import com.architecture.hexagonal.outbound.memory.adapter.UserRepositoryMemoryWriteAdapter;
import com.architecture.hexagonal.outbound.memory.data.UserDao;
import com.architecture.hexagonal.outbound.memory.mapper.UserDaoMapper;
import com.architecture.hexagonal.outbound.memory.mapper.UserDoMapper;
import com.architecture.hexagonal.outbound.memory.repository.UserMemoryWriteRepository;
import com.hexagonal.outbound.memory.stub.UserDaoStub;
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
class UserRepositoryMemoryWriteAdapterTest {

  @InjectMocks
  UserRepositoryMemoryWriteAdapter userRepositoryMemoryWriteAdapter;

  @Mock
  UserMemoryWriteRepository userMemoryWriteRepository;

  @Spy
  UserDoMapper userDoMapper = Mappers.getMapper(UserDoMapper.class);

  @Spy
  UserDaoMapper userDaoMapper = Mappers.getMapper(UserDaoMapper.class);

  @Test
  void testCreateUserSuccess() {
    final UserDo userDo = UserDo.builder()
        .email(UserDaoStub.userDao().getEmail())
        .name(UserDaoStub.userDao().getName())
        .build();

    final UserDo expectedResult = UserDo.builder()
        .userId(UserDaoStub.userDao().getUserId())
        .email(UserDaoStub.userDao().getEmail())
        .name(UserDaoStub.userDao().getName())
        .build();

    Mockito.when(userMemoryWriteRepository.createUser(ArgumentMatchers.any(UserDao.class)))
        .thenReturn(UserDaoStub.userDao());

    UserDo result = userRepositoryMemoryWriteAdapter.createUser(userDo);

    Assertions.assertEquals(expectedResult, result);

    Mockito.verify(userDaoMapper).toUserDao(ArgumentMatchers.any(UserDo.class));
    Mockito.verify(userMemoryWriteRepository).createUser(ArgumentMatchers.any(UserDao.class));
    Mockito.verify(userDoMapper).toUserDo(ArgumentMatchers.any(UserDao.class));
  }

}
