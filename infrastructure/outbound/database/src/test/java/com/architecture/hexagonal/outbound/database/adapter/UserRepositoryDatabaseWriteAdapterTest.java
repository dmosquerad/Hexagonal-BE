package com.architecture.hexagonal.outbound.database.adapter;

import com.architecture.hexagonal.domain.data.UserDo;
import com.architecture.hexagonal.outbound.database.adapter.UserRepositoryDatabaseWriteAdapter;
import com.architecture.hexagonal.outbound.database.data.UserDao;
import com.architecture.hexagonal.outbound.database.mapper.UserDaoMapper;
import com.architecture.hexagonal.outbound.database.mapper.UserDoMapper;
import com.architecture.hexagonal.outbound.database.repository.UserDatabaseWriteRepository;
import com.hexagonal.outbound.database.testutils.data.dao.UserDaoTestDataBuilder;
import com.hexagonal.outbound.database.testutils.data.entity.UserDoTestDataBuilder;
import java.util.UUID;
import org.assertj.core.api.AssertionsForClassTypes;
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
  void saveUser() {
    final UserDo userDo = UserDoTestDataBuilder
        .builder()
        .build()
        .userDo();

    Mockito.when(userDatabaseWriteRepository.save(ArgumentMatchers.any(UserDao.class))).thenReturn(
        UserDaoTestDataBuilder
            .builder()
            .build()
            .userDao());

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
