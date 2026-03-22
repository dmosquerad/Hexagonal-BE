package com.architecture.hexagonal.infrastructure.outbound.database.adapter;

import com.architecture.hexagonal.domain.data.entity.User;
import com.architecture.hexagonal.infrastructure.outbound.database.data.UserDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mapper.UserMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.repository.UserDatabaseReadRepository;
import com.architecture.hexagonal.infrastructure.outbound.database.testutils.data.dao.UserDaoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.testutils.data.entity.UserTestDataBuilder;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
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
class UserRepositoryDatabaseReadAdapterTest
{

  @InjectMocks
  UserRepositoryDatabaseReadAdapter userRepositoryDatabaseReadAdapter;

  @Mock
  UserDatabaseReadRepository userDatabaseReadRepository;

  @Spy
  UserMapper userMapper = Mappers.getMapper(UserMapper.class);

  @Test
  void getAllUsers() {
    final UserDao userDao = UserDaoTestDataBuilder
      .builder()
      .build()
      .userDao();

    Mockito.when(userDatabaseReadRepository.findAll())
      .thenReturn(Collections.singletonList(userDao));

    Set<User> result = userRepositoryDatabaseReadAdapter.getAllUsers();

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(Collections.singleton(UserTestDataBuilder
            .builder()
            .build()
            .user()));

    Mockito.verify(userDatabaseReadRepository).findAll();
    Mockito.verify(userMapper).toUser(userDao);
  }

  @Test
  void findUserById() {
    final UserDao userDao = UserDaoTestDataBuilder
        .builder()
        .build()
        .userDao();

    Mockito.when(userDatabaseReadRepository.findByUserId(userDao.getUserId()))
        .thenReturn(Optional.of(userDao));

    Optional<User> result = userRepositoryDatabaseReadAdapter.findUserById(userDao.getUserId());

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(Optional.of(UserTestDataBuilder.builder().build().user()));

    Mockito.verify(userDatabaseReadRepository).findByUserId(userDao.getUserId());
    Mockito.verify(userMapper).toUser(userDao);
  }

}
