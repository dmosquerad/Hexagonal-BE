package com.architecture.hexagonal.infrastructure.outbound.database.adapter;

import com.architecture.hexagonal.domain.data.User;
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
  UserMapper userMapper = Mappers.getMapper(UserMapper.class);

  @Test
  void getAllUsers() {
    Mockito.when(userDatabaseReadRepository.findAll())
        .thenReturn(Collections.singletonList(UserDaoTestDataBuilder
            .builder()
            .build()
            .userDao()));

    Set<User> result = userRepositoryDatabaseReadAdapter.getAllUsers();

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(Collections.singleton(UserTestDataBuilder
            .builder()
            .build()
            .user()));

    Mockito.verify(userDatabaseReadRepository).findAll();
    Mockito.verify(userMapper).toUserSet(ArgumentMatchers.anyList());
  }

  @Test
  void findUserById() {
    final UserDao userDao = UserDaoTestDataBuilder
        .builder()
        .build()
        .userDao();

    Mockito.when(userDatabaseReadRepository.findByUserId(ArgumentMatchers.any(UUID.class)))
        .thenReturn(Optional.of(userDao));

    User result = userRepositoryDatabaseReadAdapter.findUserById(userDao.getUserId());

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(UserTestDataBuilder
            .builder()
            .build()
            .user());

    Mockito.verify(userDatabaseReadRepository).findByUserId(ArgumentMatchers.any(UUID.class));
    Mockito.verify(userMapper).toUser(ArgumentMatchers.any(UserDao.class));
  }

}
