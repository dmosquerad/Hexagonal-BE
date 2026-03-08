package com.architecture.hexagonal.outbound.database.adapter;

import com.architecture.hexagonal.domain.data.UserDo;
import com.architecture.hexagonal.outbound.database.adapter.UserRepositoryDatabaseReadAdapter;
import com.architecture.hexagonal.outbound.database.data.UserDao;
import com.architecture.hexagonal.outbound.database.mapper.UserDoMapper;
import com.architecture.hexagonal.outbound.database.repository.UserDatabaseReadRepository;
import com.hexagonal.outbound.database.testutils.data.dao.UserDaoTestDataBuilder;
import com.hexagonal.outbound.database.testutils.data.entity.UserDoTestDataBuilder;
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
  UserDoMapper userDoMapper = Mappers.getMapper(UserDoMapper.class);

  @Test
  void getAllUsers() {
    Mockito.when(userDatabaseReadRepository.findAll())
        .thenReturn(Collections.singletonList(UserDaoTestDataBuilder
            .builder()
            .build()
            .userDao()));

    Set<UserDo> result = userRepositoryDatabaseReadAdapter.getAllUsers();

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(Collections.singleton(UserDoTestDataBuilder
            .builder()
            .build()
            .userDo()));

    Mockito.verify(userDatabaseReadRepository).findAll();
    Mockito.verify(userDoMapper).toUserDoSet(ArgumentMatchers.anyList());
  }

  @Test
  void findUserById() {
    final UserDao userDao = UserDaoTestDataBuilder
        .builder()
        .build()
        .userDao();

    Mockito.when(userDatabaseReadRepository.findByUserId(ArgumentMatchers.any(UUID.class)))
        .thenReturn(Optional.of(userDao));

    UserDo result = userRepositoryDatabaseReadAdapter.findUserById(userDao.getUserId());

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(UserDoTestDataBuilder
            .builder()
            .build()
            .userDo());

    Mockito.verify(userDatabaseReadRepository).findByUserId(ArgumentMatchers.any(UUID.class));
    Mockito.verify(userDoMapper).toUserDo(ArgumentMatchers.any(UserDao.class));
  }

}
