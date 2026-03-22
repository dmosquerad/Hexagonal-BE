package com.architecture.hexagonal.infrastructure.inbound.rest.controller;

import com.architecture.hexagonal.domain.data.entity.User;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.input.command.CreateUserCommand;
import com.architecture.hexagonal.domain.input.command.DeleteUserCommand;
import com.architecture.hexagonal.domain.input.command.PatchUserCommand;
import com.architecture.hexagonal.domain.input.command.UpdateUserCommand;
import com.architecture.hexagonal.domain.input.query.FindUserByUserIdQuery;
import com.architecture.hexagonal.domain.input.query.UserExistsQuery;
import com.architecture.hexagonal.domain.port.in.CreateUserUseCasePort;
import com.architecture.hexagonal.domain.port.in.DeleteUserUseCasePort;
import com.architecture.hexagonal.domain.port.in.FindUserByUserIdUseCasePort;
import com.architecture.hexagonal.domain.port.in.GetAllUsersUseCasePort;
import com.architecture.hexagonal.domain.port.in.PatchUserUseCasePort;
import com.architecture.hexagonal.domain.port.in.UpdateUserUseCasePort;
import com.architecture.hexagonal.domain.port.in.UserExistsUseCasePort;
import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UserResponseDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UsersResponseDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.UserReadDtoMapper;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.dto.UserReadDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.time.TestClock;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.dto.UserCreateDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.dto.UserPatchDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.dto.UserResponseDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.dto.UserUpdateDtoTestDataBuilder;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponseException;

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

  @Mock
  UpdateUserUseCasePort updateUserUseCasePort;

  @Mock
  PatchUserUseCasePort patchUserUseCasePort;

  @Mock
  UserExistsUseCasePort userExistsUseCasePort;

  @Spy
  UserReadDtoMapper userReadDtoMapper = Mappers.getMapper(UserReadDtoMapper.class);

  @Spy
  Clock clock = TestClock.FIXED_CLOCK;

  @Test
  void getAllUsers() {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();
    
    Mockito.when(getAllUsersUseCasePort.execute())
        .thenReturn(Collections.singleton(user));

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
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void createUser() {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();
    final var createUserDto = UserCreateDtoTestDataBuilder
        .builder()
        .build()
        .userCreateDto();

    Mockito.when(createUserUseCasePort.execute(ArgumentMatchers.any(CreateUserCommand.class)))
        .thenReturn(user);

    final ResponseEntity<UserResponseDto> responseExpected = ResponseEntity.ok(
        UserResponseDtoTestDataBuilder
            .builder()
            .build()
            .userResponseDto());

    final ResponseEntity<UserResponseDto> response = userController.createUser(createUserDto);

    AssertionsForClassTypes.assertThat(response)
        .usingRecursiveComparison()
        .isEqualTo(responseExpected);

    Mockito.verify(createUserUseCasePort).execute(ArgumentMatchers.any(CreateUserCommand.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void getUserByUuid() throws ResourceNotFoundException {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();

    Mockito
        .when(findUserByUserIdUseCasePort.execute(ArgumentMatchers.any(FindUserByUserIdQuery.class)))
        .thenReturn(user);

    final ResponseEntity<UserResponseDto> responseExpected = ResponseEntity.ok(
        UserResponseDtoTestDataBuilder
            .builder()
            .build()
            .userResponseDto());

    final ResponseEntity<UserResponseDto> response = userController.getUserByUuid(user.getUserId());

    AssertionsForClassTypes.assertThat(response)
        .usingRecursiveComparison()
        .isEqualTo(responseExpected);

    Mockito
        .verify(findUserByUserIdUseCasePort).execute(ArgumentMatchers.any(FindUserByUserIdQuery.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
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

    final ResponseEntity<UserResponseDto> responseExpected = ResponseEntity.ok(
        UserResponseDtoTestDataBuilder
            .builder()
            .build()
            .userResponseDto());

    final ResponseEntity<UserResponseDto> response = userController.deleteUserByUuid(
        user.getUserId());

    AssertionsForClassTypes.assertThat(response)
        .usingRecursiveComparison()
        .isEqualTo(responseExpected);

    Mockito.verify(deleteUserUseCasePort).execute(ArgumentMatchers.any(DeleteUserCommand.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void updateUserByUuid() throws ResourceNotFoundException {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();

    Mockito.when(updateUserUseCasePort.execute(ArgumentMatchers.any(UpdateUserCommand.class)))
        .thenReturn(user);

    final ResponseEntity<UserResponseDto> responseExpected = ResponseEntity.ok(
        UserResponseDtoTestDataBuilder
            .builder()
            .build()
            .userResponseDto());

    final ResponseEntity<UserResponseDto> response = userController.updateUserByUuid(
        user.getUserId(),
        UserUpdateDtoTestDataBuilder
            .builder()
            .build()
            .userUpdateDto());

    AssertionsForClassTypes.assertThat(response)
        .usingRecursiveComparison()
        .isEqualTo(responseExpected);

    Mockito.verify(updateUserUseCasePort).execute(ArgumentMatchers.any(UpdateUserCommand.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void updateUserByUuidUserNotFound() throws ResourceNotFoundException {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();
    final String errorMessage = HttpStatus.NOT_FOUND.getReasonPhrase();

    Mockito.when(updateUserUseCasePort.execute(ArgumentMatchers.any(UpdateUserCommand.class)))
        .thenThrow(new ResourceNotFoundException(errorMessage));

    final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND.value());
    problemDetail.setDetail(errorMessage);
    final ErrorResponseException errorException = new ErrorResponseException(
        HttpStatus.NOT_FOUND,
        problemDetail,
        null);

    AssertionsForClassTypes.assertThatThrownBy(() ->
        userController.updateUserByUuid(
            user.getUserId(),
            UserUpdateDtoTestDataBuilder
               .builder()
               .build()
               .userUpdateDto()))
        .isInstanceOf(ErrorResponseException.class)
        .usingRecursiveComparison()
        .isEqualTo(errorException);

    Mockito.verify(updateUserUseCasePort).execute(ArgumentMatchers.any(UpdateUserCommand.class));
  }

  @Test
  void patchUserByUuid() throws ResourceNotFoundException {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();

    Mockito.when(patchUserUseCasePort.execute(ArgumentMatchers.any(PatchUserCommand.class)))
        .thenReturn(user);

    final ResponseEntity<UserResponseDto> responseExpected = ResponseEntity.ok(
        UserResponseDtoTestDataBuilder
            .builder()
            .userReadDto(
                UserReadDtoTestDataBuilder
                    .builder()
                    .build()
                    .userReadDto())
            .build()
            .userResponseDto());

    final ResponseEntity<UserResponseDto> response = userController.patchUserByUuid(
        user.getUserId(),
        UserPatchDtoTestDataBuilder
            .builder()
            .build()
            .userPatchDto());

    AssertionsForClassTypes.assertThat(response)
        .usingRecursiveComparison()
        .isEqualTo(responseExpected);

    Mockito.verify(patchUserUseCasePort).execute(ArgumentMatchers.any(PatchUserCommand.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void patchUserByUuidUserNotFound() throws ResourceNotFoundException {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();
    final String errorMessage = HttpStatus.NOT_FOUND.getReasonPhrase();

    Mockito.when(patchUserUseCasePort.execute(ArgumentMatchers.any(PatchUserCommand.class)))
        .thenThrow(new ResourceNotFoundException(errorMessage));

    final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND.value());
    problemDetail.setDetail(errorMessage);
    final ErrorResponseException errorException = new ErrorResponseException(
        HttpStatus.NOT_FOUND,
        problemDetail,
        null);

    AssertionsForClassTypes.assertThatThrownBy(() ->
        userController.patchUserByUuid(
            user.getUserId(),
            UserPatchDtoTestDataBuilder
                .builder()
                .build()
                .userPatchDto()))
        .isInstanceOf(ErrorResponseException.class)
        .usingRecursiveComparison()
        .isEqualTo(errorException);

    Mockito.verify(patchUserUseCasePort).execute(ArgumentMatchers.any(PatchUserCommand.class));
  }

  @Test
  void headUserByUuid() throws ResourceNotFoundException {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();

    final ResponseEntity<Void> responseExpected = ResponseEntity.ok().build();

    Mockito.doNothing()
        .when(userExistsUseCasePort)
        .execute(ArgumentMatchers.any(UserExistsQuery.class));

    final ResponseEntity<Void> response = userController.headUserByUuid(user.getUserId());

    AssertionsForClassTypes.assertThat(response)
        .usingRecursiveComparison()
        .isEqualTo(responseExpected);

    Mockito.verify(userExistsUseCasePort).execute(ArgumentMatchers.any(UserExistsQuery.class));
  }

  @Test
  void headUserByUuidUserNotFound() throws ResourceNotFoundException {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();
    final String errorMessage = HttpStatus.NOT_FOUND.getReasonPhrase();

    Mockito.doThrow(new ResourceNotFoundException(errorMessage))
        .when(userExistsUseCasePort).execute(ArgumentMatchers.any(UserExistsQuery.class));

    final ErrorResponseException errorException = new ErrorResponseException(
        HttpStatus.NOT_FOUND,
        null);

    AssertionsForClassTypes.assertThatThrownBy(() ->
        userController.headUserByUuid(user.getUserId()))
        .isInstanceOf(ErrorResponseException.class)
        .usingRecursiveComparison()
        .isEqualTo(errorException);

    Mockito.verify(userExistsUseCasePort).execute(ArgumentMatchers.any(UserExistsQuery.class));
  }

}