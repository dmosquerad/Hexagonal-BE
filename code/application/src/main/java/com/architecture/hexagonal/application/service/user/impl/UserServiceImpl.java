package com.architecture.hexagonal.application.service.user.impl;

import com.architecture.hexagonal.application.annotation.ApplicationService;
import com.architecture.hexagonal.application.port.out.bus.event.EventBus;
import com.architecture.hexagonal.application.port.out.user.UserRepositoryReadPort;
import com.architecture.hexagonal.application.port.out.user.UserRepositoryWritePort;
import com.architecture.hexagonal.application.projector.user.UserEmailProjector;
import com.architecture.hexagonal.application.service.user.UserService;
import com.architecture.hexagonal.contract.bus.model.user.event.UserCreatedEvent;
import com.architecture.hexagonal.contract.bus.model.user.event.UserDeletedEvent;
import com.architecture.hexagonal.contract.bus.model.user.event.UserUpdatedEvent;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.email.EmailVo;
import com.architecture.hexagonal.domain.model.vo.pagination.PaginationResultVo;
import com.architecture.hexagonal.domain.model.vo.pagination.PaginationVo;
import java.util.Objects;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@ApplicationService
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepositoryReadPort userRepositoryReadPort;
  private final UserRepositoryWritePort userRepositoryWritePort;
  private final EventBus eventBus;

  @Override
  public User createUser(final @NonNull User user) {
    final User createdUser = userRepositoryWritePort.saveUser(user);

    final UserCreatedEvent userCreatedEvent = new UserCreatedEvent();
    userCreatedEvent.setUserId(createdUser.userId());
    userCreatedEvent.setName(createdUser.name());
    userCreatedEvent.setEmail(createdUser.email().getEmail());
    eventBus.publish(userCreatedEvent);
    return createdUser;
  }

  @Override
  public User deleteUser(final @NonNull UUID userUuid) {
    final User deletedUser =
        userRepositoryWritePort
            .deleteUser(userUuid)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        ExceptionMessage.NOT_FOUND_DATA_MESSAGE + userUuid));

    final UserDeletedEvent userDeletedEvent = new UserDeletedEvent();
    userDeletedEvent.setUserId(deletedUser.userId());
    userDeletedEvent.setName(deletedUser.name());
    userDeletedEvent.setEmail(deletedUser.email().getEmail());
    eventBus.publish(userDeletedEvent);
    return deletedUser;
  }

  @Override
  public void existsUser(final @NonNull UUID userUuid) {
    userRepositoryReadPort
        .findUserById(userUuid)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + userUuid));
  }

  @Override
  public User findUserById(final @NonNull UUID userUuid) {
    return userRepositoryReadPort
        .findUserById(userUuid)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + userUuid));
  }

  @Override
  public PaginationResultVo<User> getAllUsers(
      final @NonNull UserEmailProjector userEmailProjector) {
    return userRepositoryReadPort.getAllUsers(userEmailProjector);
  }

  @Override
  public PaginationResultVo<User> getAllUsers(
      final @NonNull UserEmailProjector userEmailProjector,
      final @NonNull PaginationVo paginationVo) {
    return userRepositoryReadPort.getAllUsers(userEmailProjector, paginationVo);
  }

  @Override
  public User patchUser(final @NonNull User user) {
    final UUID uuid = user.userId();
    final User currentUser =
        userRepositoryReadPort
            .findUserById(uuid)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + uuid));

    final EmailVo email = Objects.isNull(user.email()) ? currentUser.email() : user.email();

    final String name = StringUtils.isBlank(user.name()) ? currentUser.name() : user.name();
    final User updatedUser =
        userRepositoryWritePort.saveUser(
            User.builder().userId(uuid).name(name).email(email).build());

    publishUserUpdated(updatedUser);
    return updatedUser;
  }

  @Override
  public User updateUser(final @NonNull User user) {
    final UUID uuid = user.userId();
    userRepositoryReadPort
        .findUserById(uuid)
        .orElseThrow(
            () -> new ResourceNotFoundException(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + uuid));

    final User updatedUser = userRepositoryWritePort.saveUser(user);

    publishUserUpdated(updatedUser);
    return updatedUser;
  }

  private void publishUserUpdated(final User updatedUser) {
    final UserUpdatedEvent userUpdatedEvent = new UserUpdatedEvent();
    userUpdatedEvent.setUserId(updatedUser.userId());
    userUpdatedEvent.setName(updatedUser.name());
    userUpdatedEvent.setEmail(updatedUser.email().getEmail());
    eventBus.publish(userUpdatedEvent);
  }
}
