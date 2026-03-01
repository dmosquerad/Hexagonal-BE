package com.hexagonal.inbound.rest.controller;

import com.architecture.hexagonal.domain.input.command.CreateUserCommand;
import com.architecture.hexagonal.domain.input.query.FindUserByUserIdQuery;
import com.architecture.hexagonal.inbound.rest.controller.UserController;
import com.architecture.hexagonal.domain.data.UserDo;
import com.architecture.hexagonal.domain.port.in.CreateUserUseCasePort;
import com.architecture.hexagonal.domain.port.in.FindUserByUserIdUseCasePort;
import com.architecture.hexagonal.domain.port.in.GetAllUsersUseCasePort;
import com.architecture.hexagonal.inbound.rest.dto.UserDto;
import com.architecture.hexagonal.inbound.rest.dto.UserResponseDto;
import com.architecture.hexagonal.inbound.rest.dto.UsersResponseDto;
import com.architecture.hexagonal.inbound.rest.mapper.UserDtoMapper;
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
  UserDtoMapper userDtoMapper = Mappers.getMapper(UserDtoMapper.class);

  @Spy
  Clock clock = Clock.fixed(InstantStub.INSTANT_DATE,  ZoneOffset.UTC);

  @Test
  void getAllUsers() {
    final UserDto userDto = new UserDto();
    userDto.setUserId(UserDoStub.USERDO.getUserId());
    userDto.setEmail(UserDoStub.USERDO.getEmail());
    userDto.setName(UserDoStub.USERDO.getName());

    final UsersResponseDto userResponseDto = new UsersResponseDto();
    userResponseDto.setData(Set.of(userDto));
    userResponseDto.setDate(OffsetDateTime.ofInstant(InstantStub.INSTANT_DATE, ZoneOffset.UTC));
    userResponseDto.setStatus(HttpStatus.OK.value());

    Mockito.when(getAllUsersUseCasePort.execute()).thenReturn(Collections.singleton(UserDoStub.USERDO));

    final ResponseEntity<UsersResponseDto> responseExpected = ResponseEntity.ok(userResponseDto);

    final ResponseEntity<UsersResponseDto> response = userController.getAllUsers();

    Assertions.assertEquals(responseExpected, response);

    Mockito.verify(getAllUsersUseCasePort).execute();
    Mockito.verify(userDtoMapper).toUserDtoSet(ArgumentMatchers.anySet());
    Mockito.verify(clock).instant();
  }

  @Test
  void createUser() {
    final UserDto userDto = new UserDto();
    userDto.setEmail(UserDoStub.USERDO.getEmail());
    userDto.setName(UserDoStub.USERDO.getName());

    final UserDto userDtoResponse = new UserDto();
    userDtoResponse.setUserId(UserDoStub.USERDO.getUserId());
    userDtoResponse.setEmail(UserDoStub.USERDO.getEmail());
    userDtoResponse.setName(UserDoStub.USERDO.getName());

    final UserResponseDto userResponseDto = new UserResponseDto();
    userResponseDto.setData(userDtoResponse);
    userResponseDto.setDate(OffsetDateTime.ofInstant(InstantStub.INSTANT_DATE, ZoneOffset.UTC));
    userResponseDto.setStatus(HttpStatus.OK.value());

    Mockito.when(createUserUseCasePort.execute(ArgumentMatchers.any())).thenReturn(UserDoStub.USERDO);

    final ResponseEntity<UserResponseDto> responseExpected = ResponseEntity.ok(userResponseDto);

    final ResponseEntity<UserResponseDto> response = userController.createUser(userDto);

    Assertions.assertEquals(responseExpected, response);

    Mockito.verify(createUserUseCasePort).execute(ArgumentMatchers.any(CreateUserCommand.class));
    Mockito.verify(userDtoMapper).toUserDto(ArgumentMatchers.any(UserDo.class));
    Mockito.verify(clock).instant();
  }

  @Test
  void getUserByUuid() {
    final UserDto userDto = new UserDto();
    userDto.setUserId(UserDoStub.USERDO.getUserId());
    userDto.setEmail(UserDoStub.USERDO.getEmail());
    userDto.setName(UserDoStub.USERDO.getName());

    final UserResponseDto userResponseDto = new UserResponseDto();
    userResponseDto.setData(userDto);
    userResponseDto.setDate(OffsetDateTime.ofInstant(InstantStub.INSTANT_DATE, ZoneOffset.UTC));
    userResponseDto.setStatus(HttpStatus.OK.value());

    Mockito.when(findUserByUserIdUseCasePort.execute(ArgumentMatchers.any(FindUserByUserIdQuery.class))).thenReturn(
        UserDoStub.USERDO);

    final ResponseEntity<UserResponseDto> responseExpected = ResponseEntity.ok(userResponseDto);

    final ResponseEntity<UserResponseDto> response = userController.getUserByUuid(UserDoStub.USERDO.getUserId());

    Assertions.assertEquals(responseExpected, response);

    Mockito.verify(findUserByUserIdUseCasePort).execute(ArgumentMatchers.any(FindUserByUserIdQuery.class));
    Mockito.verify(userDtoMapper).toUserDto(ArgumentMatchers.any(UserDo.class));
    Mockito.verify(clock).instant();
  }

}
