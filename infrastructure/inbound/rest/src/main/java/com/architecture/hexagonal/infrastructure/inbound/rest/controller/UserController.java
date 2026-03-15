package com.architecture.hexagonal.infrastructure.inbound.rest.controller;

import com.architecture.hexagonal.domain.data.User;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.input.query.FindUserByUserIdQuery;
import com.architecture.hexagonal.domain.port.in.CreateUserUseCasePort;
import com.architecture.hexagonal.domain.port.in.FindUserByUserIdUseCasePort;
import com.architecture.hexagonal.domain.port.in.GetAllUsersUseCasePort;
import com.architecture.hexagonal.domain.input.command.CreateUserCommand;
import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UserCreateDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UserResponseDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UsersResponseDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.UserReadDtoMapper;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
public class UserController implements UsersApi {

  private final GetAllUsersUseCasePort getAllUsersUseCasePort;

  private final CreateUserUseCasePort createUserUseCasePort;

  private final FindUserByUserIdUseCasePort findUserByUserIdUseCasePort;

  private final UserReadDtoMapper userReadDtoMapper;

  private final Clock clock;

  @Override
  public ResponseEntity<UsersResponseDto> getAllUsers() {
    final UsersResponseDto userResponseDto = new UsersResponseDto();
    userResponseDto.setDate(OffsetDateTime.now(clock));
    userResponseDto.setStatus(HttpStatus.OK.value());
    userResponseDto.setData(userReadDtoMapper.toUserReadDtoSet(this.getAllUsersUseCasePort.execute()));

    return ResponseEntity.ok(userResponseDto);
  }

  @Override
  public ResponseEntity<UserResponseDto> createUser(UserCreateDto userCreateDto) {
    final UserResponseDto userResponseDto = new UserResponseDto();
    userResponseDto.setDate(OffsetDateTime.now(clock));
    userResponseDto.setStatus(HttpStatus.OK.value());
    userResponseDto.setData(userReadDtoMapper.toUserReadDto(createUserUseCasePort.execute(
        CreateUserCommand.builder().name(userCreateDto.getName()).email(userCreateDto.getEmail()).build())));

    return ResponseEntity.ok(userResponseDto);
  }

  @Override
  public ResponseEntity<UserResponseDto> getUserByUuid(UUID userUuid) {
    final User user;
    try {
      user = findUserByUserIdUseCasePort.execute(
          FindUserByUserIdQuery.builder().userId(userUuid).build());
    } catch (ResourceNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    final UserResponseDto userResponseDto = new UserResponseDto();
    userResponseDto.setDate(OffsetDateTime.now(clock));
    userResponseDto.setStatus(HttpStatus.OK.value());
    userResponseDto.setData(userReadDtoMapper.toUserReadDto(user));
    return ResponseEntity.ok(userResponseDto);
  }

}
