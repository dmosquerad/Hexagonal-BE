package com.architecture.hexagonal.infrastructure.outbound.database.adapter;

import com.architecture.hexagonal.domain.data.User;
import com.architecture.hexagonal.infrastructure.outbound.database.data.UserDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mapper.UserDaoMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.mapper.UserMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.repository.UserDatabaseWriteRepository;
import com.architecture.hexagonal.infrastructure.outbound.database.testutils.data.dao.UserDaoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.testutils.data.entity.UserTestDataBuilder;
import java.util.Optional;
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
class UserRepositoryDatabaseWriteAdapterTest {

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

  @Test
  void deleteUser() {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();

    Mockito.when(userDatabaseWriteRepository.deleteByUserId(ArgumentMatchers.any(UUID.class)))
        .thenReturn(Optional.of(UserDaoTestDataBuilder
            .builder()
            .build()
            .userDao()));

    final Optional<User> result = userRepositoryDatabaseWriteAdapter.deleteUser(user.getUserId());

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(Optional.of(user));

    Mockito.verify(userDatabaseWriteRepository).deleteByUserId(ArgumentMatchers.any(UUID.class));
    Mockito.verify(userMapper).toUser(ArgumentMatchers.any(UserDao.class));
  }

}
