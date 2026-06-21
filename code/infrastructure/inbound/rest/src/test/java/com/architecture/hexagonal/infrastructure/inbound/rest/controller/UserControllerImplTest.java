package com.architecture.hexagonal.infrastructure.inbound.rest.controller;

import com.architecture.hexagonal.domain.exception.DomainException;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.InvalidValueException;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.domain.model.pagination.PaginationResult;
import com.architecture.hexagonal.infrastructure.contract.orchestration.generated.user.*;
import com.architecture.hexagonal.infrastructure.contract.rest.user.server.dto.ResponsePaginationDto;
import com.architecture.hexagonal.infrastructure.contract.rest.user.server.dto.UserCreateDto;
import com.architecture.hexagonal.infrastructure.contract.rest.user.server.dto.UserResponseDto;
import com.architecture.hexagonal.infrastructure.contract.rest.user.server.dto.UsersResponseDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.command.CommandBus;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.query.QueryBus;
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
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class UserControllerImplTest {

  @InjectMocks UserControllerImpl userControllerImpl;

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
  void getAllUsers_shouldReturnOk_whenUsersExist() {
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
                .data(Collections.singletonList(user))
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
        userControllerImpl.getAllUsers(host, blockEmail, page, size);

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
  void createUser_shouldReturnOk_whenRequestIsValid() {
    final User user = UserTestDataBuilder.builder().build().user();
    final UserCreateDto createUserDto =
        UserCreateDtoTestDataBuilder.builder().build().userCreateDto();

    Mockito.when(commandBus.execute(ArgumentMatchers.any(CreateUserCommandDto.class)))
        .thenReturn(user);

    final ResponseEntity<UserResponseDto> responseExpected =
        ResponseEntity.ok(UserResponseDtoTestDataBuilder.builder().build().userResponseDto());

    final ResponseEntity<UserResponseDto> response = userControllerImpl.createUser(createUserDto);

    AssertionsForClassTypes.assertThat(response)
        .usingRecursiveComparison()
        .isEqualTo(responseExpected);

    Mockito.verify(createUserCommandDtoMapper).toCreateUserCommand(createUserDto);
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(CreateUserCommandDto.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void createUser_shouldPropagateIllegalArgumentException_whenEmailIsInvalid() {
    final UserCreateDto createUserDto =
        UserCreateDtoTestDataBuilder.builder().build().userCreateDto();
    final String errorMessage = "Invalid email";

    Mockito.when(commandBus.execute(ArgumentMatchers.any(CreateUserCommandDto.class)))
        .thenThrow(new IllegalArgumentException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(() -> userControllerImpl.createUser(createUserDto))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(errorMessage);

    Mockito.verify(createUserCommandDtoMapper).toCreateUserCommand(createUserDto);
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(CreateUserCommandDto.class));
  }

  @Test
  void createUser_shouldPropagateDomainException_whenDomainExceptionOccurs() {
    final UserCreateDto createUserDto =
        UserCreateDtoTestDataBuilder.builder().build().userCreateDto();
    final String errorMessage = "domain exception";

    Mockito.when(commandBus.execute(ArgumentMatchers.any(CreateUserCommandDto.class)))
        .thenThrow(new DomainException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(() -> userControllerImpl.createUser(createUserDto))
        .isInstanceOf(DomainException.class)
        .hasMessage(errorMessage);

    Mockito.verify(createUserCommandDtoMapper).toCreateUserCommand(createUserDto);
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(CreateUserCommandDto.class));
  }

  @Test
  void createUser_shouldPropagateRuntimeException_whenUnexpectedExceptionOccurs() {
    final UserCreateDto createUserDto =
        UserCreateDtoTestDataBuilder.builder().build().userCreateDto();
    final String errorMessage = "Unexpected error";

    Mockito.when(commandBus.execute(ArgumentMatchers.any(CreateUserCommandDto.class)))
        .thenThrow(new RuntimeException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(() -> userControllerImpl.createUser(createUserDto))
        .isInstanceOf(RuntimeException.class)
        .hasMessage(errorMessage);

    Mockito.verify(createUserCommandDtoMapper).toCreateUserCommand(createUserDto);
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(CreateUserCommandDto.class));
  }

  @Test
  void getUserByUuid_shouldReturnOk_whenUserExists() {
    final User user = UserTestDataBuilder.builder().build().user();

    Mockito.when(queryBus.execute(ArgumentMatchers.any(FindUserByUserIdQueryDto.class)))
        .thenReturn(user);

    final ResponseEntity<UserResponseDto> responseExpected =
        ResponseEntity.ok(UserResponseDtoTestDataBuilder.builder().build().userResponseDto());

    final ResponseEntity<UserResponseDto> response = userControllerImpl.getUserByUuid(user.getId());

    AssertionsForClassTypes.assertThat(response)
        .usingRecursiveComparison()
        .isEqualTo(responseExpected);

    Mockito.verify(findUserByUserIdQueryDtoMapper).toFindUserByUserIdQuery(user.getId());
    Mockito.verify(queryBus).execute(ArgumentMatchers.any(FindUserByUserIdQueryDto.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void getUserByUuid_shouldPropagateDomainException_whenDomainExceptionOccurs() {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = "domain exception";

    Mockito.when(queryBus.execute(ArgumentMatchers.any(FindUserByUserIdQueryDto.class)))
        .thenThrow(new DomainException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(() -> userControllerImpl.getUserByUuid(user.getId()))
        .isInstanceOf(DomainException.class)
        .hasMessage(errorMessage);

    Mockito.verify(findUserByUserIdQueryDtoMapper).toFindUserByUserIdQuery(user.getId());
    Mockito.verify(queryBus).execute(ArgumentMatchers.any(FindUserByUserIdQueryDto.class));
  }

  @Test
  void getUserByUuid_shouldPropagateResourceNotFoundException_whenUserNotFound() {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = HttpStatus.NOT_FOUND.getReasonPhrase();

    Mockito.when(queryBus.execute(ArgumentMatchers.any(FindUserByUserIdQueryDto.class)))
        .thenThrow(new ResourceNotFoundException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(() -> userControllerImpl.getUserByUuid(user.getId()))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(errorMessage);

    Mockito.verify(findUserByUserIdQueryDtoMapper).toFindUserByUserIdQuery(user.getId());
    Mockito.verify(queryBus).execute(ArgumentMatchers.any(FindUserByUserIdQueryDto.class));
  }

  @Test
  void getUserByUuid_shouldPropagateRuntimeException_whenUnexpectedExceptionOccurs() {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = "Unexpected error";

    Mockito.when(queryBus.execute(ArgumentMatchers.any(FindUserByUserIdQueryDto.class)))
        .thenThrow(new RuntimeException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(() -> userControllerImpl.getUserByUuid(user.getId()))
        .isInstanceOf(RuntimeException.class)
        .hasMessage(errorMessage);

    Mockito.verify(findUserByUserIdQueryDtoMapper).toFindUserByUserIdQuery(user.getId());
    Mockito.verify(queryBus).execute(ArgumentMatchers.any(FindUserByUserIdQueryDto.class));
  }

  @Test
  void deleteUserByUuid_shouldReturnOk_whenUserExists() {
    final User user = UserTestDataBuilder.builder().build().user();

    Mockito.when(commandBus.execute(ArgumentMatchers.any(DeleteUserCommandDto.class)))
        .thenReturn(user);

    final ResponseEntity<UserResponseDto> responseExpected =
        ResponseEntity.ok(UserResponseDtoTestDataBuilder.builder().build().userResponseDto());

    final ResponseEntity<UserResponseDto> response =
        userControllerImpl.deleteUserByUuid(user.getId());

    AssertionsForClassTypes.assertThat(response)
        .usingRecursiveComparison()
        .isEqualTo(responseExpected);

    Mockito.verify(deleteUserCommandDtoMapper).toDeleteUserCommand(user.getId());
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(DeleteUserCommandDto.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void deleteUserByUuid_shouldPropagateDomainException_whenDomainExceptionOccurs() {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = "domain exception";

    Mockito.when(commandBus.execute(ArgumentMatchers.any(DeleteUserCommandDto.class)))
        .thenThrow(new DomainException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(
            () -> userControllerImpl.deleteUserByUuid(user.getId()))
        .isInstanceOf(DomainException.class)
        .hasMessage(errorMessage);

    Mockito.verify(deleteUserCommandDtoMapper).toDeleteUserCommand(user.getId());
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(DeleteUserCommandDto.class));
  }

  @Test
  void deleteUserByUuid_shouldPropagateRuntimeException_whenUnexpectedExceptionOccurs() {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = "Unexpected error";

    Mockito.when(commandBus.execute(ArgumentMatchers.any(DeleteUserCommandDto.class)))
        .thenThrow(new RuntimeException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(
            () -> userControllerImpl.deleteUserByUuid(user.getId()))
        .isInstanceOf(RuntimeException.class)
        .hasMessage(errorMessage);

    Mockito.verify(deleteUserCommandDtoMapper).toDeleteUserCommand(user.getId());
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(DeleteUserCommandDto.class));
  }

  @Test
  void updateUserByUuid_shouldReturnOk_whenRequestIsValid() {
    final User user = UserTestDataBuilder.builder().build().user();

    Mockito.when(commandBus.execute(ArgumentMatchers.any(UpdateUserCommandDto.class)))
        .thenReturn(user);

    final ResponseEntity<UserResponseDto> responseExpected =
        ResponseEntity.ok(UserResponseDtoTestDataBuilder.builder().build().userResponseDto());

    final ResponseEntity<UserResponseDto> response =
        userControllerImpl.updateUserByUuid(
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
  void updateUserByUuid_shouldPropagateIllegalArgumentException_whenInvalidInput() {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = "Invalid update";

    Mockito.when(commandBus.execute(ArgumentMatchers.any(UpdateUserCommandDto.class)))
        .thenThrow(new IllegalArgumentException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(
            () ->
                userControllerImpl.updateUserByUuid(
                    user.getId(), UserUpdateDtoTestDataBuilder.builder().build().userUpdateDto()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(errorMessage);

    Mockito.verify(updateUserCommandDtoMapper)
        .toUpdateUserCommand(ArgumentMatchers.eq(user.getId()), ArgumentMatchers.any());
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(UpdateUserCommandDto.class));
  }

  @Test
  void updateUserByUuid_shouldPropagateDomainException_whenDomainExceptionOccurs() {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = "domain exception";

    Mockito.when(commandBus.execute(ArgumentMatchers.any(UpdateUserCommandDto.class)))
        .thenThrow(new DomainException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(
            () ->
                userControllerImpl.updateUserByUuid(
                    user.getId(), UserUpdateDtoTestDataBuilder.builder().build().userUpdateDto()))
        .isInstanceOf(DomainException.class)
        .hasMessage(errorMessage);

    Mockito.verify(updateUserCommandDtoMapper)
        .toUpdateUserCommand(ArgumentMatchers.eq(user.getId()), ArgumentMatchers.any());
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(UpdateUserCommandDto.class));
  }

  @Test
  void updateUserByUuid_shouldPropagateRuntimeException_whenUnexpectedExceptionOccurs() {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = "Unexpected error";

    Mockito.when(commandBus.execute(ArgumentMatchers.any(UpdateUserCommandDto.class)))
        .thenThrow(new RuntimeException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(
            () ->
                userControllerImpl.updateUserByUuid(
                    user.getId(), UserUpdateDtoTestDataBuilder.builder().build().userUpdateDto()))
        .isInstanceOf(RuntimeException.class)
        .hasMessage(errorMessage);

    Mockito.verify(updateUserCommandDtoMapper)
        .toUpdateUserCommand(ArgumentMatchers.eq(user.getId()), ArgumentMatchers.any());
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(UpdateUserCommandDto.class));
  }

  @Test
  void updateUserByUuid_shouldPropagateResourceNotFoundException_whenUserNotFound() {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = HttpStatus.NOT_FOUND.getReasonPhrase();

    Mockito.when(commandBus.execute(ArgumentMatchers.any(UpdateUserCommandDto.class)))
        .thenThrow(new ResourceNotFoundException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(
            () ->
                userControllerImpl.updateUserByUuid(
                    user.getId(), UserUpdateDtoTestDataBuilder.builder().build().userUpdateDto()))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(errorMessage);

    Mockito.verify(updateUserCommandDtoMapper)
        .toUpdateUserCommand(ArgumentMatchers.eq(user.getId()), ArgumentMatchers.any());
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(UpdateUserCommandDto.class));
  }

  @Test
  void patchUserByUuid_shouldReturnOk_whenRequestIsValid() {
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
        userControllerImpl.patchUserByUuid(
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
  void patchUserByUuid_shouldPropagateResourceNotFoundException_whenUserNotFound() {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = HttpStatus.NOT_FOUND.getReasonPhrase();

    Mockito.when(commandBus.execute(ArgumentMatchers.any(PatchUserCommandDto.class)))
        .thenThrow(new ResourceNotFoundException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(
            () ->
                userControllerImpl.patchUserByUuid(
                    user.getId(), UserPatchDtoTestDataBuilder.builder().build().userPatchDto()))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(errorMessage);

    Mockito.verify(patchUserCommandDtoMapper)
        .toPatchUserCommand(ArgumentMatchers.eq(user.getId()), ArgumentMatchers.any());
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(PatchUserCommandDto.class));
  }

  @Test
  void patchUserByUuid_shouldPropagateIllegalArgumentException_whenInvalidInput() {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = "Invalid patch";

    Mockito.when(commandBus.execute(ArgumentMatchers.any(PatchUserCommandDto.class)))
        .thenThrow(new IllegalArgumentException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(
            () ->
                userControllerImpl.patchUserByUuid(
                    user.getId(), UserPatchDtoTestDataBuilder.builder().build().userPatchDto()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(errorMessage);

    Mockito.verify(patchUserCommandDtoMapper)
        .toPatchUserCommand(ArgumentMatchers.eq(user.getId()), ArgumentMatchers.any());
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(PatchUserCommandDto.class));
  }

  @Test
  void patchUserByUuid_shouldPropagateDomainException_whenDomainExceptionOccurs() {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = "domain exception";

    Mockito.when(commandBus.execute(ArgumentMatchers.any(PatchUserCommandDto.class)))
        .thenThrow(new DomainException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(
            () ->
                userControllerImpl.patchUserByUuid(
                    user.getId(), UserPatchDtoTestDataBuilder.builder().build().userPatchDto()))
        .isInstanceOf(DomainException.class)
        .hasMessage(errorMessage);

    Mockito.verify(patchUserCommandDtoMapper)
        .toPatchUserCommand(ArgumentMatchers.eq(user.getId()), ArgumentMatchers.any());
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(PatchUserCommandDto.class));
  }

  @Test
  void patchUserByUuid_shouldPropagateRuntimeException_whenUnexpectedExceptionOccurs() {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = "Unexpected error";

    Mockito.when(commandBus.execute(ArgumentMatchers.any(PatchUserCommandDto.class)))
        .thenThrow(new RuntimeException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(
            () ->
                userControllerImpl.patchUserByUuid(
                    user.getId(), UserPatchDtoTestDataBuilder.builder().build().userPatchDto()))
        .isInstanceOf(RuntimeException.class)
        .hasMessage(errorMessage);

    Mockito.verify(patchUserCommandDtoMapper)
        .toPatchUserCommand(ArgumentMatchers.eq(user.getId()), ArgumentMatchers.any());
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(PatchUserCommandDto.class));
  }

  @Test
  void headUserByUuid_shouldReturnOk_whenUserExists() {
    final User user = UserTestDataBuilder.builder().build().user();

    final ResponseEntity<Void> responseExpected = ResponseEntity.ok().build();

    Mockito.when(queryBus.execute(ArgumentMatchers.any(UserExistsQueryDto.class))).thenReturn(null);

    final ResponseEntity<Void> response = userControllerImpl.headUserByUuid(user.getId());

    AssertionsForClassTypes.assertThat(response)
        .usingRecursiveComparison()
        .isEqualTo(responseExpected);

    Mockito.verify(userExistsQueryDtoMapper).toUserExistsQuery(user.getId());
    Mockito.verify(queryBus).execute(ArgumentMatchers.any(UserExistsQueryDto.class));
  }

  @Test
  void headUserByUuid_shouldPropagateResourceNotFoundException_whenUserNotFound() {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = HttpStatus.NOT_FOUND.getReasonPhrase();

    Mockito.when(queryBus.execute(ArgumentMatchers.any(UserExistsQueryDto.class)))
        .thenThrow(new ResourceNotFoundException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(
            () -> userControllerImpl.headUserByUuid(user.getId()))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(errorMessage);

    Mockito.verify(userExistsQueryDtoMapper).toUserExistsQuery(user.getId());
    Mockito.verify(queryBus).execute(ArgumentMatchers.any(UserExistsQueryDto.class));
  }

  @Test
  void headUserByUuid_shouldPropagateDomainException_whenDomainExceptionOccurs() {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = "domain exception";

    Mockito.when(queryBus.execute(ArgumentMatchers.any(UserExistsQueryDto.class)))
        .thenThrow(new DomainException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(
            () -> userControllerImpl.headUserByUuid(user.getId()))
        .isInstanceOf(DomainException.class)
        .hasMessage(errorMessage);

    Mockito.verify(userExistsQueryDtoMapper).toUserExistsQuery(user.getId());
    Mockito.verify(queryBus).execute(ArgumentMatchers.any(UserExistsQueryDto.class));
  }

  @Test
  void headUserByUuid_shouldPropagateRuntimeException_whenUnexpectedExceptionOccurs() {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = "Unexpected error";

    Mockito.when(queryBus.execute(ArgumentMatchers.any(UserExistsQueryDto.class)))
        .thenThrow(new RuntimeException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(
            () -> userControllerImpl.headUserByUuid(user.getId()))
        .isInstanceOf(RuntimeException.class)
        .hasMessage(errorMessage);

    Mockito.verify(userExistsQueryDtoMapper).toUserExistsQuery(user.getId());
    Mockito.verify(queryBus).execute(ArgumentMatchers.any(UserExistsQueryDto.class));
  }

  @Test
  void getAllUsers_shouldReturnOk_withCustomPagination() {
    final User user = UserTestDataBuilder.builder().build().user();
    final Integer page = 1;
    final Integer size = 10;
    final int totalPages = 5;
    final long totalElements = 50L;

    Mockito.when(queryBus.execute(ArgumentMatchers.any(GetUsersFilteredQueryDto.class)))
        .thenReturn(
            PaginationResult.<User>builder()
                .data(Collections.singletonList(user))
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
        userControllerImpl.getAllUsers(null, null, page, size);

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
  void getAllUsers_shouldPropagateInvalidValueException_whenDomainExceptionOccurs() {
    final String errorMessage = ExceptionMessage.EMAIL_NO_ALLOWED_MESSAGE + "blocked@banned.com";

    Mockito.when(queryBus.execute(ArgumentMatchers.any(GetUsersFilteredQueryDto.class)))
        .thenThrow(new InvalidValueException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(
            () -> userControllerImpl.getAllUsers(null, null, 0, 100))
        .isInstanceOf(InvalidValueException.class)
        .hasMessage(errorMessage);

    Mockito.verify(queryBus).execute(ArgumentMatchers.any(GetUsersFilteredQueryDto.class));
  }

  @Test
  void getAllUsers_shouldPropagateRuntimeException_whenUnexpectedExceptionOccurs() {
    final String errorMessage = "Unexpected error";

    Mockito.when(queryBus.execute(ArgumentMatchers.any(GetUsersFilteredQueryDto.class)))
        .thenThrow(new RuntimeException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(
            () -> userControllerImpl.getAllUsers(null, null, 0, 100))
        .isInstanceOf(RuntimeException.class)
        .hasMessage(errorMessage);

    Mockito.verify(queryBus).execute(ArgumentMatchers.any(GetUsersFilteredQueryDto.class));
  }
}
