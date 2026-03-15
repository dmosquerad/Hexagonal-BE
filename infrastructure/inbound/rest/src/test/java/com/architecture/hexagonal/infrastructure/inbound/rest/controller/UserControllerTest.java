package com.architecture.hexagonal.infrastructure.inbound.rest.controller;

import com.architecture.hexagonal.domain.data.User;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.input.command.CreateUserCommand;
import com.architecture.hexagonal.domain.input.command.DeleteUserCommand;
import com.architecture.hexagonal.domain.input.query.FindUserByUserIdQuery;
import com.architecture.hexagonal.domain.port.in.CreateUserUseCasePort;
import com.architecture.hexagonal.domain.port.in.DeleteUserUseCasePort;
import com.architecture.hexagonal.domain.port.in.FindUserByUserIdUseCasePort;
import com.architecture.hexagonal.domain.port.in.GetAllUsersUseCasePort;
import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UserResponseDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UsersResponseDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.UserReadDtoMapper;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.time.TestClock;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.dto.UserCreateDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.dto.UserResponseDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.dto.UsersResponseDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.entity.UserTestDataBuilder;
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

  @Mock
  DeleteUserUseCasePort deleteUserUseCasePort;

  @Spy
  UserReadDtoMapper userReadDtoMapper = Mappers.getMapper(UserReadDtoMapper.class);

  @Spy
  Clock clock = TestClock.FIXED_CLOCK;

  @Test
  void getAllUsers() {
    Mockito.when(getAllUsersUseCasePort.execute())
        .thenReturn(Collections.singleton(UserTestDataBuilder
            .builder()
            .build()
            .user()));

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
    Mockito.verify(userReadDtoMapper).toUserReadDto(ArgumentMatchers.any(User.class));
    Mockito.verify(clock).instant();
  }

  @Test
  void createUser() {
    Mockito.when(createUserUseCasePort.execute(ArgumentMatchers.any()))
        .thenReturn(UserTestDataBuilder
            .builder()
            .build()
            .user());

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
    Mockito.verify(userReadDtoMapper).toUserReadDto(ArgumentMatchers.any(User.class));
    Mockito.verify(clock).instant();
  }

  @Test
  void getUserByUuid() throws ResourceNotFoundException {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();

    Mockito.when(findUserByUserIdUseCasePort.execute(ArgumentMatchers.any(FindUserByUserIdQuery.class))).thenReturn(
        user);

    final ResponseEntity<UserResponseDto> responseExpected = ResponseEntity.ok(UserResponseDtoTestDataBuilder
        .builder()
        .build()
        .userResponseDto());

    final ResponseEntity<UserResponseDto> response = userController.getUserByUuid(user.getUserId());

    AssertionsForClassTypes.assertThat(response)
        .usingRecursiveComparison()
        .isEqualTo(responseExpected);

    Mockito.verify(findUserByUserIdUseCasePort).execute(ArgumentMatchers.any(FindUserByUserIdQuery.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(ArgumentMatchers.any(User.class));
    Mockito.verify(clock).instant();
  }

  @Test
  void deleteUserByUuid() throws ResourceNotFoundException {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();

    Mockito.when(deleteUserUseCasePort.execute(ArgumentMatchers.any(DeleteUserCommand.class)))
        .thenReturn(user);

    final ResponseEntity<UserResponseDto> responseExpected = ResponseEntity.ok(UserResponseDtoTestDataBuilder
        .builder()
        .build()
        .userResponseDto());

    final ResponseEntity<UserResponseDto> response = userController.deleteUserByUuid(user.getUserId());

    AssertionsForClassTypes.assertThat(response)
        .usingRecursiveComparison()
        .isEqualTo(responseExpected);

    Mockito.verify(deleteUserUseCasePort).execute(ArgumentMatchers.any(DeleteUserCommand.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(ArgumentMatchers.any(User.class));
    Mockito.verify(clock).instant();
  }

}