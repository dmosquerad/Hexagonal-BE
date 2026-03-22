package com.architecture.hexagonal.infrastructure.outbound.database.adapter;

import com.architecture.hexagonal.domain.data.entity.User;
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
    final UserDao userDao = UserDaoTestDataBuilder
      .builder()
      .build()
      .userDao();

    Mockito.when(userDatabaseWriteRepository.save(userDao)).thenReturn(userDao);

    User result = userRepositoryDatabaseWriteAdapter.saveUser(user);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(user);

    Mockito.verify(userDaoMapper).toUserDao(user);
    Mockito.verify(userDatabaseWriteRepository).save(userDao);
    Mockito.verify(userMapper).toUser(userDao);
  }

  @Test
  void deleteUser() {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();
    final UserDao userDao = UserDaoTestDataBuilder
      .builder()
      .build()
      .userDao();

    Mockito.when(userDatabaseWriteRepository.deleteByUserId(user.getUserId()))
      .thenReturn(Optional.of(userDao));

    final Optional<User> result = userRepositoryDatabaseWriteAdapter.deleteUser(user.getUserId());

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(Optional.of(user));

    Mockito.verify(userDatabaseWriteRepository).deleteByUserId(user.getUserId());
    Mockito.verify(userMapper).toUser(userDao);
  }

}
