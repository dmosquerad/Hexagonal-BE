package com.architecture.hexagonal.infrastructure.outbound.database.postgresql.adapter;

import com.architecture.hexagonal.domain.model.aggregate.user.User;
import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.data.UserDao;
import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.mapper.user.UserDaoMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.mapper.user.UserFromPostgresqlMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.repository.UserPostgresqlWriteRepository;
import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.testutils.data.aggregate.user.UserTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.testutils.data.dao.UserDaoTestDataBuilder;
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
class UserPostgresqlWriteAdapterImplTest {

  @InjectMocks UserPostgresqlWriteAdapterImpl userPostgresqlWriteAdapterImpl;

  @Mock UserPostgresqlWriteRepository userPostgresqlWriteRepository;

  @Spy
  UserFromPostgresqlMapper userFromPostgresqlMapper =
      Mappers.getMapper(UserFromPostgresqlMapper.class);

  @Spy UserDaoMapper userDaoMapper = Mappers.getMapper(UserDaoMapper.class);

  @Test
  void saveUser_shouldPersistUser_whenUserIsValid() {
    final User user = UserTestDataBuilder.builder().build().user();
    final UserDao userDao = UserDaoTestDataBuilder.builder().build().userDao();

    Mockito.when(userPostgresqlWriteRepository.save(userDao)).thenReturn(userDao);

    User result = userPostgresqlWriteAdapterImpl.saveUser(user);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(user);

    Mockito.verify(userDaoMapper).toUserDao(user);
    Mockito.verify(userPostgresqlWriteRepository).save(userDao);
    Mockito.verify(userFromPostgresqlMapper).toUser(userDao);
  }

  @Test
  void deleteUser_shouldReturnUser_whenUserExists() {
    final User user = UserTestDataBuilder.builder().build().user();
    final UserDao userDao = UserDaoTestDataBuilder.builder().build().userDao();

    Mockito.when(userPostgresqlWriteRepository.deleteByUserId(user.getId()))
        .thenReturn(Optional.of(userDao));

    final Optional<User> result = userPostgresqlWriteAdapterImpl.deleteUser(user.getId());

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(Optional.of(user));

    Mockito.verify(userPostgresqlWriteRepository).deleteByUserId(user.getId());
    Mockito.verify(userFromPostgresqlMapper).toUser(userDao);
  }

  @Test
  void deleteUser_shouldReturnEmpty_whenUserNotFound() {
    final UserDao userDao = UserDaoTestDataBuilder.builder().build().userDao();

    Mockito.when(userPostgresqlWriteRepository.deleteByUserId(userDao.getUserId()))
        .thenReturn(Optional.empty());

    final Optional<User> result = userPostgresqlWriteAdapterImpl.deleteUser(userDao.getUserId());

    AssertionsForClassTypes.assertThat(result).isEqualTo(Optional.empty());

    Mockito.verify(userPostgresqlWriteRepository).deleteByUserId(userDao.getUserId());
    Mockito.verify(userFromPostgresqlMapper, Mockito.never()).toUser(userDao);
  }
}
