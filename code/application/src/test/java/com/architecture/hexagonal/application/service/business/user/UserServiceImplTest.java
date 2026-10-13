package com.architecture.hexagonal.application.service.business.user;

import com.architecture.hexagonal.application.port.out.user.UserRepositoryReadPort;
import com.architecture.hexagonal.application.port.out.user.UserRepositoryWritePort;
import com.architecture.hexagonal.application.port.out.user.UserSenderPort;
import com.architecture.hexagonal.application.projector.user.UserEmailProjector;
import com.architecture.hexagonal.application.service.business.user.impl.UserServiceImpl;
import com.architecture.hexagonal.application.testutils.projector.user.UserEmailProjectorTestDataBuilder;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.email.EmailBlockRulesVo;
import com.architecture.hexagonal.domain.model.vo.pagination.PaginationResultVo;
import com.architecture.hexagonal.domain.model.vo.pagination.PaginationVo;
import com.architecture.hexagonal.domain.testutils.data.model.entity.user.UserTestDataBuilder;
import com.architecture.hexagonal.domain.testutils.data.model.vo.email.EmailBlockRulesVoTestDataBuilder;
import com.architecture.hexagonal.domain.testutils.data.model.vo.pagination.PaginationResultVoTestDataBuilder;
import com.architecture.hexagonal.domain.testutils.data.model.vo.pagination.PaginationVoTestDataBuilder;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @InjectMocks UserServiceImpl userService;

  @Mock UserRepositoryWritePort userRepositoryWritePort;

  @Mock UserRepositoryReadPort userRepositoryReadPort;

  @Mock UserSenderPort userSenderPort;

  @Test
  void createShouldCreateUserUserWhenEmailIsAllowed() {
    final User user = UserTestDataBuilder.builder().build().user();
    final User savedUser =
        UserTestDataBuilder.builder()
            .userId(UUID.fromString("9a3cc2bd-e1b4-451c-b807-637c0b927817"))
            .build()
            .user();

    Mockito.when(userRepositoryWritePort.saveUser(user)).thenReturn(savedUser);

    final User result = userService.createUser(user);

    AssertionsForClassTypes.assertThat(result).usingRecursiveComparison().isEqualTo(savedUser);

    Mockito.verify(userRepositoryWritePort).saveUser(user);
    Mockito.verify(userSenderPort).userSenderCreated(savedUser);
  }

  @Test
  void deleteShouldDeleteUserUserWhenUserExistsUser() {
    final User user = UserTestDataBuilder.builder().build().user();

    Mockito.when(userRepositoryWritePort.deleteUser(user.userId())).thenReturn(Optional.of(user));

    final User result = userService.deleteUser(user.userId());

    AssertionsForClassTypes.assertThat(result).usingRecursiveComparison().isEqualTo(user);

    Mockito.verify(userRepositoryWritePort).deleteUser(user.userId());
    Mockito.verify(userSenderPort).userSenderDeleted(user);
  }

  @Test
  void deleteUserShouldThrowResourceNotFoundExceptionWhenUserNotFound() {
    final User user = UserTestDataBuilder.builder().build().user();

    Mockito.when(userRepositoryWritePort.deleteUser(user.userId())).thenReturn(Optional.empty());

    AssertionsForClassTypes.assertThatThrownBy(() -> userService.deleteUser(user.userId()))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + user.userId());

    Mockito.verify(userRepositoryWritePort).deleteUser(user.userId());
    Mockito.verifyNoInteractions(userSenderPort);
  }

  @Test
  void existsShouldNotThrowWhenUserExistsUser() {
    final User user = UserTestDataBuilder.builder().build().user();

    Mockito.when(userRepositoryReadPort.findUserById(user.userId())).thenReturn(Optional.of(user));

    userService.existsUser(user.userId());

    Mockito.verify(userRepositoryReadPort).findUserById(user.userId());
  }

  @Test
  void existsUserShouldThrowResourceNotFoundExceptionWhenUserNotFound() {
    final User user = UserTestDataBuilder.builder().build().user();

    Mockito.when(userRepositoryReadPort.findUserById(user.userId())).thenReturn(Optional.empty());

    AssertionsForClassTypes.assertThatThrownBy(() -> userService.existsUser(user.userId()))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + user.userId());

    Mockito.verify(userRepositoryReadPort).findUserById(user.userId());
  }

  @Test
  void findUserByIdShouldReturnUserWhenUserExistsUser() {
    final User user = UserTestDataBuilder.builder().build().user();

    Mockito.when(userRepositoryReadPort.findUserById(user.userId())).thenReturn(Optional.of(user));

    final User result = userService.findUserById(user.userId());

    AssertionsForClassTypes.assertThat(result).usingRecursiveComparison().isEqualTo(user);

    Mockito.verify(userRepositoryReadPort).findUserById(user.userId());
  }

  @Test
  void findUserByIdShouldThrowResourceNotFoundExceptionWhenUserNotFound() {
    final User user = UserTestDataBuilder.builder().build().user();

    Mockito.when(userRepositoryReadPort.findUserById(user.userId())).thenReturn(Optional.empty());

    AssertionsForClassTypes.assertThatThrownBy(() -> userService.findUserById(user.userId()))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + user.userId());

    Mockito.verify(userRepositoryReadPort).findUserById(user.userId());
  }

  @Test
  void getAllUsersShouldCallUnpaginatedRepositoryWhenPaginationIsNotProvided() {
    final List<User> users =
        Collections.singletonList(UserTestDataBuilder.builder().build().user());
    final UserEmailProjector projector =
        UserEmailProjectorTestDataBuilder.builder().build().userEmailProjector();
    final PaginationResultVo<User> expectedResult =
        PaginationResultVoTestDataBuilder.<User>builder().data(users).build().paginationResult();

    Mockito.when(userRepositoryReadPort.getAllUsers(projector)).thenReturn(expectedResult);

    final PaginationResultVo<User> result = userService.getAllUsers(projector);

    AssertionsForClassTypes.assertThat(result).usingRecursiveComparison().isEqualTo(expectedResult);

    Mockito.verify(userRepositoryReadPort).getAllUsers(projector);
  }

  @Test
  void getAllUsersShouldRejectNullPaginationInPaginatedOverload() {
    final UserEmailProjector projector =
        UserEmailProjectorTestDataBuilder.builder().build().userEmailProjector();

    AssertionsForClassTypes.assertThatThrownBy(() -> userService.getAllUsers(projector, null))
        .isInstanceOf(NullPointerException.class);

    Mockito.verifyNoInteractions(userRepositoryReadPort);
  }

  @Test
  void getAllUsersShouldReturnBlockedEmailUsersWhenBlockEmailIsTrue() {
    final User user = UserTestDataBuilder.builder().build().user();
    final List<User> users = Collections.singletonList(user);
    final PaginationVo paginationVo = PaginationVoTestDataBuilder.builder().build().paginationVo();
    final EmailBlockRulesVo blockedRules =
        EmailBlockRulesVoTestDataBuilder.builder()
            .host(Set.of("example"))
            .build()
            .emailBlockRulesVo();
    final UserEmailProjector projector =
        UserEmailProjectorTestDataBuilder.builder()
            .host("")
            .blockEmail(true)
            .blockedRules(blockedRules)
            .build()
            .userEmailProjector();
    final PaginationResultVo<User> expectedResult =
        PaginationResultVoTestDataBuilder.<User>builder().data(users).build().paginationResult();

    Mockito.when(userRepositoryReadPort.getAllUsers(projector, paginationVo))
        .thenReturn(expectedResult);

    final PaginationResultVo<User> result = userService.getAllUsers(projector, paginationVo);

    AssertionsForClassTypes.assertThat(result.data()).usingRecursiveComparison().isEqualTo(users);

    Mockito.verify(userRepositoryReadPort).getAllUsers(projector, paginationVo);
  }

  @Test
  void getAllUsersShouldReturnAllowedEmailUsersWhenBlockEmailIsFalse() {
    final User user = UserTestDataBuilder.builder().build().user();
    final List<User> users = Collections.singletonList(user);
    final PaginationVo paginationVo = PaginationVoTestDataBuilder.builder().build().paginationVo();
    final UserEmailProjector projector =
        UserEmailProjectorTestDataBuilder.builder().host("").build().userEmailProjector();
    final PaginationResultVo<User> expectedResult =
        PaginationResultVoTestDataBuilder.<User>builder().data(users).build().paginationResult();

    Mockito.when(userRepositoryReadPort.getAllUsers(projector, paginationVo))
        .thenReturn(expectedResult);

    final PaginationResultVo<User> result = userService.getAllUsers(projector, paginationVo);

    AssertionsForClassTypes.assertThat(result.data()).usingRecursiveComparison().isEqualTo(users);

    Mockito.verify(userRepositoryReadPort).getAllUsers(projector, paginationVo);
  }

  @Test
  void patchShouldPatchUserUserNameWhenRequestIsValid() {
    final User user = UserTestDataBuilder.builder().build().user();

    Mockito.when(userRepositoryReadPort.findUserById(user.userId())).thenReturn(Optional.of(user));
    Mockito.when(userRepositoryWritePort.saveUser(user)).thenReturn(user);

    final User result = userService.patchUser(user);

    AssertionsForClassTypes.assertThat(result).usingRecursiveComparison().isEqualTo(user);

    Mockito.verify(userRepositoryReadPort).findUserById(user.userId());
    Mockito.verify(userRepositoryWritePort).saveUser(user);
    Mockito.verify(userSenderPort).userSenderUpdated(user);
  }

  @Test
  void patchUserShouldThrowResourceNotFoundExceptionWhenUserNotFound() {
    final User patch = UserTestDataBuilder.builder().build().user();

    Mockito.when(userRepositoryReadPort.findUserById(patch.userId())).thenReturn(Optional.empty());

    AssertionsForClassTypes.assertThatThrownBy(() -> userService.patchUser(patch))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + patch.userId());

    Mockito.verify(userRepositoryReadPort).findUserById(patch.userId());
    Mockito.verifyNoInteractions(userRepositoryWritePort);
    Mockito.verifyNoInteractions(userSenderPort);
  }

  @Test
  void updateShouldUpdateUserUserWhenUserExistsUser() {
    final User user = UserTestDataBuilder.builder().build().user();

    Mockito.when(userRepositoryReadPort.findUserById(user.userId())).thenReturn(Optional.of(user));
    Mockito.when(userRepositoryWritePort.saveUser(user)).thenReturn(user);

    final User result = userService.updateUser(user);

    AssertionsForClassTypes.assertThat(result).usingRecursiveComparison().isEqualTo(user);

    Mockito.verify(userRepositoryReadPort).findUserById(user.userId());
    Mockito.verify(userRepositoryWritePort).saveUser(user);
    Mockito.verify(userSenderPort).userSenderUpdated(user);
  }

  @Test
  void updateUserShouldThrowResourceNotFoundExceptionWhenUserNotFound() {
    final User user = UserTestDataBuilder.builder().build().user();

    Mockito.when(userRepositoryReadPort.findUserById(user.userId())).thenReturn(Optional.empty());

    AssertionsForClassTypes.assertThatThrownBy(() -> userService.updateUser(user))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + user.userId());

    Mockito.verify(userRepositoryReadPort).findUserById(user.userId());
    Mockito.verifyNoInteractions(userRepositoryWritePort);
    Mockito.verifyNoInteractions(userSenderPort);
  }
}
