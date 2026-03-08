package com.architecture.hexagonal.outbound.database.adapter;

import com.architecture.hexagonal.domain.data.User;
import com.architecture.hexagonal.outbound.database.data.UserDao;
import com.architecture.hexagonal.outbound.database.mapper.UserDaoMapper;
import com.architecture.hexagonal.outbound.database.mapper.UserMapper;
import com.architecture.hexagonal.outbound.database.repository.UserDatabaseWriteRepository;
import com.hexagonal.outbound.database.testutils.data.dao.UserDaoTestDataBuilder;
import com.hexagonal.outbound.database.testutils.data.entity.UserTestDataBuilder;
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
  UserMapper userMapper = Mappers.getMapper(UserMapper.class);

  @Spy
  UserDaoMapper userDaoMapper = Mappers.getMapper(UserDaoMapper.class);

  @Test
  void saveUser() {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();

    Mockito.when(userDatabaseWriteRepository.save(ArgumentMatchers.any(UserDao.class))).thenReturn(
        UserDaoTestDataBuilder
            .builder()
            .build()
            .userDao());

    User result = userRepositoryDatabaseWriteAdapter.saveUser(user);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(user);

    Mockito.verify(userDaoMapper).toUserDao(ArgumentMatchers.any(User.class));
    Mockito.verify(userDatabaseWriteRepository).save(ArgumentMatchers.any(UserDao.class));
    Mockito.verify(userMapper).toUser(ArgumentMatchers.any(UserDao.class));
  }

}
