package com.architecture.hexagonal.infrastructure.outbound.database.adapter;

import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.infrastructure.outbound.database.data.UserDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mapper.user.UserDaoMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.mapper.user.UserMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.repository.UserDatabaseWriteRepository;
import com.architecture.hexagonal.infrastructure.outbound.database.testutils.data.aggregate.UserTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.testutils.data.dao.UserDaoTestDataBuilder;
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

  @InjectMocks UserRepositoryDatabaseWriteAdapter userRepositoryDatabaseWriteAdapter;

  @Mock UserDatabaseWriteRepository userDatabaseWriteRepository;

  @Spy UserMapper userMapper = Mappers.getMapper(UserMapper.class);

  @Spy UserDaoMapper userDaoMapper = Mappers.getMapper(UserDaoMapper.class);

  @Test
  void saveUser_shouldPersistUser_whenUserIsValid() {
    final User user = UserTestDataBuilder.builder().build().user();
    final UserDao userDao = UserDaoTestDataBuilder.builder().build().userDao();

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
  void deleteUser_shouldReturnUser_whenUserExists() {
    final User user = UserTestDataBuilder.builder().build().user();
    final UserDao userDao = UserDaoTestDataBuilder.builder().build().userDao();

    Mockito.when(userDatabaseWriteRepository.deleteByUserId(user.getId()))
        .thenReturn(Optional.of(userDao));

    final Optional<User> result = userRepositoryDatabaseWriteAdapter.deleteUser(user.getId());

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(Optional.of(user));

    Mockito.verify(userDatabaseWriteRepository).deleteByUserId(user.getId());
    Mockito.verify(userMapper).toUser(userDao);
  }

  @Test
  void deleteUser_shouldReturnEmpty_whenUserNotFound() {
    final UserDao userDao = UserDaoTestDataBuilder.builder().build().userDao();

    Mockito.when(userDatabaseWriteRepository.deleteByUserId(userDao.getUserId()))
        .thenReturn(Optional.empty());

    final Optional<User> result =
        userRepositoryDatabaseWriteAdapter.deleteUser(userDao.getUserId());

    AssertionsForClassTypes.assertThat(result).isEqualTo(Optional.empty());

    Mockito.verify(userDatabaseWriteRepository).deleteByUserId(userDao.getUserId());
    Mockito.verify(userMapper, Mockito.never()).toUser(userDao);
  }
}
