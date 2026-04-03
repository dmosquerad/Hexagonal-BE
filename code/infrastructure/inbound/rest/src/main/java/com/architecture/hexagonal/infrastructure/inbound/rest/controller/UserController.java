package com.architecture.hexagonal.infrastructure.inbound.rest.controller;

import com.architecture.hexagonal.application.bus.command.CommandBus;
import com.architecture.hexagonal.application.port.in.FindUserByUserIdUseCasePort;
import com.architecture.hexagonal.application.port.in.GetAllUsersUseCasePort;
import com.architecture.hexagonal.application.port.in.UserExistsUseCasePort;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.model.entity.User;
import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UserCreateDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.factory.ErrorResponseFactory;
import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UserPatchDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UserResponseDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UserUpdateDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UsersResponseDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.CreateUserCommandMapper;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.DeleteUserCommandMapper;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.FindUserByUserIdQueryMapper;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.GetAllUserQueryMapper;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.PatchUserCommandMapper;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.UserReadDtoMapper;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.UserExistsQueryMapper;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.UpdateUserCommandMapper;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements UsersApi {

  private final GetAllUsersUseCasePort getAllUsersUseCasePort;

  private final FindUserByUserIdUseCasePort findUserByUserIdUseCasePort;

  private final UserExistsUseCasePort userExistsUseCasePort;

  private final CommandBus commandBus;

  private final UserReadDtoMapper userReadDtoMapper;

  private final CreateUserCommandMapper createUserCommandMapper;

  private final DeleteUserCommandMapper deleteUserCommandMapper;

  private final UpdateUserCommandMapper updateUserCommandMapper;

  private final PatchUserCommandMapper patchUserCommandMapper;

  private final FindUserByUserIdQueryMapper findUserByUserIdQueryMapper;

  private final UserExistsQueryMapper userExistsQueryMapper;

  private final GetAllUserQueryMapper getAllUserQueryMapper;

  private final Clock clock;

  @Override
  public ResponseEntity<UsersResponseDto> getAllUsers(final String host, final Boolean blockEmail) {
          return ResponseEntity.ok(buildUsersResponse(
        this.getAllUsersUseCasePort.execute(
                  getAllUserQueryMapper.toGetAllUserQuery(host, blockEmail))));
  }

  @Override
  public ResponseEntity<UserResponseDto> createUser(final UserCreateDto userCreateDto) {
    try {
      return ResponseEntity.ok(buildUserResponse(
          commandBus.execute(
            createUserCommandMapper.toCreateUserCommand(userCreateDto))));
    } catch (IllegalArgumentException e) {
      throw ErrorResponseFactory.of(HttpStatus.BAD_REQUEST, e);
    } catch (Exception e) {
      throw ErrorResponseFactory.of(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }
  }

  @Override
  public ResponseEntity<UserResponseDto> getUserByUuid(final UUID userUuid) {
    try {
      return ResponseEntity.ok(buildUserResponse(
          findUserByUserIdUseCasePort.execute(
              findUserByUserIdQueryMapper.toFindUserByUserIdQuery(userUuid))));
    } catch (ResourceNotFoundException e) {
      throw ErrorResponseFactory.of(HttpStatus.NOT_FOUND, e);
    }
  }

  @Override
  public ResponseEntity<UserResponseDto> deleteUserByUuid(final UUID userUuid) {
    try {
      return ResponseEntity.ok(buildUserResponse(
          commandBus.execute(deleteUserCommandMapper.toDeleteUserCommand(userUuid))));
    } catch (ResourceNotFoundException e) {
      throw ErrorResponseFactory.of(HttpStatus.NOT_FOUND, e);
    } catch (Exception e) {
      throw ErrorResponseFactory.of(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }
  }

  @Override
  public ResponseEntity<UserResponseDto> updateUserByUuid(
      final UUID userUuid, final UserUpdateDto userUpdateDto) {
    try {
      return ResponseEntity.ok(buildUserResponse(
          commandBus.execute(
            updateUserCommandMapper.toUpdateUserCommand(userUuid, userUpdateDto))));
    } catch (ResourceNotFoundException e) {
      throw ErrorResponseFactory.of(HttpStatus.NOT_FOUND, e);
    } catch (IllegalArgumentException e) {
      throw ErrorResponseFactory.of(HttpStatus.BAD_REQUEST, e);
    } catch (Exception e) {
      throw ErrorResponseFactory.of(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }
  }

  @Override
  public ResponseEntity<UserResponseDto> patchUserByUuid(
      final UUID userUuid, final UserPatchDto userPatchDto) {
    try {
      return ResponseEntity.ok(buildUserResponse(
          commandBus.execute(
            patchUserCommandMapper.toPatchUserCommand(userUuid, userPatchDto))));
    } catch (ResourceNotFoundException e) {
      throw ErrorResponseFactory.of(HttpStatus.NOT_FOUND, e);
    } catch (IllegalArgumentException e) {
      throw ErrorResponseFactory.of(HttpStatus.BAD_REQUEST, e);
    } catch (Exception e) {
      throw ErrorResponseFactory.of(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }
  }

  @Override
  public ResponseEntity<Void> headUserByUuid(final UUID userUuid) {
    try {
      userExistsUseCasePort.execute(userExistsQueryMapper.toUserExistsQuery(userUuid));
    } catch (ResourceNotFoundException e) {
      throw new ErrorResponseException(HttpStatus.NOT_FOUND, e);
    }

      return ResponseEntity.ok().build();
  }

  private UserResponseDto buildUserResponse(final User user) {
    final UserResponseDto userResponseDto = new UserResponseDto();
    userResponseDto.setDate(OffsetDateTime.now(clock));
    userResponseDto.setStatus(HttpStatus.OK.value());
    userResponseDto.setData(userReadDtoMapper.toUserReadDto(user));
    return userResponseDto;
  }

  private UsersResponseDto buildUsersResponse(final Set<User> users) {
    final UsersResponseDto usersResponseDto = new UsersResponseDto();
    usersResponseDto.setDate(OffsetDateTime.now(clock));
    usersResponseDto.setStatus(HttpStatus.OK.value());
    usersResponseDto.setData(users.stream()
        .map(userReadDtoMapper::toUserReadDto)
        .collect(Collectors.toUnmodifiableSet()));

    return usersResponseDto;
  }
}
