package com.architecture.hexagonal.inbound.rest.controller;

import com.architecture.hexagonal.domain.input.query.FindUserByUserIdQuery;
import com.architecture.hexagonal.domain.port.in.CreateUserUseCasePort;
import com.architecture.hexagonal.domain.port.in.FindUserByUserIdUseCasePort;
import com.architecture.hexagonal.domain.port.in.GetAllUsersUseCasePort;
import com.architecture.hexagonal.domain.input.command.CreateUserCommand;
import com.architecture.hexagonal.inbound.rest.dto.UserCreateDto;
import com.architecture.hexagonal.inbound.rest.dto.UserDto;
import com.architecture.hexagonal.inbound.rest.dto.UserResponseDto;
import com.architecture.hexagonal.inbound.rest.dto.UsersResponseDto;
import com.architecture.hexagonal.inbound.rest.mapper.UserReadDtoMapper;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements UsersApi {

  public final GetAllUsersUseCasePort getAllUsersUseCasePort;

  public final CreateUserUseCasePort createUserUseCasePort;

  public final FindUserByUserIdUseCasePort findUserByUserIdUseCasePort;

  public final UserReadDtoMapper userReadDtoMapper;

  public final Clock clock;

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
    final UserResponseDto userResponseDto = new UserResponseDto();
    userResponseDto.setDate(OffsetDateTime.now(clock));
    userResponseDto.setStatus(HttpStatus.OK.value());
    userResponseDto.setData(userReadDtoMapper.toUserReadDto(findUserByUserIdUseCasePort.execute(
        FindUserByUserIdQuery.builder().userId(userUuid).build())));

    return ResponseEntity.ok(userResponseDto);
  }

}
