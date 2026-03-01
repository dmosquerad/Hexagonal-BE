package com.hexagonal.inbound.rest.controller;

import com.architecture.hexagonal.domain.input.command.CreateUserCommand;
import com.architecture.hexagonal.domain.input.query.FindUserByUserIdQuery;
import com.architecture.hexagonal.inbound.rest.controller.UserController;
import com.architecture.hexagonal.domain.data.UserDo;
import com.architecture.hexagonal.domain.port.in.CreateUserUseCasePort;
import com.architecture.hexagonal.domain.port.in.FindUserByUserIdUseCasePort;
import com.architecture.hexagonal.domain.port.in.GetAllUsersUseCasePort;
import com.architecture.hexagonal.inbound.rest.dto.UserCreateDto;
import com.architecture.hexagonal.inbound.rest.dto.UserDto;
import com.architecture.hexagonal.inbound.rest.dto.UserReadDto;
import com.architecture.hexagonal.inbound.rest.dto.UserResponseDto;
import com.architecture.hexagonal.inbound.rest.dto.UsersResponseDto;
import com.architecture.hexagonal.inbound.rest.mapper.UserReadDtoMapper;
import com.hexagonal.stub.InstantStub;
import com.hexagonal.stub.UserDoStub;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

  @InjectMocks
  UserController userController;

  @Mock
  GetAllUsersUseCasePort getAllUsersUseCasePort;

  @Mock
  CreateUserUseCasePort createUserUseCasePort;

  @Mock
  FindUserByUserIdUseCasePort findUserByUserIdUseCasePort;

  @Spy
  UserReadDtoMapper userReadDtoMapper = Mappers.getMapper(UserReadDtoMapper.class);

  @Spy
  Clock clock = Clock.fixed(InstantStub.INSTANT_DATE,  ZoneOffset.UTC);

  @Test
  void getAllUsers() {
    final UserReadDto userReadDto = new UserReadDto();
    userReadDto.setUserId(UserDoStub.USERDO.getUserId());
    userReadDto.setEmail(UserDoStub.USERDO.getEmail());
    userReadDto.setName(UserDoStub.USERDO.getName());

    final UsersResponseDto userResponseDto = new UsersResponseDto();
    userResponseDto.setData(Set.of(userReadDto));
    userResponseDto.setDate(OffsetDateTime.ofInstant(InstantStub.INSTANT_DATE, ZoneOffset.UTC));
    userResponseDto.setStatus(HttpStatus.OK.value());

    Mockito.when(getAllUsersUseCasePort.execute()).thenReturn(Collections.singleton(UserDoStub.USERDO));

    final ResponseEntity<UsersResponseDto> responseExpected = ResponseEntity.ok(userResponseDto);

    final ResponseEntity<UsersResponseDto> response = userController.getAllUsers();

    Assertions.assertEquals(responseExpected, response);

    Mockito.verify(getAllUsersUseCasePort).execute();
    Mockito.verify(userReadDtoMapper).toUserReadDtoSet(ArgumentMatchers.anySet());
    Mockito.verify(clock).instant();
  }

  @Test
  void createUser() {
    final UserCreateDto userCreateDto = new UserCreateDto();
    userCreateDto.setEmail(UserDoStub.USERDO.getEmail());
    userCreateDto.setName(UserDoStub.USERDO.getName());

    final UserReadDto userDtoResponse = new UserReadDto();
    userDtoResponse.setUserId(UserDoStub.USERDO.getUserId());
    userDtoResponse.setEmail(UserDoStub.USERDO.getEmail());
    userDtoResponse.setName(UserDoStub.USERDO.getName());

    final UserResponseDto userResponseDto = new UserResponseDto();
    userResponseDto.setData(userDtoResponse);
    userResponseDto.setDate(OffsetDateTime.ofInstant(InstantStub.INSTANT_DATE, ZoneOffset.UTC));
    userResponseDto.setStatus(HttpStatus.OK.value());

    Mockito.when(createUserUseCasePort.execute(ArgumentMatchers.any())).thenReturn(UserDoStub.USERDO);

    final ResponseEntity<UserResponseDto> responseExpected = ResponseEntity.ok(userResponseDto);

    final ResponseEntity<UserResponseDto> response = userController.createUser(userCreateDto);

    Assertions.assertEquals(responseExpected, response);

    Mockito.verify(createUserUseCasePort).execute(ArgumentMatchers.any(CreateUserCommand.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(ArgumentMatchers.any(UserDo.class));
    Mockito.verify(clock).instant();
  }

  @Test
  void getUserByUuid() {
    final UserReadDto userReadDto = new UserReadDto();
    userReadDto.setUserId(UserDoStub.USERDO.getUserId());
    userReadDto.setEmail(UserDoStub.USERDO.getEmail());
    userReadDto.setName(UserDoStub.USERDO.getName());

    final UserResponseDto userResponseDto = new UserResponseDto();
    userResponseDto.setData(userReadDto);
    userResponseDto.setDate(OffsetDateTime.ofInstant(InstantStub.INSTANT_DATE, ZoneOffset.UTC));
    userResponseDto.setStatus(HttpStatus.OK.value());

    Mockito.when(findUserByUserIdUseCasePort.execute(ArgumentMatchers.any(FindUserByUserIdQuery.class))).thenReturn(
        UserDoStub.USERDO);

    final ResponseEntity<UserResponseDto> responseExpected = ResponseEntity.ok(userResponseDto);

    final ResponseEntity<UserResponseDto> response = userController.getUserByUuid(UserDoStub.USERDO.getUserId());

    Assertions.assertEquals(responseExpected, response);

    Mockito.verify(findUserByUserIdUseCasePort).execute(ArgumentMatchers.any(FindUserByUserIdQuery.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(ArgumentMatchers.any(UserDo.class));
    Mockito.verify(clock).instant();
  }

}
