package com.architecture.hexagonal.infrastructure.inbound.rest.controller;

import com.architecture.hexagonal.application.port.in.CreateUserUseCasePort;
import com.architecture.hexagonal.application.port.in.DeleteUserUseCasePort;
import com.architecture.hexagonal.application.port.in.FindUserByUserIdUseCasePort;
import com.architecture.hexagonal.application.port.in.GetAllUsersUseCasePort;
import com.architecture.hexagonal.application.port.in.PatchUserUseCasePort;
import com.architecture.hexagonal.application.port.in.UpdateUserUseCasePort;
import com.architecture.hexagonal.application.port.in.UserExistsUseCasePort;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.data.entity.User;
import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UserCreateDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.factory.ErrorResponseFactory;
import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UserPatchDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UserResponseDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UserUpdateDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UsersResponseDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.CreateUserCommandMapper;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.DeleteUserCommandMapper;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.FindUserByUserIdQueryMapper;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.PatchUserCommandMapper;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.UserReadDtoMapper;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.UserExistsQueryMapper;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.UpdateUserCommandMapper;
import java.time.Clock;
import java.time.OffsetDateTime;
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

  private final CreateUserUseCasePort createUserUseCasePort;

  private final FindUserByUserIdUseCasePort findUserByUserIdUseCasePort;

  private final DeleteUserUseCasePort deleteUserUseCasePort;

  private final UpdateUserUseCasePort updateUserUseCasePort;

  private final PatchUserUseCasePort patchUserUseCasePort;

  private final UserExistsUseCasePort userExistsUseCasePort;

  private final UserReadDtoMapper userReadDtoMapper;

  private final CreateUserCommandMapper createUserCommandMapper;

  private final DeleteUserCommandMapper deleteUserCommandMapper;

  private final UpdateUserCommandMapper updateUserCommandMapper;

  private final PatchUserCommandMapper patchUserCommandMapper;

  private final FindUserByUserIdQueryMapper findUserByUserIdQueryMapper;

  private final UserExistsQueryMapper userExistsQueryMapper;

  private final Clock clock;

  @Override
  public ResponseEntity<UsersResponseDto> getAllUsers() {
    final UsersResponseDto userResponseDto = new UsersResponseDto();
    userResponseDto.setDate(OffsetDateTime.now(clock));
    userResponseDto.setStatus(HttpStatus.OK.value());
    userResponseDto.setData(this.getAllUsersUseCasePort.execute().stream()
        .map(userReadDtoMapper::toUserReadDto)
        .collect(Collectors.toUnmodifiableSet()));

    return ResponseEntity.ok(userResponseDto);
  }

  @Override
  public ResponseEntity<UserResponseDto> createUser(final UserCreateDto userCreateDto) {
    try {
      return ResponseEntity.ok(buildUserResponse(
          createUserUseCasePort.execute(
            createUserCommandMapper.toCreateUserCommand(userCreateDto))));
    } catch (IllegalArgumentException e) {
      throw ErrorResponseFactory.of(HttpStatus.BAD_REQUEST, e);
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
          deleteUserUseCasePort.execute(deleteUserCommandMapper.toDeleteUserCommand(userUuid))));
    } catch (ResourceNotFoundException e) {
      throw ErrorResponseFactory.of(HttpStatus.NOT_FOUND, e);
    }
  }

  @Override
  public ResponseEntity<UserResponseDto> updateUserByUuid(
      final UUID userUuid, final UserUpdateDto userUpdateDto) {
    try {
      return ResponseEntity.ok(buildUserResponse(
          updateUserUseCasePort.execute(
            updateUserCommandMapper.toUpdateUserCommand(userUuid, userUpdateDto))));
    } catch (ResourceNotFoundException e) {
      throw ErrorResponseFactory.of(HttpStatus.NOT_FOUND, e);
    } catch (IllegalArgumentException e) {
      throw ErrorResponseFactory.of(HttpStatus.BAD_REQUEST, e);
    }
  }

  @Override
  public ResponseEntity<UserResponseDto> patchUserByUuid(
      final UUID userUuid, final UserPatchDto userPatchDto) {
    try {
      return ResponseEntity.ok(buildUserResponse(
          patchUserUseCasePort.execute(
            patchUserCommandMapper.toPatchUserCommand(userUuid, userPatchDto))));
    } catch (ResourceNotFoundException e) {
      throw ErrorResponseFactory.of(HttpStatus.NOT_FOUND, e);
    } catch (IllegalArgumentException e) {
      throw ErrorResponseFactory.of(HttpStatus.BAD_REQUEST, e);
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
    final UserResponseDto dto = new UserResponseDto();
    dto.setDate(OffsetDateTime.now(clock));
    dto.setStatus(HttpStatus.OK.value());
    dto.setData(userReadDtoMapper.toUserReadDto(user));
    return dto;
  }
}
