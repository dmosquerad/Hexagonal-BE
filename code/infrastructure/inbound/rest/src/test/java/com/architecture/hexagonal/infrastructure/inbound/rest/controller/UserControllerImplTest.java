package com.architecture.hexagonal.infrastructure.inbound.rest.controller;

import com.architecture.hexagonal.domain.exception.DomainException;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.InvalidValueException;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.pagination.PaginationResult;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.user.*;
import com.architecture.hexagonal.infrastructure.inbound.contract.rest.user.server.dto.ResponsePaginationDto;
import com.architecture.hexagonal.infrastructure.inbound.contract.rest.user.server.dto.UserCreateDto;
import com.architecture.hexagonal.infrastructure.inbound.contract.rest.user.server.dto.UserResponseDto;
import com.architecture.hexagonal.infrastructure.inbound.contract.rest.user.server.dto.UsersResponseDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.command.CommandBus;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.query.QueryBus;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.user.*;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.user.FindUserByUserIdQueryDtoMapper;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.dto.UserCreateDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.dto.UserPatchDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.dto.UserReadDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.dto.UserResponseDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.dto.UserUpdateDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.dto.UsersResponseDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.model.entity.pagination.PaginationTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.model.entity.user.UserTestDataBuilder;
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
  void getAllUsersShouldReturnOkWhenUsersExist() {
    final User user = UserTestDataBuilder.builder().build().user();

    final String host = "";
    final Boolean blockEmail = false;
    final Integer page = 0;
    final Integer size = 100;
    final int totalPages = 1;
    final long totalElements = 1L;

    Mockito.when(queryBus.execute(Mockito.any(GetUsersFilteredQueryDto.class)))
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
    Mockito.verify(queryBus).execute(Mockito.any(GetUsersFilteredQueryDto.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void createUserShouldReturnOkWhenRequestIsValid() {
    final User user = UserTestDataBuilder.builder().build().user();
    final UserCreateDto createUserDto =
        UserCreateDtoTestDataBuilder.builder().build().userCreateDto();

    Mockito.when(commandBus.execute(Mockito.any(CreateUserCommandDto.class))).thenReturn(user);

    final ResponseEntity<UserResponseDto> responseExpected =
        ResponseEntity.ok(UserResponseDtoTestDataBuilder.builder().build().userResponseDto());

    final ResponseEntity<UserResponseDto> response = userControllerImpl.createUser(createUserDto);

    AssertionsForClassTypes.assertThat(response)
        .usingRecursiveComparison()
        .isEqualTo(responseExpected);

    Mockito.verify(createUserCommandDtoMapper).toCreateUserCommand(createUserDto);
    Mockito.verify(commandBus).execute(Mockito.any(CreateUserCommandDto.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void createUserShouldPropagateIllegalArgumentExceptionWhenEmailIsInvalid() {
    final UserCreateDto createUserDto =
        UserCreateDtoTestDataBuilder.builder().build().userCreateDto();
    final String errorMessage = "Invalid email";

    Mockito.when(commandBus.execute(Mockito.any(CreateUserCommandDto.class)))
        .thenThrow(new IllegalArgumentException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(() -> userControllerImpl.createUser(createUserDto))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(errorMessage);

    Mockito.verify(createUserCommandDtoMapper).toCreateUserCommand(createUserDto);
    Mockito.verify(commandBus).execute(Mockito.any(CreateUserCommandDto.class));
  }

  @Test
  void createUserShouldPropagateDomainExceptionWhenDomainExceptionOccurs() {
    final UserCreateDto createUserDto =
        UserCreateDtoTestDataBuilder.builder().build().userCreateDto();
    final String errorMessage = "domain exception";

    Mockito.when(commandBus.execute(Mockito.any(CreateUserCommandDto.class)))
        .thenThrow(new DomainException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(() -> userControllerImpl.createUser(createUserDto))
        .isInstanceOf(DomainException.class)
        .hasMessage(errorMessage);

    Mockito.verify(createUserCommandDtoMapper).toCreateUserCommand(createUserDto);
    Mockito.verify(commandBus).execute(Mockito.any(CreateUserCommandDto.class));
  }

  @Test
  void createUserShouldPropagateRuntimeExceptionWhenUnexpectedExceptionOccurs() {
    final UserCreateDto createUserDto =
        UserCreateDtoTestDataBuilder.builder().build().userCreateDto();
    final String errorMessage = "Unexpected error";

    Mockito.when(commandBus.execute(Mockito.any(CreateUserCommandDto.class)))
        .thenThrow(new RuntimeException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(() -> userControllerImpl.createUser(createUserDto))
        .isInstanceOf(RuntimeException.class)
        .hasMessage(errorMessage);

    Mockito.verify(createUserCommandDtoMapper).toCreateUserCommand(createUserDto);
    Mockito.verify(commandBus).execute(Mockito.any(CreateUserCommandDto.class));
  }

  @Test
  void getUserByUuidShouldReturnOkWhenUserExists() {
    final User user = UserTestDataBuilder.builder().build().user();

    Mockito.when(queryBus.execute(Mockito.any(FindUserByUserIdQueryDto.class))).thenReturn(user);

    final ResponseEntity<UserResponseDto> responseExpected =
        ResponseEntity.ok(UserResponseDtoTestDataBuilder.builder().build().userResponseDto());

    final ResponseEntity<UserResponseDto> response = userControllerImpl.getUserByUuid(user.getId());

    AssertionsForClassTypes.assertThat(response)
        .usingRecursiveComparison()
        .isEqualTo(responseExpected);

    Mockito.verify(findUserByUserIdQueryDtoMapper).toFindUserByUserIdQuery(user.getId());
    Mockito.verify(queryBus).execute(Mockito.any(FindUserByUserIdQueryDto.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void getUserByUuidShouldPropagateDomainExceptionWhenDomainExceptionOccurs() {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = "domain exception";

    Mockito.when(queryBus.execute(Mockito.any(FindUserByUserIdQueryDto.class)))
        .thenThrow(new DomainException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(() -> userControllerImpl.getUserByUuid(user.getId()))
        .isInstanceOf(DomainException.class)
        .hasMessage(errorMessage);

    Mockito.verify(findUserByUserIdQueryDtoMapper).toFindUserByUserIdQuery(user.getId());
    Mockito.verify(queryBus).execute(Mockito.any(FindUserByUserIdQueryDto.class));
  }

  @Test
  void getUserByUuidShouldPropagateResourceNotFoundExceptionWhenUserNotFound() {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = HttpStatus.NOT_FOUND.getReasonPhrase();

    Mockito.when(queryBus.execute(Mockito.any(FindUserByUserIdQueryDto.class)))
        .thenThrow(new ResourceNotFoundException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(() -> userControllerImpl.getUserByUuid(user.getId()))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(errorMessage);

    Mockito.verify(findUserByUserIdQueryDtoMapper).toFindUserByUserIdQuery(user.getId());
    Mockito.verify(queryBus).execute(Mockito.any(FindUserByUserIdQueryDto.class));
  }

  @Test
  void getUserByUuidShouldPropagateRuntimeExceptionWhenUnexpectedExceptionOccurs() {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = "Unexpected error";

    Mockito.when(queryBus.execute(Mockito.any(FindUserByUserIdQueryDto.class)))
        .thenThrow(new RuntimeException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(() -> userControllerImpl.getUserByUuid(user.getId()))
        .isInstanceOf(RuntimeException.class)
        .hasMessage(errorMessage);

    Mockito.verify(findUserByUserIdQueryDtoMapper).toFindUserByUserIdQuery(user.getId());
    Mockito.verify(queryBus).execute(Mockito.any(FindUserByUserIdQueryDto.class));
  }

  @Test
  void deleteUserByUuidShouldReturnOkWhenUserExists() {
    final User user = UserTestDataBuilder.builder().build().user();

    Mockito.when(commandBus.execute(Mockito.any(DeleteUserCommandDto.class))).thenReturn(user);

    final ResponseEntity<UserResponseDto> responseExpected =
        ResponseEntity.ok(UserResponseDtoTestDataBuilder.builder().build().userResponseDto());

    final ResponseEntity<UserResponseDto> response =
        userControllerImpl.deleteUserByUuid(user.getId());

    AssertionsForClassTypes.assertThat(response)
        .usingRecursiveComparison()
        .isEqualTo(responseExpected);

    Mockito.verify(deleteUserCommandDtoMapper).toDeleteUserCommand(user.getId());
    Mockito.verify(commandBus).execute(Mockito.any(DeleteUserCommandDto.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void deleteUserByUuidShouldPropagateDomainExceptionWhenDomainExceptionOccurs() {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = "domain exception";

    Mockito.when(commandBus.execute(Mockito.any(DeleteUserCommandDto.class)))
        .thenThrow(new DomainException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(
            () -> userControllerImpl.deleteUserByUuid(user.getId()))
        .isInstanceOf(DomainException.class)
        .hasMessage(errorMessage);

    Mockito.verify(deleteUserCommandDtoMapper).toDeleteUserCommand(user.getId());
    Mockito.verify(commandBus).execute(Mockito.any(DeleteUserCommandDto.class));
  }

  @Test
  void deleteUserByUuidShouldPropagateRuntimeExceptionWhenUnexpectedExceptionOccurs() {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = "Unexpected error";

    Mockito.when(commandBus.execute(Mockito.any(DeleteUserCommandDto.class)))
        .thenThrow(new RuntimeException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(
            () -> userControllerImpl.deleteUserByUuid(user.getId()))
        .isInstanceOf(RuntimeException.class)
        .hasMessage(errorMessage);

    Mockito.verify(deleteUserCommandDtoMapper).toDeleteUserCommand(user.getId());
    Mockito.verify(commandBus).execute(Mockito.any(DeleteUserCommandDto.class));
  }

  @Test
  void updateUserByUuidShouldReturnOkWhenRequestIsValid() {
    final User user = UserTestDataBuilder.builder().build().user();

    Mockito.when(commandBus.execute(Mockito.any(UpdateUserCommandDto.class))).thenReturn(user);

    final ResponseEntity<UserResponseDto> responseExpected =
        ResponseEntity.ok(UserResponseDtoTestDataBuilder.builder().build().userResponseDto());

    final ResponseEntity<UserResponseDto> response =
        userControllerImpl.updateUserByUuid(
            user.getId(), UserUpdateDtoTestDataBuilder.builder().build().userUpdateDto());

    AssertionsForClassTypes.assertThat(response)
        .usingRecursiveComparison()
        .isEqualTo(responseExpected);

    Mockito.verify(updateUserCommandDtoMapper)
        .toUpdateUserCommand(ArgumentMatchers.eq(user.getId()), Mockito.any());
    Mockito.verify(commandBus).execute(Mockito.any(UpdateUserCommandDto.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void updateUserByUuidShouldPropagateIllegalArgumentExceptionWhenInvalidInput() {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = "Invalid update";

    Mockito.when(commandBus.execute(Mockito.any(UpdateUserCommandDto.class)))
        .thenThrow(new IllegalArgumentException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(
            () ->
                userControllerImpl.updateUserByUuid(
                    user.getId(), UserUpdateDtoTestDataBuilder.builder().build().userUpdateDto()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(errorMessage);

    Mockito.verify(updateUserCommandDtoMapper)
        .toUpdateUserCommand(ArgumentMatchers.eq(user.getId()), Mockito.any());
    Mockito.verify(commandBus).execute(Mockito.any(UpdateUserCommandDto.class));
  }

  @Test
  void updateUserByUuidShouldPropagateDomainExceptionWhenDomainExceptionOccurs() {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = "domain exception";

    Mockito.when(commandBus.execute(Mockito.any(UpdateUserCommandDto.class)))
        .thenThrow(new DomainException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(
            () ->
                userControllerImpl.updateUserByUuid(
                    user.getId(), UserUpdateDtoTestDataBuilder.builder().build().userUpdateDto()))
        .isInstanceOf(DomainException.class)
        .hasMessage(errorMessage);

    Mockito.verify(updateUserCommandDtoMapper)
        .toUpdateUserCommand(ArgumentMatchers.eq(user.getId()), Mockito.any());
    Mockito.verify(commandBus).execute(Mockito.any(UpdateUserCommandDto.class));
  }

  @Test
  void updateUserByUuidShouldPropagateRuntimeExceptionWhenUnexpectedExceptionOccurs() {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = "Unexpected error";

    Mockito.when(commandBus.execute(Mockito.any(UpdateUserCommandDto.class)))
        .thenThrow(new RuntimeException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(
            () ->
                userControllerImpl.updateUserByUuid(
                    user.getId(), UserUpdateDtoTestDataBuilder.builder().build().userUpdateDto()))
        .isInstanceOf(RuntimeException.class)
        .hasMessage(errorMessage);

    Mockito.verify(updateUserCommandDtoMapper)
        .toUpdateUserCommand(ArgumentMatchers.eq(user.getId()), Mockito.any());
    Mockito.verify(commandBus).execute(Mockito.any(UpdateUserCommandDto.class));
  }

  @Test
  void updateUserByUuidShouldPropagateResourceNotFoundExceptionWhenUserNotFound() {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = HttpStatus.NOT_FOUND.getReasonPhrase();

    Mockito.when(commandBus.execute(Mockito.any(UpdateUserCommandDto.class)))
        .thenThrow(new ResourceNotFoundException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(
            () ->
                userControllerImpl.updateUserByUuid(
                    user.getId(), UserUpdateDtoTestDataBuilder.builder().build().userUpdateDto()))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(errorMessage);

    Mockito.verify(updateUserCommandDtoMapper)
        .toUpdateUserCommand(ArgumentMatchers.eq(user.getId()), Mockito.any());
    Mockito.verify(commandBus).execute(Mockito.any(UpdateUserCommandDto.class));
  }

  @Test
  void patchUserByUuidShouldReturnOkWhenRequestIsValid() {
    final User user = UserTestDataBuilder.builder().build().user();

    Mockito.when(commandBus.execute(Mockito.any(PatchUserCommandDto.class))).thenReturn(user);

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
        .toPatchUserCommand(ArgumentMatchers.eq(user.getId()), Mockito.any());
    Mockito.verify(commandBus).execute(Mockito.any(PatchUserCommandDto.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void patchUserByUuidShouldPropagateResourceNotFoundExceptionWhenUserNotFound() {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = HttpStatus.NOT_FOUND.getReasonPhrase();

    Mockito.when(commandBus.execute(Mockito.any(PatchUserCommandDto.class)))
        .thenThrow(new ResourceNotFoundException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(
            () ->
                userControllerImpl.patchUserByUuid(
                    user.getId(), UserPatchDtoTestDataBuilder.builder().build().userPatchDto()))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(errorMessage);

    Mockito.verify(patchUserCommandDtoMapper)
        .toPatchUserCommand(ArgumentMatchers.eq(user.getId()), Mockito.any());
    Mockito.verify(commandBus).execute(Mockito.any(PatchUserCommandDto.class));
  }

  @Test
  void patchUserByUuidShouldPropagateIllegalArgumentExceptionWhenInvalidInput() {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = "Invalid patch";

    Mockito.when(commandBus.execute(Mockito.any(PatchUserCommandDto.class)))
        .thenThrow(new IllegalArgumentException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(
            () ->
                userControllerImpl.patchUserByUuid(
                    user.getId(), UserPatchDtoTestDataBuilder.builder().build().userPatchDto()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(errorMessage);

    Mockito.verify(patchUserCommandDtoMapper)
        .toPatchUserCommand(ArgumentMatchers.eq(user.getId()), Mockito.any());
    Mockito.verify(commandBus).execute(Mockito.any(PatchUserCommandDto.class));
  }

  @Test
  void patchUserByUuidShouldPropagateDomainExceptionWhenDomainExceptionOccurs() {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = "domain exception";

    Mockito.when(commandBus.execute(Mockito.any(PatchUserCommandDto.class)))
        .thenThrow(new DomainException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(
            () ->
                userControllerImpl.patchUserByUuid(
                    user.getId(), UserPatchDtoTestDataBuilder.builder().build().userPatchDto()))
        .isInstanceOf(DomainException.class)
        .hasMessage(errorMessage);

    Mockito.verify(patchUserCommandDtoMapper)
        .toPatchUserCommand(ArgumentMatchers.eq(user.getId()), Mockito.any());
    Mockito.verify(commandBus).execute(Mockito.any(PatchUserCommandDto.class));
  }

  @Test
  void patchUserByUuidShouldPropagateRuntimeExceptionWhenUnexpectedExceptionOccurs() {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = "Unexpected error";

    Mockito.when(commandBus.execute(Mockito.any(PatchUserCommandDto.class)))
        .thenThrow(new RuntimeException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(
            () ->
                userControllerImpl.patchUserByUuid(
                    user.getId(), UserPatchDtoTestDataBuilder.builder().build().userPatchDto()))
        .isInstanceOf(RuntimeException.class)
        .hasMessage(errorMessage);

    Mockito.verify(patchUserCommandDtoMapper)
        .toPatchUserCommand(ArgumentMatchers.eq(user.getId()), Mockito.any());
    Mockito.verify(commandBus).execute(Mockito.any(PatchUserCommandDto.class));
  }

  @Test
  void headUserByUuidShouldReturnOkWhenUserExists() {
    final User user = UserTestDataBuilder.builder().build().user();

    final ResponseEntity<Void> responseExpected = ResponseEntity.ok().build();

    Mockito.when(queryBus.execute(Mockito.any(UserExistsQueryDto.class))).thenReturn(null);

    final ResponseEntity<Void> response = userControllerImpl.headUserByUuid(user.getId());

    AssertionsForClassTypes.assertThat(response)
        .usingRecursiveComparison()
        .isEqualTo(responseExpected);

    Mockito.verify(userExistsQueryDtoMapper).toUserExistsQuery(user.getId());
    Mockito.verify(queryBus).execute(Mockito.any(UserExistsQueryDto.class));
  }

  @Test
  void headUserByUuidShouldPropagateResourceNotFoundExceptionWhenUserNotFound() {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = HttpStatus.NOT_FOUND.getReasonPhrase();

    Mockito.when(queryBus.execute(Mockito.any(UserExistsQueryDto.class)))
        .thenThrow(new ResourceNotFoundException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(
            () -> userControllerImpl.headUserByUuid(user.getId()))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(errorMessage);

    Mockito.verify(userExistsQueryDtoMapper).toUserExistsQuery(user.getId());
    Mockito.verify(queryBus).execute(Mockito.any(UserExistsQueryDto.class));
  }

  @Test
  void headUserByUuidShouldPropagateDomainExceptionWhenDomainExceptionOccurs() {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = "domain exception";

    Mockito.when(queryBus.execute(Mockito.any(UserExistsQueryDto.class)))
        .thenThrow(new DomainException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(
            () -> userControllerImpl.headUserByUuid(user.getId()))
        .isInstanceOf(DomainException.class)
        .hasMessage(errorMessage);

    Mockito.verify(userExistsQueryDtoMapper).toUserExistsQuery(user.getId());
    Mockito.verify(queryBus).execute(Mockito.any(UserExistsQueryDto.class));
  }

  @Test
  void headUserByUuidShouldPropagateRuntimeExceptionWhenUnexpectedExceptionOccurs() {
    final User user = UserTestDataBuilder.builder().build().user();
    final String errorMessage = "Unexpected error";

    Mockito.when(queryBus.execute(Mockito.any(UserExistsQueryDto.class)))
        .thenThrow(new RuntimeException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(
            () -> userControllerImpl.headUserByUuid(user.getId()))
        .isInstanceOf(RuntimeException.class)
        .hasMessage(errorMessage);

    Mockito.verify(userExistsQueryDtoMapper).toUserExistsQuery(user.getId());
    Mockito.verify(queryBus).execute(Mockito.any(UserExistsQueryDto.class));
  }

  @Test
  void getAllUsersShouldReturnOkWithCustomPagination() {
    final User user = UserTestDataBuilder.builder().build().user();
    final Integer page = 1;
    final Integer size = 10;
    final int totalPages = 5;
    final long totalElements = 50L;

    Mockito.when(queryBus.execute(Mockito.any(GetUsersFilteredQueryDto.class)))
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
    Mockito.verify(queryBus).execute(Mockito.any(GetUsersFilteredQueryDto.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void getAllUsersShouldPropagateInvalidValueExceptionWhenDomainExceptionOccurs() {
    final String errorMessage = ExceptionMessage.EMAIL_NO_ALLOWED_MESSAGE + "blocked@banned.com";

    Mockito.when(queryBus.execute(Mockito.any(GetUsersFilteredQueryDto.class)))
        .thenThrow(new InvalidValueException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(
            () -> userControllerImpl.getAllUsers(null, null, 0, 100))
        .isInstanceOf(InvalidValueException.class)
        .hasMessage(errorMessage);

    Mockito.verify(queryBus).execute(Mockito.any(GetUsersFilteredQueryDto.class));
  }

  @Test
  void getAllUsersShouldPropagateRuntimeExceptionWhenUnexpectedExceptionOccurs() {
    final String errorMessage = "Unexpected error";

    Mockito.when(queryBus.execute(Mockito.any(GetUsersFilteredQueryDto.class)))
        .thenThrow(new RuntimeException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(
            () -> userControllerImpl.getAllUsers(null, null, 0, 100))
        .isInstanceOf(RuntimeException.class)
        .hasMessage(errorMessage);

    Mockito.verify(queryBus).execute(Mockito.any(GetUsersFilteredQueryDto.class));
  }
}
