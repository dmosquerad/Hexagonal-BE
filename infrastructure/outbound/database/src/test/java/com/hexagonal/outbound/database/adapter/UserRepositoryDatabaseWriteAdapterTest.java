package com.hexagonal.outbound.database.adapter;

import com.architecture.hexagonal.domain.data.UserDo;
import com.architecture.hexagonal.outbound.database.adapter.UserRepositoryDatabaseWriteAdapter;
import com.architecture.hexagonal.outbound.database.data.UserDao;
import com.architecture.hexagonal.outbound.database.mapper.UserDaoMapper;
import com.architecture.hexagonal.outbound.database.mapper.UserDoMapper;
import com.architecture.hexagonal.outbound.database.repository.UserDatabaseWriteRepository;
import com.hexagonal.outbound.database.stub.UserDaoStub;
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
class UserRepositoryDatabaseWriteAdapterTest
{

  @InjectMocks
  UserRepositoryDatabaseWriteAdapter userRepositoryDatabaseWriteAdapter;

  @Mock
  UserDatabaseWriteRepository userDatabaseWriteRepository;

  @Spy
  UserDoMapper userDoMapper = Mappers.getMapper(UserDoMapper.class);

  @Spy
  UserDaoMapper userDaoMapper = Mappers.getMapper(UserDaoMapper.class);

  @Test
  void testCreateUserSuccess() {
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

    Mockito.when(userDatabaseWriteRepository.save(ArgumentMatchers.any(UserDao.class)))
        .thenReturn(UserDaoStub.userDao());

    UserDo result = userRepositoryDatabaseWriteAdapter.createUser(userDo);

    Assertions.assertEquals(expectedResult, result);

    Mockito.verify(userDaoMapper).toUserDao(ArgumentMatchers.any(UserDo.class));
    Mockito.verify(userDatabaseWriteRepository).save(ArgumentMatchers.any(UserDao.class));
    Mockito.verify(userDoMapper).toUserDo(ArgumentMatchers.any(UserDao.class));
  }

}
