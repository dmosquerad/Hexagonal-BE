package com.architecture.hexagonal.inbound.rest.controller;

import com.architecture.hexagonal.domain.input.command.CreateUserCommand;
import com.architecture.hexagonal.domain.input.query.FindUserByUserIdQuery;
import com.architecture.hexagonal.domain.data.UserDo;
import com.architecture.hexagonal.domain.port.in.CreateUserUseCasePort;
import com.architecture.hexagonal.domain.port.in.FindUserByUserIdUseCasePort;
import com.architecture.hexagonal.domain.port.in.GetAllUsersUseCasePort;
import com.architecture.hexagonal.inbound.rest.dto.UserResponseDto;
import com.architecture.hexagonal.inbound.rest.dto.UsersResponseDto;
import com.architecture.hexagonal.inbound.rest.mapper.UserReadDtoMapper;
import com.architecture.hexagonal.inbound.rest.testutils.time.TestClock;
import com.architecture.hexagonal.inbound.rest.testutils.data.dto.UserCreateDtoTestDataBuilder;
import com.architecture.hexagonal.inbound.rest.testutils.data.dto.UserResponseDtoTestDataBuilder;
import com.architecture.hexagonal.inbound.rest.testutils.data.dto.UsersResponseDtoTestDataBuilder;
import com.architecture.hexagonal.inbound.rest.testutils.data.entity.UserDoTestDataBuilder;
import java.time.Clock;
import java.util.Collections;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
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
  Clock clock = TestClock.FIXED_CLOCK;

  @Test
  void getAllUsers() {
    Mockito.when(getAllUsersUseCasePort.execute())
        .thenReturn(Collections.singleton(UserDoTestDataBuilder
            .builder()
            .build()
            .userDo()));

    final ResponseEntity<UsersResponseDto> responseExpected = ResponseEntity.ok(
        UsersResponseDtoTestDataBuilder
            .builder()
            .build()
            .usersResponseDto());

    final ResponseEntity<UsersResponseDto> response = userController.getAllUsers();

    AssertionsForClassTypes.assertThat(response)
        .usingRecursiveComparison()
        .isEqualTo(responseExpected);

    Mockito.verify(getAllUsersUseCasePort).execute();
    Mockito.verify(userReadDtoMapper).toUserReadDtoSet(ArgumentMatchers.anySet());
    Mockito.verify(clock).instant();
  }

  @Test
  void createUser() {
    Mockito.when(createUserUseCasePort.execute(ArgumentMatchers.any()))
        .thenReturn(UserDoTestDataBuilder
            .builder()
            .build()
            .userDo());

    final ResponseEntity<UserResponseDto> responseExpected = ResponseEntity.ok(
        UserResponseDtoTestDataBuilder
            .builder()
            .build()
            .userResponseDto());

    final ResponseEntity<UserResponseDto> response = userController.createUser(
        UserCreateDtoTestDataBuilder
            .builder()
            .build()
            .userCreateDto());

    AssertionsForClassTypes.assertThat(response)
        .usingRecursiveComparison()
        .isEqualTo(responseExpected);

    Mockito.verify(createUserUseCasePort).execute(ArgumentMatchers.any(CreateUserCommand.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(ArgumentMatchers.any(UserDo.class));
    Mockito.verify(clock).instant();
  }

  @Test
  void getUserByUuid() {
    final UserDo userDo = UserDoTestDataBuilder
        .builder()
        .build()
        .userDo();

    Mockito.when(findUserByUserIdUseCasePort.execute(ArgumentMatchers.any(FindUserByUserIdQuery.class))).thenReturn(userDo);

    final ResponseEntity<UserResponseDto> responseExpected = ResponseEntity.ok(UserResponseDtoTestDataBuilder
        .builder()
        .build()
        .userResponseDto());

    final ResponseEntity<UserResponseDto> response = userController.getUserByUuid(userDo.getUserId());

    AssertionsForClassTypes.assertThat(response)
        .usingRecursiveComparison()
        .isEqualTo(responseExpected);

    Mockito.verify(findUserByUserIdUseCasePort).execute(ArgumentMatchers.any(FindUserByUserIdQuery.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(ArgumentMatchers.any(UserDo.class));
    Mockito.verify(clock).instant();
  }

}