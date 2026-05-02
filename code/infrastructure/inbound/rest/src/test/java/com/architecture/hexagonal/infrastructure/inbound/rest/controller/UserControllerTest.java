package com.architecture.hexagonal.infrastructure.inbound.rest.controller;

import com.architecture.hexagonal.domain.exception.DomainException;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.InvalidValueException;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.domain.model.pagination.PaginationResult;
import com.architecture.hexagonal.infrastructure.contract.cqrs.generated.user.*;
import com.architecture.hexagonal.infrastructure.contract.rest.user.server.dto.ResponsePaginationDto;
import com.architecture.hexagonal.infrastructure.contract.rest.user.server.dto.UserCreateDto;
import com.architecture.hexagonal.infrastructure.contract.rest.user.server.dto.UserResponseDto;
import com.architecture.hexagonal.infrastructure.contract.rest.user.server.dto.UsersResponseDto;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.dispatcher.command.CommandBus;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.dispatcher.query.QueryBus;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.user.*;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.user.FindUserByUserIdQueryDtoMapper;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.aggregate.UserTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.dto.UserCreateDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.dto.UserPatchDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.dto.UserReadDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.dto.UserResponseDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.dto.UserUpdateDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.dto.UsersResponseDtoTestDataBuilder;
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

  @InjectMocks UserController userController;

  @Mock QueryBus queryBus;

  @Mock CommandBus commandBus;

  @Spy UserReadDtoMapper userReadDtoMapper = Mappers.getMapper(UserReadDtoMapper.class);

  @Spy
  CreateUserCommandDtoMapper createUserCommandDtoMapper =
      Mappers.getMapper(CreateUserCommandDtoMapper.class);

  @Spy
  DeleteUserCommandDtoMapper deleteUserCommandDtoMapper =
      Mappers.getMapper(DeleteUserCommandDtoMapper.class);

  @Spy
  UpdateUserCommandDtoMapper updateUserCommandDtoMapper =
      Mappers.getMapper(UpdateUserCommandDtoMapper.class);

  @Spy
  PatchUserCommandDtoMapper patchUserCommandDtoMapper =
      Mappers.getMapper(PatchUserCommandDtoMapper.class);

  @Spy
  FindUserByUserIdQueryDtoMapper findUserByUserIdQueryDtoMapper =
      Mappers.getMapper(FindUserByUserIdQueryDtoMapper.class);

  @Spy
  UserExistsQueryDtoMapper userExistsQueryDtoMapper =
      Mappers.getMapper(UserExistsQueryDtoMapper.class);

  @Spy
  GetAllUserQueryDtoMapper getAllUserQueryDtoMapper =
      Mappers.getMapper(GetAllUserQueryDtoMapper.class);

  @Spy Clock clock = TestClock.FIXED_CLOCK;

  @Test
  void getAllUsers_shouldReturnOk_whenUsersExist() throws Exception {
    final User user = UserTestDataBuilder.builder().build().user();

    final String host = "";
    final Boolean blockEmail = false;
    final Integer page = 0;
    final Integer size = 100;
    final int totalPages = 1;
    final long totalElements = 1L;

    Mockito.when(queryBus.execute(ArgumentMatchers.any(GetUsersFilteredQueryDto.class)))
        .thenReturn(
            PaginationResult.<User>builder()
                .data(Collections.singleton(user))
                .page(page)
                .size(size)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .build());

    final UsersResponseDto expectedBody =
        UsersResponseDtoTestDataBuilder.builder().build().usersResponseDto();
    expectedBody.setPagination(
        new ResponsePaginationDto()
            .page(page)
            .size(size)
            .totalElements(totalElements)
            .totalPages(totalPages));
    final ResponseEntity<UsersResponseDto> responseExpected = ResponseEntity.ok(expectedBody);

    final ResponseEntity<UsersResponseDto> response =
        userController.getAllUsers(host, blockEmail, page, size);

    AssertionsForClassTypes.assertThat(response)
        .usingRecursiveComparison()
        .isEqualTo(responseExpected);

    Mockito.verify(getAllUserQueryDtoMapper)
        .toGetAllUserQuery(
            host,
            blockEmail,
            PaginationTestDataBuilder.builder().page(page).size(size).build().pagination());
    Mockito.verify(queryBus).execute(ArgumentMatchers.any(GetUsersFilteredQueryDto.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void createUser_shouldReturnOk_whenRequestIsValid() throws Exception {
    final User user = UserTestDataBuilder.builder().build().user();
    final UserCreateDto createUserDto =
        UserCreateDtoTestDataBuilder.builder().build().userCreateDto();

    Mockito.when(commandBus.execute(ArgumentMatchers.any(CreateUserCommandDto.class)))
        .thenReturn(user);

    final ResponseEntity<UserResponseDto> responseExpected =
        ResponseEntity.ok(UserResponseDtoTestDataBuilder.builder().build().userResponseDto());

    final ResponseEntity<UserResponseDto> response = userController.createUser(createUserDto);

    AssertionsForClassTypes.assertThat(response)
        .usingRecursiveComparison()
        .isEqualTo(responseExpected);

    Mockito.verify(createUserCommandDtoMapper).toCreateUserCommand(createUserDto);
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(CreateUserCommandDto.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void createUser_shouldThrowErrorResponseException_whenEmailIsInvalid() throws Exception {
    final UserCreateDto createUserDto =
        UserCreateDtoTestDataBuilder.builder().build().userCreateDto();
    final String errorMessage = "Invalid email";

    Mockito.when(commandBus.execute(ArgumentMatchers.any(CreateUserCommandDto.class)))
        .thenThrow(new IllegalArgumentException(errorMessage));

    final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST.value());
    problemDetail.setDetail(errorMessage);
    final ErrorResponseException errorException =
        new ErrorResponseException(HttpStatus.BAD_REQUEST, problemDetail, null);

    AssertionsForClassTypes.assertThatThrownBy(() -> userController.createUser(createUserDto))
        .isInstanceOf(ErrorResponseException.class)
        .usingRecursiveComparison()
        .isEqualTo(errorException);

    Mockito.verify(createUserCommandDtoMapper).toCreateUserCommand(createUserDto);
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(CreateUserCommandDto.class));
  }

  @Test
  void createUser_shouldThrowErrorResponseException_whenDomainExceptionOccurs() throws Exception {
    final UserCreateDto createUserDto =
        UserCreateDtoTestDataBuilder.builder().build().userCreateDto();
    final String errorMessage = "domain exception";

    Mockito.when(commandBus.execute(ArgumentMatchers.any(CreateUserCommandDto.class)))
        .thenThrow(new DomainException(errorMessage));

    final ProblemDetail problemDetail =
        ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
    problemDetail.setDetail(errorMessage);
    final ErrorResponseException errorException =
        new ErrorResponseException(HttpStatus.UNPROCESSABLE_ENTITY, problemDetail, null);

    AssertionsForClassTypes.assertThatThrownBy(() -> userController.createUser(createUserDto))
        .isInstanceOf(ErrorResponseException.class)
        .usingRecursiveComparison()
        .isEqualTo(errorException);

    Mockito.verify(createUserCommandDtoMapper).toCreateUserCommand(createUserDto);
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(CreateUserCommandDto.class));
  }

  @Test
  void createUser_shouldThrowErrorResponseException_whenUnexpectedExceptionOccurs()
      throws Exception {
    final UserCreateDto createUserDto =
        UserCreateDtoTestDataBuilder.builder().build().userCreateDto();
    final String errorMessage = "Unexpected error";

    Mockito.when(commandBus.execute(ArgumentMatchers.any(CreateUserCommandDto.class)))
        .thenThrow(new RuntimeException(errorMessage));

    final ProblemDetail problemDetail =
        ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    problemDetail.setDetail(errorMessage);
    final ErrorResponseException errorException =
        new ErrorResponseException(HttpStatus.INTERNAL_SERVER_ERROR, problemDetail, null);

    AssertionsForClassTypes.assertThatThrownBy(() -> userController.createUser(createUserDto))
        .isInstanceOf(ErrorResponseException.class)
        .usingRecursiveComparison()
        .isEqualTo(errorException);

    Mockito.verify(createUserCommandDtoMapper).toCreateUserCommand(createUserDto);
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(CreateUserCommandDto.class));
  }

  @Test
  void getUserByUuid_shouldReturnOk_whenUserExists() throws Exception {
    final User user = UserTestDataBuilder.builder().build().user();

    Mockito.when(queryBus.execute(ArgumentMatchers.any(FindUserByUserIdQueryDto.class)))
        .thenReturn(user);

    final ResponseEntity<UserResponseDto> responseExpected =
        ResponseEntity.ok(UserResponseDtoTestDataBuilder.builder().build().userResponseDto());

    final ResponseEntity<UserResponseDto> response = userController.getUserByUuid(user.getId());

    AssertionsForClassTypes.assertThat(response)
        .usingRecursiveComparison()
        .isEqualTo(responseExpected);

    Mockito.verify(findUserByUserIdQueryDtoMapper).toFindUserByUserIdQuery(user.getId());
    Mockito.verify(queryBus).execute(ArgumentMatchers.any(FindUserByUserIdQueryDto.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void getUserByUuid_shouldThrowErrorResponseException_whenDomainExceptionOccurs()
      throws Exception {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = "domain exception";

    Mockito.when(queryBus.execute(ArgumentMatchers.any(FindUserByUserIdQueryDto.class)))
        .thenThrow(new DomainException(errorMessage));

    final ProblemDetail problemDetail =
        ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
    problemDetail.setDetail(errorMessage);
    final ErrorResponseException errorException =
        new ErrorResponseException(HttpStatus.UNPROCESSABLE_ENTITY, problemDetail, null);

    AssertionsForClassTypes.assertThatThrownBy(() -> userController.getUserByUuid(user.getId()))
        .isInstanceOf(ErrorResponseException.class)
        .usingRecursiveComparison()
        .isEqualTo(errorException);

    Mockito.verify(findUserByUserIdQueryDtoMapper).toFindUserByUserIdQuery(user.getId());
    Mockito.verify(queryBus).execute(ArgumentMatchers.any(FindUserByUserIdQueryDto.class));
  }

  @Test
  void getUserByUuid_shouldThrowErrorResponseException_whenUserNotFound() throws Exception {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = HttpStatus.NOT_FOUND.getReasonPhrase();

    Mockito.when(queryBus.execute(ArgumentMatchers.any(FindUserByUserIdQueryDto.class)))
        .thenThrow(new ResourceNotFoundException(errorMessage));

    final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND.value());
    problemDetail.setDetail(errorMessage);
    final ErrorResponseException errorException =
        new ErrorResponseException(HttpStatus.NOT_FOUND, problemDetail, null);

    AssertionsForClassTypes.assertThatThrownBy(() -> userController.getUserByUuid(user.getId()))
        .isInstanceOf(ErrorResponseException.class)
        .usingRecursiveComparison()
        .isEqualTo(errorException);

    Mockito.verify(findUserByUserIdQueryDtoMapper).toFindUserByUserIdQuery(user.getId());
    Mockito.verify(queryBus).execute(ArgumentMatchers.any(FindUserByUserIdQueryDto.class));
  }

  @Test
  void getUserByUuid_shouldThrowErrorResponseException_whenUnexpectedExceptionOccurs()
      throws Exception {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = "Unexpected error";

    Mockito.when(queryBus.execute(ArgumentMatchers.any(FindUserByUserIdQueryDto.class)))
        .thenThrow(new RuntimeException(errorMessage));

    final ProblemDetail problemDetail =
        ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    problemDetail.setDetail(errorMessage);
    final ErrorResponseException errorException =
        new ErrorResponseException(HttpStatus.INTERNAL_SERVER_ERROR, problemDetail, null);

    AssertionsForClassTypes.assertThatThrownBy(() -> userController.getUserByUuid(user.getId()))
        .isInstanceOf(ErrorResponseException.class)
        .usingRecursiveComparison()
        .isEqualTo(errorException);

    Mockito.verify(findUserByUserIdQueryDtoMapper).toFindUserByUserIdQuery(user.getId());
    Mockito.verify(queryBus).execute(ArgumentMatchers.any(FindUserByUserIdQueryDto.class));
  }

  @Test
  void deleteUserByUuid_shouldReturnOk_whenUserExists() throws Exception {
    final User user = UserTestDataBuilder.builder().build().user();

    Mockito.when(commandBus.execute(ArgumentMatchers.any(DeleteUserCommandDto.class)))
        .thenReturn(user);

    final ResponseEntity<UserResponseDto> responseExpected =
        ResponseEntity.ok(UserResponseDtoTestDataBuilder.builder().build().userResponseDto());

    final ResponseEntity<UserResponseDto> response = userController.deleteUserByUuid(user.getId());

    AssertionsForClassTypes.assertThat(response)
        .usingRecursiveComparison()
        .isEqualTo(responseExpected);

    Mockito.verify(deleteUserCommandDtoMapper).toDeleteUserCommand(user.getId());
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(DeleteUserCommandDto.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void deleteUserByUuid_shouldThrowErrorResponseException_whenDomainExceptionOccurs()
      throws Exception {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = "domain exception";

    Mockito.when(commandBus.execute(ArgumentMatchers.any(DeleteUserCommandDto.class)))
        .thenThrow(new DomainException(errorMessage));

    final ProblemDetail problemDetail =
        ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
    problemDetail.setDetail(errorMessage);
    final ErrorResponseException errorException =
        new ErrorResponseException(HttpStatus.UNPROCESSABLE_ENTITY, problemDetail, null);

    AssertionsForClassTypes.assertThatThrownBy(() -> userController.deleteUserByUuid(user.getId()))
        .isInstanceOf(ErrorResponseException.class)
        .usingRecursiveComparison()
        .isEqualTo(errorException);

    Mockito.verify(deleteUserCommandDtoMapper).toDeleteUserCommand(user.getId());
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(DeleteUserCommandDto.class));
  }

  @Test
  void deleteUserByUuid_shouldThrowErrorResponseException_whenUnexpectedExceptionOccurs()
      throws Exception {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = "Unexpected error";

    Mockito.when(commandBus.execute(ArgumentMatchers.any(DeleteUserCommandDto.class)))
        .thenThrow(new RuntimeException(errorMessage));

    final ProblemDetail problemDetail =
        ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    problemDetail.setDetail(errorMessage);
    final ErrorResponseException errorException =
        new ErrorResponseException(HttpStatus.INTERNAL_SERVER_ERROR, problemDetail, null);

    AssertionsForClassTypes.assertThatThrownBy(() -> userController.deleteUserByUuid(user.getId()))
        .isInstanceOf(ErrorResponseException.class)
        .usingRecursiveComparison()
        .isEqualTo(errorException);

    Mockito.verify(deleteUserCommandDtoMapper).toDeleteUserCommand(user.getId());
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(DeleteUserCommandDto.class));
  }

  @Test
  void updateUserByUuid_shouldReturnOk_whenRequestIsValid() throws Exception {
    final User user = UserTestDataBuilder.builder().build().user();

    Mockito.when(commandBus.execute(ArgumentMatchers.any(UpdateUserCommandDto.class)))
        .thenReturn(user);

    final ResponseEntity<UserResponseDto> responseExpected =
        ResponseEntity.ok(UserResponseDtoTestDataBuilder.builder().build().userResponseDto());

    final ResponseEntity<UserResponseDto> response =
        userController.updateUserByUuid(
            user.getId(), UserUpdateDtoTestDataBuilder.builder().build().userUpdateDto());

    AssertionsForClassTypes.assertThat(response)
        .usingRecursiveComparison()
        .isEqualTo(responseExpected);

    Mockito.verify(updateUserCommandDtoMapper)
        .toUpdateUserCommand(ArgumentMatchers.eq(user.getId()), ArgumentMatchers.any());
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(UpdateUserCommandDto.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void updateUserByUuid_shouldThrowErrorResponseException_whenInvalidInput() throws Exception {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = "Invalid update";

    Mockito.when(commandBus.execute(ArgumentMatchers.any(UpdateUserCommandDto.class)))
        .thenThrow(new IllegalArgumentException(errorMessage));

    final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST.value());
    problemDetail.setDetail(errorMessage);
    final ErrorResponseException errorException =
        new ErrorResponseException(HttpStatus.BAD_REQUEST, problemDetail, null);

    AssertionsForClassTypes.assertThatThrownBy(
            () ->
                userController.updateUserByUuid(
                    user.getId(), UserUpdateDtoTestDataBuilder.builder().build().userUpdateDto()))
        .isInstanceOf(ErrorResponseException.class)
        .usingRecursiveComparison()
        .isEqualTo(errorException);

    Mockito.verify(updateUserCommandDtoMapper)
        .toUpdateUserCommand(ArgumentMatchers.eq(user.getId()), ArgumentMatchers.any());
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(UpdateUserCommandDto.class));
  }

  @Test
  void updateUserByUuid_shouldThrowErrorResponseException_whenDomainExceptionOccurs()
      throws Exception {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = "domain exception";

    Mockito.when(commandBus.execute(ArgumentMatchers.any(UpdateUserCommandDto.class)))
        .thenThrow(new DomainException(errorMessage));

    final ProblemDetail problemDetail =
        ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
    problemDetail.setDetail(errorMessage);
    final ErrorResponseException errorException =
        new ErrorResponseException(HttpStatus.UNPROCESSABLE_ENTITY, problemDetail, null);

    AssertionsForClassTypes.assertThatThrownBy(
            () ->
                userController.updateUserByUuid(
                    user.getId(), UserUpdateDtoTestDataBuilder.builder().build().userUpdateDto()))
        .isInstanceOf(ErrorResponseException.class)
        .usingRecursiveComparison()
        .isEqualTo(errorException);

    Mockito.verify(updateUserCommandDtoMapper)
        .toUpdateUserCommand(ArgumentMatchers.eq(user.getId()), ArgumentMatchers.any());
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(UpdateUserCommandDto.class));
  }

  @Test
  void updateUserByUuid_shouldThrowErrorResponseException_whenUnexpectedExceptionOccurs()
      throws Exception {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = "Unexpected error";

    Mockito.when(commandBus.execute(ArgumentMatchers.any(UpdateUserCommandDto.class)))
        .thenThrow(new RuntimeException(errorMessage));

    final ProblemDetail problemDetail =
        ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    problemDetail.setDetail(errorMessage);
    final ErrorResponseException errorException =
        new ErrorResponseException(HttpStatus.INTERNAL_SERVER_ERROR, problemDetail, null);

    AssertionsForClassTypes.assertThatThrownBy(
            () ->
                userController.updateUserByUuid(
                    user.getId(), UserUpdateDtoTestDataBuilder.builder().build().userUpdateDto()))
        .isInstanceOf(ErrorResponseException.class)
        .usingRecursiveComparison()
        .isEqualTo(errorException);

    Mockito.verify(updateUserCommandDtoMapper)
        .toUpdateUserCommand(ArgumentMatchers.eq(user.getId()), ArgumentMatchers.any());
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(UpdateUserCommandDto.class));
  }

  @Test
  void updateUserByUuid_shouldThrowErrorResponseException_whenUserNotFound() throws Exception {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = HttpStatus.NOT_FOUND.getReasonPhrase();

    Mockito.when(commandBus.execute(ArgumentMatchers.any(UpdateUserCommandDto.class)))
        .thenThrow(new ResourceNotFoundException(errorMessage));

    final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND.value());
    problemDetail.setDetail(errorMessage);
    final ErrorResponseException errorException =
        new ErrorResponseException(HttpStatus.NOT_FOUND, problemDetail, null);

    AssertionsForClassTypes.assertThatThrownBy(
            () ->
                userController.updateUserByUuid(
                    user.getId(), UserUpdateDtoTestDataBuilder.builder().build().userUpdateDto()))
        .isInstanceOf(ErrorResponseException.class)
        .usingRecursiveComparison()
        .isEqualTo(errorException);

    Mockito.verify(updateUserCommandDtoMapper)
        .toUpdateUserCommand(ArgumentMatchers.eq(user.getId()), ArgumentMatchers.any());
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(UpdateUserCommandDto.class));
  }

  @Test
  void patchUserByUuid_shouldReturnOk_whenRequestIsValid() throws Exception {
    final User user = UserTestDataBuilder.builder().build().user();

    Mockito.when(commandBus.execute(ArgumentMatchers.any(PatchUserCommandDto.class)))
        .thenReturn(user);

    final ResponseEntity<UserResponseDto> responseExpected =
        ResponseEntity.ok(
            UserResponseDtoTestDataBuilder.builder()
                .userReadDto(UserReadDtoTestDataBuilder.builder().build().userReadDto())
                .build()
                .userResponseDto());

    final ResponseEntity<UserResponseDto> response =
        userController.patchUserByUuid(
            user.getId(), UserPatchDtoTestDataBuilder.builder().build().userPatchDto());

    AssertionsForClassTypes.assertThat(response)
        .usingRecursiveComparison()
        .isEqualTo(responseExpected);

    Mockito.verify(patchUserCommandDtoMapper)
        .toPatchUserCommand(ArgumentMatchers.eq(user.getId()), ArgumentMatchers.any());
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(PatchUserCommandDto.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void patchUserByUuid_shouldThrowErrorResponseException_whenUserNotFound() throws Exception {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = HttpStatus.NOT_FOUND.getReasonPhrase();

    Mockito.when(commandBus.execute(ArgumentMatchers.any(PatchUserCommandDto.class)))
        .thenThrow(new ResourceNotFoundException(errorMessage));

    final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND.value());
    problemDetail.setDetail(errorMessage);
    final ErrorResponseException errorException =
        new ErrorResponseException(HttpStatus.NOT_FOUND, problemDetail, null);

    AssertionsForClassTypes.assertThatThrownBy(
            () ->
                userController.patchUserByUuid(
                    user.getId(), UserPatchDtoTestDataBuilder.builder().build().userPatchDto()))
        .isInstanceOf(ErrorResponseException.class)
        .usingRecursiveComparison()
        .isEqualTo(errorException);

    Mockito.verify(patchUserCommandDtoMapper)
        .toPatchUserCommand(ArgumentMatchers.eq(user.getId()), ArgumentMatchers.any());
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(PatchUserCommandDto.class));
  }

  @Test
  void patchUserByUuid_shouldThrowErrorResponseException_whenInvalidInput() throws Exception {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = "Invalid patch";

    Mockito.when(commandBus.execute(ArgumentMatchers.any(PatchUserCommandDto.class)))
        .thenThrow(new IllegalArgumentException(errorMessage));

    final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST.value());
    problemDetail.setDetail(errorMessage);
    final ErrorResponseException errorException =
        new ErrorResponseException(HttpStatus.BAD_REQUEST, problemDetail, null);

    AssertionsForClassTypes.assertThatThrownBy(
            () ->
                userController.patchUserByUuid(
                    user.getId(), UserPatchDtoTestDataBuilder.builder().build().userPatchDto()))
        .isInstanceOf(ErrorResponseException.class)
        .usingRecursiveComparison()
        .isEqualTo(errorException);

    Mockito.verify(patchUserCommandDtoMapper)
        .toPatchUserCommand(ArgumentMatchers.eq(user.getId()), ArgumentMatchers.any());
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(PatchUserCommandDto.class));
  }

  @Test
  void patchUserByUuid_shouldThrowErrorResponseException_whenDomainExceptionOccurs()
      throws Exception {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = "domain exception";

    Mockito.when(commandBus.execute(ArgumentMatchers.any(PatchUserCommandDto.class)))
        .thenThrow(new DomainException(errorMessage));

    final ProblemDetail problemDetail =
        ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
    problemDetail.setDetail(errorMessage);
    final ErrorResponseException errorException =
        new ErrorResponseException(HttpStatus.UNPROCESSABLE_ENTITY, problemDetail, null);

    AssertionsForClassTypes.assertThatThrownBy(
            () ->
                userController.patchUserByUuid(
                    user.getId(), UserPatchDtoTestDataBuilder.builder().build().userPatchDto()))
        .isInstanceOf(ErrorResponseException.class)
        .usingRecursiveComparison()
        .isEqualTo(errorException);

    Mockito.verify(patchUserCommandDtoMapper)
        .toPatchUserCommand(ArgumentMatchers.eq(user.getId()), ArgumentMatchers.any());
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(PatchUserCommandDto.class));
  }

  @Test
  void patchUserByUuid_shouldThrowErrorResponseException_whenUnexpectedExceptionOccurs()
      throws Exception {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = "Unexpected error";

    Mockito.when(commandBus.execute(ArgumentMatchers.any(PatchUserCommandDto.class)))
        .thenThrow(new RuntimeException(errorMessage));

    final ProblemDetail problemDetail =
        ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    problemDetail.setDetail(errorMessage);
    final ErrorResponseException errorException =
        new ErrorResponseException(HttpStatus.INTERNAL_SERVER_ERROR, problemDetail, null);

    AssertionsForClassTypes.assertThatThrownBy(
            () ->
                userController.patchUserByUuid(
                    user.getId(), UserPatchDtoTestDataBuilder.builder().build().userPatchDto()))
        .isInstanceOf(ErrorResponseException.class)
        .usingRecursiveComparison()
        .isEqualTo(errorException);

    Mockito.verify(patchUserCommandDtoMapper)
        .toPatchUserCommand(ArgumentMatchers.eq(user.getId()), ArgumentMatchers.any());
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(PatchUserCommandDto.class));
  }

  @Test
  void headUserByUuid_shouldReturnOk_whenUserExists() throws Exception {
    final User user = UserTestDataBuilder.builder().build().user();

    final ResponseEntity<Void> responseExpected = ResponseEntity.ok().build();

    Mockito.when(queryBus.execute(ArgumentMatchers.any(UserExistsQueryDto.class))).thenReturn(null);

    final ResponseEntity<Void> response = userController.headUserByUuid(user.getId());

    AssertionsForClassTypes.assertThat(response)
        .usingRecursiveComparison()
        .isEqualTo(responseExpected);

    Mockito.verify(userExistsQueryDtoMapper).toUserExistsQuery(user.getId());
    Mockito.verify(queryBus).execute(ArgumentMatchers.any(UserExistsQueryDto.class));
  }

  @Test
  void headUserByUuid_shouldThrowErrorResponseException_whenUserNotFound() throws Exception {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = HttpStatus.NOT_FOUND.getReasonPhrase();

    Mockito.when(queryBus.execute(ArgumentMatchers.any(UserExistsQueryDto.class)))
        .thenThrow(new ResourceNotFoundException(errorMessage));

    final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND.value());
    problemDetail.setDetail(errorMessage);
    final ErrorResponseException errorException =
        new ErrorResponseException(HttpStatus.NOT_FOUND, problemDetail, null);

    AssertionsForClassTypes.assertThatThrownBy(() -> userController.headUserByUuid(user.getId()))
        .isInstanceOf(ErrorResponseException.class)
        .usingRecursiveComparison()
        .isEqualTo(errorException);

    Mockito.verify(userExistsQueryDtoMapper).toUserExistsQuery(user.getId());
    Mockito.verify(queryBus).execute(ArgumentMatchers.any(UserExistsQueryDto.class));
  }

  @Test
  void headUserByUuid_shouldThrowErrorResponseException_whenDomainExceptionOccurs()
      throws Exception {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = "domain exception";

    Mockito.when(queryBus.execute(ArgumentMatchers.any(UserExistsQueryDto.class)))
        .thenThrow(new DomainException(errorMessage));

    final ProblemDetail problemDetail =
        ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
    problemDetail.setDetail(errorMessage);
    final ErrorResponseException errorException =
        new ErrorResponseException(HttpStatus.UNPROCESSABLE_ENTITY, problemDetail, null);

    AssertionsForClassTypes.assertThatThrownBy(() -> userController.headUserByUuid(user.getId()))
        .isInstanceOf(ErrorResponseException.class)
        .usingRecursiveComparison()
        .isEqualTo(errorException);

    Mockito.verify(userExistsQueryDtoMapper).toUserExistsQuery(user.getId());
    Mockito.verify(queryBus).execute(ArgumentMatchers.any(UserExistsQueryDto.class));
  }

  @Test
  void headUserByUuid_shouldThrowErrorResponseException_whenUnexpectedExceptionOccurs()
      throws Exception {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = "Unexpected error";

    Mockito.when(queryBus.execute(ArgumentMatchers.any(UserExistsQueryDto.class)))
        .thenThrow(new RuntimeException(errorMessage));

    final ProblemDetail problemDetail =
        ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    problemDetail.setDetail(errorMessage);
    final ErrorResponseException errorException =
        new ErrorResponseException(HttpStatus.INTERNAL_SERVER_ERROR, problemDetail, null);

    AssertionsForClassTypes.assertThatThrownBy(() -> userController.headUserByUuid(user.getId()))
        .isInstanceOf(ErrorResponseException.class)
        .usingRecursiveComparison()
        .isEqualTo(errorException);

    Mockito.verify(userExistsQueryDtoMapper).toUserExistsQuery(user.getId());
    Mockito.verify(queryBus).execute(ArgumentMatchers.any(UserExistsQueryDto.class));
  }

  @Test
  void getAllUsers_shouldReturnOk_withCustomPagination() throws Exception {
    final User user = UserTestDataBuilder.builder().build().user();
    final Integer page = 1;
    final Integer size = 10;
    final int totalPages = 5;
    final long totalElements = 50L;

    Mockito.when(queryBus.execute(ArgumentMatchers.any(GetUsersFilteredQueryDto.class)))
        .thenReturn(
            PaginationResult.<User>builder()
                .data(Collections.singleton(user))
                .page(page)
                .size(size)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .build());

    final UsersResponseDto expectedBody =
        UsersResponseDtoTestDataBuilder.builder().build().usersResponseDto();
    expectedBody.setPagination(
        new ResponsePaginationDto()
            .page(page)
            .size(size)
            .totalElements(totalElements)
            .totalPages(totalPages));
    final ResponseEntity<UsersResponseDto> responseExpected = ResponseEntity.ok(expectedBody);

    final ResponseEntity<UsersResponseDto> response =
        userController.getAllUsers(null, null, page, size);

    AssertionsForClassTypes.assertThat(response)
        .usingRecursiveComparison()
        .isEqualTo(responseExpected);

    Mockito.verify(getAllUserQueryDtoMapper)
        .toGetAllUserQuery(
            null,
            null,
            PaginationTestDataBuilder.builder().page(page).size(size).build().pagination());
    Mockito.verify(queryBus).execute(ArgumentMatchers.any(GetUsersFilteredQueryDto.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void getAllUsers_shouldThrowErrorResponseException_whenDomainExceptionOccurs() throws Exception {
    final String errorMessage = ExceptionMessage.EMAIL_NO_ALLOWED_MESSAGE + "blocked@banned.com";

    Mockito.when(queryBus.execute(ArgumentMatchers.any(GetUsersFilteredQueryDto.class)))
        .thenThrow(new InvalidValueException(errorMessage));

    final ProblemDetail problemDetail =
        ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
    problemDetail.setDetail(errorMessage);
    final ErrorResponseException errorException =
        new ErrorResponseException(HttpStatus.UNPROCESSABLE_ENTITY, problemDetail, null);

    AssertionsForClassTypes.assertThatThrownBy(() -> userController.getAllUsers(null, null, 0, 100))
        .isInstanceOf(ErrorResponseException.class)
        .usingRecursiveComparison()
        .isEqualTo(errorException);

    Mockito.verify(queryBus).execute(ArgumentMatchers.any(GetUsersFilteredQueryDto.class));
  }

  @Test
  void getAllUsers_shouldThrowErrorResponseException_whenUnexpectedExceptionOccurs()
      throws Exception {
    final String errorMessage = "Unexpected error";

    Mockito.when(queryBus.execute(ArgumentMatchers.any(GetUsersFilteredQueryDto.class)))
        .thenThrow(new RuntimeException(errorMessage));

    final ProblemDetail problemDetail =
        ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    problemDetail.setDetail(errorMessage);
    final ErrorResponseException errorException =
        new ErrorResponseException(HttpStatus.INTERNAL_SERVER_ERROR, problemDetail, null);

    AssertionsForClassTypes.assertThatThrownBy(() -> userController.getAllUsers(null, null, 0, 100))
        .isInstanceOf(ErrorResponseException.class)
        .usingRecursiveComparison()
        .isEqualTo(errorException);

    Mockito.verify(queryBus).execute(ArgumentMatchers.any(GetUsersFilteredQueryDto.class));
  }
}
