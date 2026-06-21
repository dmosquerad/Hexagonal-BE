package com.architecture.hexagonal.infrastructure.inbound.rest.controller;

import com.architecture.hexagonal.domain.model.pagination.PaginationResult;
import com.architecture.hexagonal.infrastructure.contract.orchestration.generated.user.*;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.command.CommandBus;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.query.QueryBus;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.user.*;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.pagination.PaginationTestDataBuilder;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.infrastructure.inbound.rest.config.TestApplication;
import com.architecture.hexagonal.infrastructure.contract.rest.user.server.dto.ResponseErrorDto;
import com.architecture.hexagonal.infrastructure.contract.rest.user.server.dto.UserCreateDto;
import com.architecture.hexagonal.infrastructure.contract.rest.user.server.dto.UserPatchDto;
import com.architecture.hexagonal.infrastructure.contract.rest.user.server.dto.UserResponseDto;
import com.architecture.hexagonal.infrastructure.contract.rest.user.server.dto.UserUpdateDto;
import com.architecture.hexagonal.infrastructure.contract.rest.user.server.dto.ResponsePaginationDto;
import com.architecture.hexagonal.infrastructure.contract.rest.user.server.dto.UsersResponseDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.resources.user.UserRequestResource;
import com.architecture.hexagonal.infrastructure.inbound.rest.resources.user.UserResponseResource;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.dto.ResponseErrorDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.dto.UsersResponseDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.aggregate.UserTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.time.TestClock;
import java.time.Clock;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(classes = UserControllerImpl.class)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ContextConfiguration(classes = TestApplication.class)
@Import({UserRequestResource.class, UserResponseResource.class})
class UserControllerImplTestIT {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  JacksonTester<UsersResponseDto> usersResponseDtoJson;

  @Autowired
  JacksonTester<UserResponseDto> userResponseDtoJson;

  @Autowired
  JacksonTester<UserCreateDto> userCreateDtoJson;

  @Autowired
  JacksonTester<UserUpdateDto> userUpdateDtoJson;

  @Autowired
  JacksonTester<UserPatchDto> userPatchDtoJson;

  @Autowired
  JacksonTester<ResponseErrorDto> responseErrorDtoJson;

  @Autowired
  UserRequestResource userRequestResource;

  @Autowired
  UserResponseResource userResponseResource;

  @MockitoSpyBean
  UserControllerImpl userControllerImpl;

  @MockitoBean
  QueryBus queryBus;

  @MockitoBean
  CommandBus commandBus;

  @MockitoSpyBean
  UserReadDtoMapper userReadDtoMapper;

  @MockitoSpyBean
  CreateUserCommandDtoMapper createUserCommandDtoMapper;

  @MockitoSpyBean
  DeleteUserCommandDtoMapper deleteUserCommandDtoMapper;

  @MockitoSpyBean
  UpdateUserCommandDtoMapper updateUserCommandDtoMapper;

  @MockitoSpyBean
  PatchUserCommandDtoMapper patchUserCommandDtoMapper;

  @MockitoSpyBean
  FindUserByUserIdQueryDtoMapper findUserByUserIdQueryDtoMapper;

  @MockitoSpyBean
  UserExistsQueryDtoMapper userExistsQueryDtoMapper;

  @MockitoSpyBean
  GetAllUserQueryDtoMapper getAllUserQueryDtoMapper;

  @MockitoSpyBean
  Clock clock;

