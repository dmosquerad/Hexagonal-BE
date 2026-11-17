package com.architecture.hexagonal.application.service.user.impl;

import com.architecture.hexagonal.application.mapper.user.UserCreatedEventMapper;
import com.architecture.hexagonal.application.mapper.user.UserDeletedEventMapper;
import com.architecture.hexagonal.application.mapper.user.UserUpdatedEventMapper;
import com.architecture.hexagonal.application.service.user.UserService;
import com.architecture.hexagonal.domain.annotation.ApplicationService;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.projector.user.UserEmailProjector;
import com.architecture.hexagonal.domain.model.vo.email.EmailVo;
import com.architecture.hexagonal.domain.model.vo.exception.FailureType;
import com.architecture.hexagonal.domain.model.vo.pagination.PaginationResultVo;
import com.architecture.hexagonal.domain.model.vo.pagination.PaginationVo;
import com.architecture.hexagonal.domain.port.out.bus.event.EventBus;
import com.architecture.hexagonal.domain.port.out.repository.user.UserRepositoryReadPort;
import com.architecture.hexagonal.domain.port.out.repository.user.UserRepositoryWritePort;
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

    eventBus.publish(UserCreatedEventMapper.INSTANCE.toUserCreatedEvent(createdUser));
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
                        ExceptionMessage.NOT_FOUND_DATA_MESSAGE + userUuid,
                        FailureType.NON_RETRYABLE));

    eventBus.publish(UserDeletedEventMapper.INSTANCE.toUserDeletedEvent(deletedUser));
    return deletedUser;
  }

  @Override
  public void existsUser(final @NonNull UUID userUuid) {
    userRepositoryReadPort
        .findUserById(userUuid)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    ExceptionMessage.NOT_FOUND_DATA_MESSAGE + userUuid, FailureType.NON_RETRYABLE));
  }

  @Override
  public User findUserById(final @NonNull UUID userUuid) {
    return userRepositoryReadPort
        .findUserById(userUuid)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    ExceptionMessage.NOT_FOUND_DATA_MESSAGE + userUuid, FailureType.NON_RETRYABLE));
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
                    new ResourceNotFoundException(
                        ExceptionMessage.NOT_FOUND_DATA_MESSAGE + uuid, FailureType.NON_RETRYABLE));

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
            () ->
                new ResourceNotFoundException(
                    ExceptionMessage.NOT_FOUND_DATA_MESSAGE + uuid, FailureType.NON_RETRYABLE));

    final User updatedUser = userRepositoryWritePort.saveUser(user);

    publishUserUpdated(updatedUser);
    return updatedUser;
  }

  private void publishUserUpdated(final User updatedUser) {
    eventBus.publish(UserUpdatedEventMapper.INSTANCE.toUserUpdatedEvent(updatedUser));
  }
}
