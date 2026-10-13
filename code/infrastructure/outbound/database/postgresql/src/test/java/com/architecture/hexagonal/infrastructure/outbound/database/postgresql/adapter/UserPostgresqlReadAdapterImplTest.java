package com.architecture.hexagonal.infrastructure.outbound.database.postgresql.adapter;

import com.architecture.hexagonal.application.testutils.projector.user.UserEmailProjectorTestDataBuilder;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.pagination.PaginationResultVo;
import com.architecture.hexagonal.domain.model.vo.pagination.PaginationVo;
import com.architecture.hexagonal.domain.testutils.data.model.entity.user.UserTestDataBuilder;
import com.architecture.hexagonal.domain.testutils.data.model.vo.email.EmailBlockRulesVoTestDataBuilder;
import com.architecture.hexagonal.domain.testutils.data.model.vo.pagination.PaginationResultVoTestDataBuilder;
import com.architecture.hexagonal.domain.testutils.data.model.vo.pagination.PaginationVoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.data.UserDao;
import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.mapper.user.UserFromPostgresqlMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.repository.UserPostgresqlReadRepository;
import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.testutils.data.UserDaoTestDataBuilder;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class UserPostgresqlReadAdapterImplTest {

  @InjectMocks UserPostgresqlReadAdapterImpl userPostgresqlReadAdapterImpl;

  @Mock UserPostgresqlReadRepository userPostgresqlReadRepository;

  @Spy
  UserFromPostgresqlMapper userFromPostgresqlMapper =
      Mappers.getMapper(UserFromPostgresqlMapper.class);

  @Test
  void findUserByIdShouldReturnUserWhenUserExists() {
    final UserDao userDao = UserDaoTestDataBuilder.builder().build().userDao();

    Mockito.when(userPostgresqlReadRepository.findByUserId(userDao.getUserId()))
        .thenReturn(Optional.of(userDao));

    final Optional<User> result = userPostgresqlReadAdapterImpl.findUserById(userDao.getUserId());

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(Optional.of(UserTestDataBuilder.builder().build().user()));

    Mockito.verify(userPostgresqlReadRepository).findByUserId(userDao.getUserId());
    Mockito.verify(userFromPostgresqlMapper).toUser(userDao);
  }

  @Test
  void findUserByIdShouldReturnEmptyWhenUserNotFound() {
    final UserDao userDao = UserDaoTestDataBuilder.builder().build().userDao();

    Mockito.when(userPostgresqlReadRepository.findByUserId(userDao.getUserId()))
        .thenReturn(Optional.empty());

    final Optional<User> result = userPostgresqlReadAdapterImpl.findUserById(userDao.getUserId());

    AssertionsForClassTypes.assertThat(result).isEqualTo(Optional.empty());

    Mockito.verify(userPostgresqlReadRepository).findByUserId(userDao.getUserId());
    Mockito.verify(userFromPostgresqlMapper, Mockito.never()).toUser(userDao);
  }

  @Test
  void getAllUsersShouldReturnPagedUsersByHostAndBlockedHosts() {
    final UserDao userDao = UserDaoTestDataBuilder.builder().build().userDao();
    final PaginationVo paginationVo = PaginationVoTestDataBuilder.builder().build().paginationVo();
    final Pageable pageable = PageRequest.of(0, 100);

    Mockito.when(
            userPostgresqlReadRepository.findAll(
                Mockito.<Specification<UserDao>>any(), Mockito.eq(pageable)))
        .thenReturn(new PageImpl<>(Collections.singletonList(userDao), pageable, 1));

    final String host = "example";
    final Boolean blockEmail = true;
    final User expectedUser = UserTestDataBuilder.builder().build().user();
    final PaginationResultVo<User> expectedResult =
        PaginationResultVoTestDataBuilder.<User>builder()
            .data(Collections.singletonList(expectedUser))
            .build()
            .paginationResult();

    final PaginationResultVo<User> result =
        userPostgresqlReadAdapterImpl.getAllUsers(
            UserEmailProjectorTestDataBuilder.builder()
                .blockEmail(blockEmail)
                .blockedRules(
                    EmailBlockRulesVoTestDataBuilder.builder()
                        .host(Set.of(host))
                        .build()
                        .emailBlockRulesVo())
                .build()
                .userEmailProjector(),
            paginationVo);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(expectedResult);

    Mockito.verify(userPostgresqlReadRepository)
        .findAll(Mockito.<Specification<UserDao>>any(), Mockito.eq(pageable));
  }

  @Test
  void getAllUsersShouldReturnAllUsersWhenPaginationIsNull() {
    final String host = "example";
    final UserDao userDao = UserDaoTestDataBuilder.builder().build().userDao();

    Mockito.when(userPostgresqlReadRepository.findAll(Mockito.<Specification<UserDao>>any()))
        .thenReturn(Collections.singletonList(userDao));

    final User expectedUser = UserTestDataBuilder.builder().build().user();
    final PaginationResultVo<User> expectedResult =
        PaginationResultVoTestDataBuilder.<User>builder()
            .data(Collections.singletonList(expectedUser))
            .size(1)
            .build()
            .paginationResult();

    final PaginationResultVo<User> result =
        userPostgresqlReadAdapterImpl.getAllUsers(
            UserEmailProjectorTestDataBuilder.builder()
                .blockedRules(
                    EmailBlockRulesVoTestDataBuilder.builder()
                        .host(Set.of(host))
                        .build()
                        .emailBlockRulesVo())
                .build()
                .userEmailProjector());

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(expectedResult);

    Mockito.verify(userPostgresqlReadRepository).findAll(Mockito.<Specification<UserDao>>any());
  }
}