  @Test
  void getAllUsers_shouldReturnOk_whenUsersExist() throws Exception {
    final UsersResponseDto getAllUsersResponse = usersResponseDtoJson
        .readObject(userResponseResource.getAllUsers);

    final String host = "";
    final Boolean blockEmail = false;

    final User user = UserTestDataBuilder.builder().build().user();

    Mockito.when(clock.instant()).thenReturn(TestClock.FIXED_INSTANT);
    Mockito.when(queryBus.execute(ArgumentMatchers.any(GetUsersFilteredQueryDto.class)))
        .thenReturn(PaginationResult.<User>builder()
            .data(Collections.singletonList(user))
            .page(0)
            .size(100)
            .totalElements(1L)
            .totalPages(1)
            .build());

    mockMvc.perform(
            MockMvcRequestBuilders.get("/users")
                .queryParam("host",host)
                .queryParam("blockEmail", String.valueOf(blockEmail))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content()
            .json(usersResponseDtoJson.write(getAllUsersResponse).getJson()));

    Mockito.verify(userControllerImpl).getAllUsers(host, blockEmail, 0, 100);
    Mockito.verify(getAllUserQueryDtoMapper).toGetAllUserQuery(host, blockEmail, PaginationTestDataBuilder.builder().build().pagination());
    Mockito.verify(queryBus).execute(ArgumentMatchers.any(GetUsersFilteredQueryDto.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
  }

  @Test
  void createUser_shouldReturnOk_whenRequestIsValid() throws Exception {
    final UserCreateDto createUserRequest = userCreateDtoJson
        .readObject(userRequestResource.createUser);
    final UserResponseDto createUserResponse = userResponseDtoJson
        .readObject(userResponseResource.createUser);
    final User user = UserTestDataBuilder.builder().build().user();

    Mockito.when(clock.instant()).thenReturn(TestClock.FIXED_INSTANT);
    Mockito.when(commandBus.execute(ArgumentMatchers.any(CreateUserCommandDto.class)))
        .thenReturn(user);

    mockMvc.perform(
            MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(userCreateDtoJson.write(createUserRequest).getJson()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content()
            .json(userResponseDtoJson.write(createUserResponse).getJson()));

    Mockito.verify(userControllerImpl).createUser(createUserRequest);
    Mockito.verify(createUserCommandDtoMapper).toCreateUserCommand(createUserRequest);
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(CreateUserCommandDto.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void getAllUsers_shouldReturnOk_whenCustomPaginationIsProvided() throws Exception {
    final Integer page = 1;
    final Integer size = 10;
    final long totalElements = 50L;
    final int totalPages = 5;

    final User user = UserTestDataBuilder.builder().build().user();

    Mockito.when(clock.instant()).thenReturn(TestClock.FIXED_INSTANT);
    Mockito.when(queryBus.execute(ArgumentMatchers.any(GetUsersFilteredQueryDto.class)))
        .thenReturn(PaginationResult.<User>builder()
            .data(Collections.singletonList(user))
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

    mockMvc.perform(
            MockMvcRequestBuilders.get("/users")
                .queryParam("page", String.valueOf(page))
                .queryParam("size", String.valueOf(size))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content()
            .json(usersResponseDtoJson.write(expectedBody).getJson()));
    Mockito.verify(userControllerImpl).getAllUsers(null, null, page, size);
    Mockito.verify(getAllUserQueryDtoMapper).toGetAllUserQuery(
        null, null, PaginationTestDataBuilder.builder().page(page).size(size).build().pagination());
    Mockito.verify(queryBus).execute(ArgumentMatchers.any(GetUsersFilteredQueryDto.class));
  }

  @Test
  void getUserByUuid_shouldReturnOk_whenUserExists() throws Exception {
    final UserResponseDto getUserByUuidResponse = userResponseDtoJson
        .readObject(userResponseResource.getUserByUuid);

    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();

    Mockito.when(clock.instant()).thenReturn(TestClock.FIXED_INSTANT);
    Mockito.when(
        queryBus.execute(ArgumentMatchers.any(FindUserByUserIdQueryDto.class)))
        .thenReturn(user);

    mockMvc.perform(
            MockMvcRequestBuilders.get("/users/{userUuid}", user.getUser().getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content()
            .json(userResponseDtoJson.write(getUserByUuidResponse).getJson()));

    Mockito.verify(userControllerImpl).getUserByUuid(user.getUser().getUserId());
    Mockito.verify(findUserByUserIdQueryDtoMapper).toFindUserByUserIdQuery(user.getUser().getUserId());
    Mockito.verify(queryBus)
        .execute(ArgumentMatchers.any(FindUserByUserIdQueryDto.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void deleteUserByUuid_shouldReturnOk_whenUserExists() throws Exception {
    final UserResponseDto deleteUserByUuidResponse = userResponseDtoJson
        .readObject(userResponseResource.deleteUserByUuid);

    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();

    Mockito.when(clock.instant()).thenReturn(TestClock.FIXED_INSTANT);
    Mockito.when(commandBus.execute(ArgumentMatchers.any(DeleteUserCommandDto.class)))
        .thenReturn(user);

    mockMvc.perform(
            MockMvcRequestBuilders.delete("/users/{userUuid}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content()
            .json(userResponseDtoJson.write(deleteUserByUuidResponse).getJson()));

    Mockito.verify(userControllerImpl).deleteUserByUuid(user.getId());
    Mockito.verify(deleteUserCommandDtoMapper).toDeleteUserCommand(user.getId());
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(DeleteUserCommandDto.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void updateUserByUuid_shouldReturnOk_whenRequestIsValid() throws Exception {
    final UserUpdateDto updateUserByUuidRequest = userUpdateDtoJson
        .readObject(userRequestResource.updateUserByUuid);
    final UserResponseDto updateUserByUuidResponse = userResponseDtoJson
        .readObject(userResponseResource.updateUserByUuid);

    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();

    Mockito.when(clock.instant()).thenReturn(TestClock.FIXED_INSTANT);
    Mockito.when(commandBus.execute(ArgumentMatchers.any(UpdateUserCommandDto.class)))
        .thenReturn(user);

    mockMvc.perform(
            MockMvcRequestBuilders.put("/users/{userUuid}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(userUpdateDtoJson.write(updateUserByUuidRequest).getJson()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content()
            .json(userResponseDtoJson.write(updateUserByUuidResponse).getJson()));

    Mockito.verify(userControllerImpl).updateUserByUuid(user.getId(), updateUserByUuidRequest);
    Mockito.verify(updateUserCommandDtoMapper)
        .toUpdateUserCommand(ArgumentMatchers.eq(user.getId()), ArgumentMatchers.any());
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(UpdateUserCommandDto.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void patchUserByUuid_shouldReturnOk_whenRequestIsValid() throws Exception {
    final UserPatchDto patchUserByUuidRequest = userPatchDtoJson
        .readObject(userRequestResource.patchUserByUuid);
    final UserResponseDto patchUserByUuidResponse = userResponseDtoJson
        .readObject(userResponseResource.patchUserByUuid);

    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();

    Mockito.when(clock.instant()).thenReturn(TestClock.FIXED_INSTANT);
    Mockito.when(commandBus.execute(ArgumentMatchers.any(PatchUserCommandDto.class)))
        .thenReturn(user);

    mockMvc.perform(
            MockMvcRequestBuilders.patch("/users/{userUuid}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(userPatchDtoJson.write(patchUserByUuidRequest).getJson()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content()
            .json(userResponseDtoJson.write(patchUserByUuidResponse).getJson()));

    Mockito.verify(userControllerImpl).patchUserByUuid(user.getId(), patchUserByUuidRequest);
    Mockito.verify(patchUserCommandDtoMapper)
        .toPatchUserCommand(ArgumentMatchers.eq(user.getId()), ArgumentMatchers.any());
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(PatchUserCommandDto.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void headUserByUuid_shouldReturnOk_whenUserExists() throws Exception {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();

    Mockito.when(queryBus.execute(ArgumentMatchers.any(UserExistsQueryDto.class)))
        .thenReturn(null);

    mockMvc.perform(
            MockMvcRequestBuilders.head("/users/{userUuid}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string(""));

    Mockito.verify(userExistsQueryDtoMapper).toUserExistsQuery(user.getId());
    Mockito.verify(queryBus).execute(ArgumentMatchers.any(UserExistsQueryDto.class));
  }

  @Test
  void getUserByUuid_shouldReturn404_whenUserNotFound() throws Exception {
    final User user = UserTestDataBuilder.builder().build().user();
    final ResponseErrorDto expected = ResponseErrorDtoTestDataBuilder.builder()
        .status(HttpStatus.NOT_FOUND.value())
        .title(HttpStatus.NOT_FOUND.getReasonPhrase())
        .detail(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + user.getId())
        .build()
        .responseErrorDto();

    Mockito.when(clock.instant()).thenReturn(TestClock.FIXED_INSTANT);
    Mockito.when(queryBus.execute(ArgumentMatchers.any(FindUserByUserIdQueryDto.class)))
        .thenThrow(new ResourceNotFoundException(
            ExceptionMessage.NOT_FOUND_DATA_MESSAGE + user.getId()));

    mockMvc.perform(
            MockMvcRequestBuilders.get("/users/{userUuid}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.content()
            .json(responseErrorDtoJson.write(expected).getJson()));

    Mockito.verify(userControllerImpl).getUserByUuid(user.getId());
    Mockito.verify(queryBus).execute(ArgumentMatchers.any(FindUserByUserIdQueryDto.class));
    Mockito.verify(clock).instant();
  }

  @Test
  void deleteUserByUuid_shouldReturn404_whenUserNotFound() throws Exception {
    final User user = UserTestDataBuilder.builder().build().user();
    final ResponseErrorDto expected = ResponseErrorDtoTestDataBuilder.builder()
        .status(HttpStatus.NOT_FOUND.value())
        .title(HttpStatus.NOT_FOUND.getReasonPhrase())
        .detail(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + user.getId())
        .build()
        .responseErrorDto();

    Mockito.when(clock.instant()).thenReturn(TestClock.FIXED_INSTANT);
    Mockito.when(commandBus.execute(ArgumentMatchers.any(DeleteUserCommandDto.class)))
        .thenThrow(new ResourceNotFoundException(
            ExceptionMessage.NOT_FOUND_DATA_MESSAGE + user.getId()));

    mockMvc.perform(
            MockMvcRequestBuilders.delete("/users/{userUuid}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.content()
            .json(responseErrorDtoJson.write(expected).getJson()));

    Mockito.verify(userControllerImpl).deleteUserByUuid(user.getId());
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(DeleteUserCommandDto.class));
    Mockito.verify(clock).instant();
  }

  @Test
  void createUser_shouldReturn400_whenEmailIsInvalid() throws Exception {
    final UserCreateDto createUserRequest = userCreateDtoJson
        .readObject(userRequestResource.createUser);
    final ResponseErrorDto expected = ResponseErrorDtoTestDataBuilder.builder()
        .status(HttpStatus.BAD_REQUEST.value())
        .title(HttpStatus.BAD_REQUEST.getReasonPhrase())
        .detail(ExceptionMessage.INVALID_EMAIL_FORMAT)
        .build()
        .responseErrorDto();

    Mockito.when(clock.instant()).thenReturn(TestClock.FIXED_INSTANT);
    Mockito.when(commandBus.execute(ArgumentMatchers.any(CreateUserCommandDto.class)))
        .thenThrow(new IllegalArgumentException(ExceptionMessage.INVALID_EMAIL_FORMAT));

    mockMvc.perform(
            MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(userCreateDtoJson.write(createUserRequest).getJson()))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content()
            .json(responseErrorDtoJson.write(expected).getJson()));

    Mockito.verify(userControllerImpl).createUser(ArgumentMatchers.any());
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(CreateUserCommandDto.class));
    Mockito.verify(clock).instant();
  }

}
