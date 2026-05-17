package com.architecture.hexagonal.infrastructure.outbound.database.adapter;

import com.architecture.hexagonal.application.common.pagination.Pagination;
import com.architecture.hexagonal.application.common.pagination.PaginationResult;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.infrastructure.outbound.database.data.UserDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mapper.UserMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.repository.UserDatabaseReadRepository;
import com.architecture.hexagonal.infrastructure.outbound.database.testutils.data.dao.UserDaoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.testutils.data.aggregate.UserTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.testutils.data.pagination.PaginationResultTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.testutils.data.pagination.PaginationTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.testutils.data.vo.EmailBlockRulesVoTestDataBuilder;
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
class UserRepositoryDatabaseReadAdapterTest
{

  @InjectMocks
  UserRepositoryDatabaseReadAdapter userRepositoryDatabaseReadAdapter;

  @Mock
  UserDatabaseReadRepository userDatabaseReadRepository;

  @Spy
  UserMapper userMapper = Mappers.getMapper(UserMapper.class);

  @Test
  void getAllUsers_shouldReturnAllUsers_whenNoFiltersAreApplied() {
    final String host = "";
    final UserDao userDao = UserDaoTestDataBuilder
        .builder()
        .build()
        .userDao();
    final Pagination pagination = PaginationTestDataBuilder.builder().build().pagination();
    final Pageable pageable = PageRequest.of(0, 100);

    Mockito.when(userDatabaseReadRepository.findAll(
        Mockito.<Specification<UserDao>>any(),
        Mockito.eq(pageable)))
        .thenReturn(new PageImpl<>(Collections.singletonList(userDao), pageable, 1));

    final User expectedUser = UserTestDataBuilder.builder().build().user();
    final PaginationResult<User> expectedResult = PaginationResultTestDataBuilder.<User>builder()
        .data(Collections.singleton(expectedUser))
        .build()
        .paginationResult();

    PaginationResult<User> result = userRepositoryDatabaseReadAdapter.getAllUsers(host, pagination);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(expectedResult);

    Mockito.verify(userDatabaseReadRepository).findAll(Mockito.<Specification<UserDao>>any(), Mockito.eq(pageable));
  }

  @Test
  void findUserById_shouldReturnUser_whenUserExists() {
    final UserDao userDao = UserDaoTestDataBuilder
        .builder()
        .build()
        .userDao();

    Mockito.when(userDatabaseReadRepository.findByUserId(userDao.getUserId()))
        .thenReturn(Optional.of(userDao));

    final Optional<User> result = userRepositoryDatabaseReadAdapter.findUserById(userDao.getUserId());

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(Optional.of(UserTestDataBuilder.builder().build().user()));

    Mockito.verify(userDatabaseReadRepository).findByUserId(userDao.getUserId());
    Mockito.verify(userMapper).toUser(userDao);
  }

  @Test
  void findUserById_shouldReturnEmpty_whenUserNotFound() {
    final UserDao userDao = UserDaoTestDataBuilder
            .builder()
            .build()
            .userDao();

    Mockito.when(userDatabaseReadRepository.findByUserId(userDao.getUserId()))
        .thenReturn(Optional.empty());

    final Optional<User> result = userRepositoryDatabaseReadAdapter.findUserById(userDao.getUserId());

    AssertionsForClassTypes.assertThat(result).isEqualTo(Optional.empty());

    Mockito.verify(userDatabaseReadRepository).findByUserId(userDao.getUserId());
    Mockito.verify(userMapper, Mockito.never()).toUser(userDao);
  }

  @Test
  void getAllUsers_shouldReturnPagedUsersByHostAndBlockedHosts() {
    final UserDao userDao = UserDaoTestDataBuilder
        .builder()
        .build()
        .userDao();
    final Pagination pagination = PaginationTestDataBuilder.builder().build().pagination();
    final Pageable pageable = PageRequest.of(0, 100);

    Mockito.when(userDatabaseReadRepository.findAll(
        Mockito.<Specification<UserDao>>any(),
        Mockito.eq(pageable)))
        .thenReturn(new PageImpl<>(Collections.singletonList(userDao), pageable, 1));

    final String host = "example";
    final Boolean blockEmail = true;
    final User expectedUser = UserTestDataBuilder.builder().build().user();
    final PaginationResult<User> expectedResult = PaginationResultTestDataBuilder.<User>builder()
        .data(Collections.singleton(expectedUser))
        .build()
        .paginationResult();

    PaginationResult<User> result = userRepositoryDatabaseReadAdapter.getAllUsers(
        host,
        blockEmail,
        EmailBlockRulesVoTestDataBuilder.builder().host(Set.of(host)).build().emailBlockRulesVo(),
        pagination);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(expectedResult);

    Mockito.verify(userDatabaseReadRepository).findAll(Mockito.<Specification<UserDao>>any(), Mockito.eq(pageable));
  }

  @Test
  void getAllUsers_shouldReturnAllUsers_whenPaginationIsNull() {
    final String host = "example";
    final Boolean blockEmail = false;
    final UserDao userDao = UserDaoTestDataBuilder
        .builder()
        .build()
        .userDao();

    Mockito.when(userDatabaseReadRepository.findAll(Mockito.<Specification<UserDao>>any()))
        .thenReturn(Collections.singletonList(userDao));

    final User expectedUser = UserTestDataBuilder.builder().build().user();
    final PaginationResult<User> expectedResult = PaginationResultTestDataBuilder.<User>builder()
        .data(Collections.singleton(expectedUser))
        .size(1)
        .build()
        .paginationResult();

    PaginationResult<User> result = userRepositoryDatabaseReadAdapter.getAllUsers(
        host,
        blockEmail,
        EmailBlockRulesVoTestDataBuilder.builder().build().emailBlockRulesVo());

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(expectedResult);

    Mockito.verify(userDatabaseReadRepository).findAll(Mockito.<Specification<UserDao>>any());
  }

  @Test
  void getAllUsers_shouldReturnSecondPage_whenCustomPaginationIsProvided() {
    final String host = "";
    final UserDao userDao = UserDaoTestDataBuilder.builder().build().userDao();
    final Pagination pagination = PaginationTestDataBuilder.builder().page(1).size(5).build().pagination();
    final Pageable pageable = PageRequest.of(1, 5);

    Mockito.when(userDatabaseReadRepository.findAll(
            Mockito.<Specification<UserDao>>any(),
            Mockito.eq(pageable)))
        .thenReturn(new PageImpl<>(Collections.singletonList(userDao), pageable, 11));

    final User expectedUser = UserTestDataBuilder.builder().build().user();
    final PaginationResult<User> expectedResult = PaginationResultTestDataBuilder.<User>builder()
        .data(Collections.singleton(expectedUser))
        .totalElements(11L)
        .totalPages(3)
        .page(1)
        .size(5)
        .build()
        .paginationResult();

    PaginationResult<User> result = userRepositoryDatabaseReadAdapter.getAllUsers(host, pagination);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .ignoringFieldsOfTypes(UUID.class)
        .isEqualTo(expectedResult);

    Mockito.verify(userDatabaseReadRepository).findAll(
        Mockito.<Specification<UserDao>>any(), Mockito.eq(pageable));
  }

}
