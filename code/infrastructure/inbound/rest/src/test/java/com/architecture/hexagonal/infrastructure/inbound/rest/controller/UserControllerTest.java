package com.architecture.hexagonal.infrastructure.inbound.rest.controller;

import com.architecture.hexagonal.application.cqrs.command.request.CreateUserCommand;
import com.architecture.hexagonal.application.cqrs.command.request.DeleteUserCommand;
import com.architecture.hexagonal.application.cqrs.command.request.PatchUserCommand;
import com.architecture.hexagonal.application.cqrs.command.request.UpdateUserCommand;
import com.architecture.hexagonal.application.cqrs.command.dispatcher.CommandBus;
import com.architecture.hexagonal.application.cqrs.query.request.FindUserByUserIdQuery;
import com.architecture.hexagonal.application.cqrs.query.request.GetAllUserQuery;
import com.architecture.hexagonal.application.cqrs.query.request.UserExistsQuery;
import com.architecture.hexagonal.application.cqrs.query.dispatcher.QueryBus;
import com.architecture.hexagonal.application.cqrs.query.request.pagination.PaginationResult;
import com.architecture.hexagonal.domain.model.entity.User;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.InvalidValueException;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.infrastructure.contract.rest.user.server.dto.ResponsePaginationDto;
import com.architecture.hexagonal.infrastructure.contract.rest.user.server.dto.UserCreateDto;
import com.architecture.hexagonal.infrastructure.contract.rest.user.server.dto.UserResponseDto;
import com.architecture.hexagonal.infrastructure.contract.rest.user.server.dto.UsersResponseDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.CreateUserCommandMapper;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.DeleteUserCommandMapper;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.FindUserByUserIdQueryMapper;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.GetAllUserQueryMapper;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.PatchUserCommandMapper;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.UpdateUserCommandMapper;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.UserExistsQueryMapper;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.UserReadDtoMapper;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.dto.UserCreateDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.dto.UserPatchDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.dto.UserReadDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.dto.UserResponseDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.dto.UserUpdateDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.dto.UsersResponseDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.entity.UserTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.pagination.PaginationTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.time.TestClock;
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
  QueryBus queryBus;

  @Mock
  CommandBus commandBus;

  @Spy
  UserReadDtoMapper userReadDtoMapper = Mappers.getMapper(UserReadDtoMapper.class);

  @Spy
  CreateUserCommandMapper createUserCommandMapper =
      Mappers.getMapper(CreateUserCommandMapper.class);

  @Spy
  DeleteUserCommandMapper deleteUserCommandMapper =
      Mappers.getMapper(DeleteUserCommandMapper.class);

  @Spy
  UpdateUserCommandMapper updateUserCommandMapper =
      Mappers.getMapper(UpdateUserCommandMapper.class);

  @Spy
  PatchUserCommandMapper patchUserCommandMapper =
      Mappers.getMapper(PatchUserCommandMapper.class);

  @Spy
  FindUserByUserIdQueryMapper findUserByUserIdQueryMapper =
      Mappers.getMapper(FindUserByUserIdQueryMapper.class);

  @Spy
  UserExistsQueryMapper userExistsQueryMapper =
      Mappers.getMapper(UserExistsQueryMapper.class);

  @Spy
  GetAllUserQueryMapper getAllUserQueryMapper =
      Mappers.getMapper(GetAllUserQueryMapper.class);

  @Spy
  Clock clock = TestClock.FIXED_CLOCK;

  @Test
  void getAllUsers_shouldReturnOk_whenUsersExist() throws Exception {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();

    final String host = "";
    final Boolean blockEmail = false;
    final Integer page = 0;
    final Integer size = 100;
    final int totalPages = 1;
    final long totalElements = 1L;

    Mockito.when(queryBus.execute(ArgumentMatchers.any(GetAllUserQuery.class)))
        .thenReturn(PaginationResult.<User>builder()
            .data(Collections.singleton(user))
            .page(page)
            .size(size)
            .totalElements(totalElements)
            .totalPages(totalPages)
            .build());

    final UsersResponseDto expectedBody = UsersResponseDtoTestDataBuilder
        .builder()
        .build()
        .usersResponseDto();
    expectedBody.setPagination(new ResponsePaginationDto()
        .page(page)
        .size(size)
        .totalElements(totalElements)
        .totalPages(totalPages));
    final ResponseEntity<UsersResponseDto> responseExpected = ResponseEntity.ok(expectedBody);

    final ResponseEntity<UsersResponseDto> response = userController.getAllUsers(
        host,
        blockEmail,
        page,
        size);

    AssertionsForClassTypes.assertThat(response)
        .usingRecursiveComparison()
        .isEqualTo(responseExpected);

    Mockito.verify(getAllUserQueryMapper).toGetAllUserQuery(host, blockEmail, PaginationTestDataBuilder
        .builder()
        .page(page)
        .size(size)
        .build()
        .pagination());
    Mockito.verify(queryBus).execute(ArgumentMatchers.any(GetAllUserQuery.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void createUser_shouldReturnOk_whenRequestIsValid() throws Exception {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();
    final UserCreateDto createUserDto = UserCreateDtoTestDataBuilder
        .builder()
        .build()
        .userCreateDto();

    Mockito.when(commandBus.execute(ArgumentMatchers.any(CreateUserCommand.class)))
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

    Mockito.verify(createUserCommandMapper).toCreateUserCommand(createUserDto);
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(CreateUserCommand.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void getUserByUuid_shouldReturnOk_whenUserExists() throws Exception {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();

    Mockito
        .when(queryBus.execute(ArgumentMatchers.any(FindUserByUserIdQuery.class)))
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

    Mockito.verify(findUserByUserIdQueryMapper).toFindUserByUserIdQuery(user.getUserId());
    Mockito
        .verify(queryBus).execute(ArgumentMatchers.any(FindUserByUserIdQuery.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void deleteUserByUuid_shouldReturnOk_whenUserExists() throws Exception {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();

    Mockito.when(commandBus.execute(ArgumentMatchers.any(DeleteUserCommand.class)))
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

    Mockito.verify(deleteUserCommandMapper).toDeleteUserCommand(user.getUserId());
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(DeleteUserCommand.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void updateUserByUuid_shouldReturnOk_whenRequestIsValid() throws Exception {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();

    Mockito.when(commandBus.execute(ArgumentMatchers.any(UpdateUserCommand.class)))
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

    Mockito.verify(updateUserCommandMapper)
        .toUpdateUserCommand(ArgumentMatchers.eq(user.getUserId()), ArgumentMatchers.any());
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(UpdateUserCommand.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void updateUserByUuid_shouldThrowErrorResponseException_whenUserNotFound() throws Exception {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();
    final String errorMessage = HttpStatus.NOT_FOUND.getReasonPhrase();

    Mockito.when(commandBus.execute(ArgumentMatchers.any(UpdateUserCommand.class)))
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

    Mockito.verify(updateUserCommandMapper)
        .toUpdateUserCommand(ArgumentMatchers.eq(user.getUserId()), ArgumentMatchers.any());
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(UpdateUserCommand.class));
  }

  @Test
  void patchUserByUuid_shouldReturnOk_whenRequestIsValid() throws Exception {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();

    Mockito.when(commandBus.execute(ArgumentMatchers.any(PatchUserCommand.class)))
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

    Mockito.verify(patchUserCommandMapper)
        .toPatchUserCommand(ArgumentMatchers.eq(user.getUserId()), ArgumentMatchers.any());
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(PatchUserCommand.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void patchUserByUuid_shouldThrowErrorResponseException_whenUserNotFound() throws Exception {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();
    final String errorMessage = HttpStatus.NOT_FOUND.getReasonPhrase();

    Mockito.when(commandBus.execute(ArgumentMatchers.any(PatchUserCommand.class)))
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

    Mockito.verify(patchUserCommandMapper)
        .toPatchUserCommand(ArgumentMatchers.eq(user.getUserId()), ArgumentMatchers.any());
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(PatchUserCommand.class));
  }

  @Test
  void headUserByUuid_shouldReturnOk_whenUserExists() throws Exception {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();

    final ResponseEntity<Void> responseExpected = ResponseEntity.ok().build();

    Mockito.when(queryBus.execute(ArgumentMatchers.any(UserExistsQuery.class)))
        .thenReturn(null);

    final ResponseEntity<Void> response = userController.headUserByUuid(user.getUserId());

    AssertionsForClassTypes.assertThat(response)
        .usingRecursiveComparison()
        .isEqualTo(responseExpected);

    Mockito.verify(userExistsQueryMapper).toUserExistsQuery(user.getUserId());
    Mockito.verify(queryBus).execute(ArgumentMatchers.any(UserExistsQuery.class));
  }

  @Test
  void headUserByUuid_shouldThrowErrorResponseException_whenUserNotFound() throws Exception {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();
    final String errorMessage = HttpStatus.NOT_FOUND.getReasonPhrase();

    Mockito.when(queryBus.execute(ArgumentMatchers.any(UserExistsQuery.class)))
        .thenThrow(new ResourceNotFoundException(errorMessage));

    final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND.value());
    problemDetail.setDetail(errorMessage);
    final ErrorResponseException errorException = new ErrorResponseException(
        HttpStatus.NOT_FOUND,
        problemDetail,
        null);

    AssertionsForClassTypes.assertThatThrownBy(() ->
        userController.headUserByUuid(user.getUserId()))
        .isInstanceOf(ErrorResponseException.class)
        .usingRecursiveComparison()
        .isEqualTo(errorException);

    Mockito.verify(userExistsQueryMapper).toUserExistsQuery(user.getUserId());
    Mockito.verify(queryBus).execute(ArgumentMatchers.any(UserExistsQuery.class));
  }

  @Test
  void getAllUsers_shouldReturnOk_withCustomPagination() throws Exception {
    final User user = UserTestDataBuilder.builder().build().user();
    final Integer page = 1;
    final Integer size = 10;
    final int totalPages = 5;
    final long totalElements = 50L;

    Mockito.when(queryBus.execute(ArgumentMatchers.any(GetAllUserQuery.class)))
        .thenReturn(PaginationResult.<User>builder()
            .data(Collections.singleton(user))
            .page(page)
            .size(size)
            .totalElements(totalElements)
            .totalPages(totalPages)
            .build());

    final UsersResponseDto expectedBody = UsersResponseDtoTestDataBuilder.builder().build().usersResponseDto();
    expectedBody.setPagination(new ResponsePaginationDto()
        .page(page)
        .size(size)
        .totalElements(totalElements)
        .totalPages(totalPages));
    final ResponseEntity<UsersResponseDto> responseExpected = ResponseEntity.ok(expectedBody);

    final ResponseEntity<UsersResponseDto> response = userController.getAllUsers(null, null, page, size);

    AssertionsForClassTypes.assertThat(response)
        .usingRecursiveComparison()
        .isEqualTo(responseExpected);

    Mockito.verify(getAllUserQueryMapper).toGetAllUserQuery(null, null, PaginationTestDataBuilder.builder().page(page).size(size).build().pagination());
    Mockito.verify(queryBus).execute(ArgumentMatchers.any(GetAllUserQuery.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void getAllUsers_shouldThrowErrorResponseException_whenDomainExceptionOccurs() throws Exception {
    final String errorMessage = ExceptionMessage.EMAIL_NO_ALLOWED_MESSAGE + "blocked@banned.com";

    Mockito.when(queryBus.execute(ArgumentMatchers.any(GetAllUserQuery.class)))
        .thenThrow(new InvalidValueException(errorMessage));

    final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
    problemDetail.setDetail(errorMessage);
    final ErrorResponseException errorException = new ErrorResponseException(
        HttpStatus.UNPROCESSABLE_ENTITY, problemDetail, null);

    AssertionsForClassTypes.assertThatThrownBy(() ->
        userController.getAllUsers(null, null, 0, 100))
        .isInstanceOf(ErrorResponseException.class)
        .usingRecursiveComparison()
        .isEqualTo(errorException);

    Mockito.verify(queryBus).execute(ArgumentMatchers.any(GetAllUserQuery.class));
  }

  @Test
  void getAllUsers_shouldThrowErrorResponseException_whenUnexpectedExceptionOccurs() throws Exception {
    final String errorMessage = "Unexpected error";

    Mockito.when(queryBus.execute(ArgumentMatchers.any(GetAllUserQuery.class)))
        .thenThrow(new RuntimeException(errorMessage));

    final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    problemDetail.setDetail(errorMessage);
    final ErrorResponseException errorException = new ErrorResponseException(
        HttpStatus.INTERNAL_SERVER_ERROR, problemDetail, null);

    AssertionsForClassTypes.assertThatThrownBy(() ->
        userController.getAllUsers(null, null, 0, 100))
        .isInstanceOf(ErrorResponseException.class)
        .usingRecursiveComparison()
        .isEqualTo(errorException);

    Mockito.verify(queryBus).execute(ArgumentMatchers.any(GetAllUserQuery.class));
  }

}