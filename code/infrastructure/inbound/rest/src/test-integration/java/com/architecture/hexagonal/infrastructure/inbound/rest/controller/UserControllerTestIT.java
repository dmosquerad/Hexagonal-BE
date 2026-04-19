package com.architecture.hexagonal.infrastructure.inbound.rest.controller;

import com.architecture.hexagonal.application.cqrs.command.request.CreateUserCommand;
import com.architecture.hexagonal.application.cqrs.command.request.DeleteUserCommand;
import com.architecture.hexagonal.application.cqrs.command.request.PatchUserCommand;
import com.architecture.hexagonal.application.cqrs.command.request.UpdateUserCommand;
import com.architecture.hexagonal.application.cqrs.query.request.FindUserByUserIdQuery;
import com.architecture.hexagonal.application.cqrs.query.request.GetAllUserQuery;
import com.architecture.hexagonal.application.cqrs.query.request.UserExistsQuery;
import com.architecture.hexagonal.application.cqrs.command.dispatcher.CommandBus;
import com.architecture.hexagonal.application.cqrs.query.dispatcher.QueryBus;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.model.entity.User;
import com.architecture.hexagonal.infrastructure.inbound.rest.config.TestApplication;
import com.architecture.hexagonal.infrastructure.inbound.rest.dto.ResponseErrorDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UserCreateDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UserPatchDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UserResponseDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UserUpdateDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UsersResponseDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.*;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.dto.ResponseErrorDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.entity.UserTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.time.TestClock;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Clock;
import java.util.Collections;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@SpringBootTest(classes = UserController.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = TestApplication.class)
class UserControllerTestIT {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockitoSpyBean
  UserController userController;

  @MockitoBean
  QueryBus queryBus;

  @MockitoBean
  CommandBus commandBus;

  @MockitoSpyBean
  UserReadDtoMapper userReadDtoMapper;

  @MockitoSpyBean
  CreateUserCommandMapper createUserCommandMapper;

  @MockitoSpyBean
  DeleteUserCommandMapper deleteUserCommandMapper;

  @MockitoSpyBean
  UpdateUserCommandMapper updateUserCommandMapper;

  @MockitoSpyBean
  PatchUserCommandMapper patchUserCommandMapper;

  @MockitoSpyBean
  FindUserByUserIdQueryMapper findUserByUserIdQueryMapper;

  @MockitoSpyBean
  UserExistsQueryMapper userExistsQueryMapper;

  @MockitoSpyBean
  GetAllUserQueryMapper getAllUserQueryMapper;

  @MockitoSpyBean
  Clock clock;

  @Test
  void getAllUsers_shouldReturnOk_whenUsersExist() throws Exception {
    final UsersResponseDto getAllUsersResponse = objectMapper.readValue(
          new ClassPathResource("getAllUsers_response.json", UserControllerTestIT.class).getFile(),
          UsersResponseDto.class);

    final String host = "";
    final Boolean blockEmail = false;

    final User user = UserTestDataBuilder.builder().build().user();      

    Mockito.when(clock.instant()).thenReturn(TestClock.FIXED_INSTANT);
    Mockito.when(queryBus.execute(ArgumentMatchers.any(GetAllUserQuery.class)))
        .thenReturn(Collections.singleton(user));

    final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("host", host);
    params.add("blockEmail", String.valueOf(blockEmail));

    final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/users")
                .queryParams(params)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();

    AssertionsForClassTypes.assertThat(
            objectMapper.readValue(
                result.getResponse().getContentAsString(),
                UsersResponseDto.class))
        .usingRecursiveComparison()
        .isEqualTo(getAllUsersResponse);

    Mockito.verify(userController).getAllUsers(host, blockEmail);
    Mockito.verify(getAllUserQueryMapper).toGetAllUserQuery(host, blockEmail);
    Mockito.verify(queryBus).execute(ArgumentMatchers.any(GetAllUserQuery.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
  }

  @Test
  void createUser_shouldReturnOk_whenRequestIsValid() throws Exception {
    final UserCreateDto createUserRequest = objectMapper.readValue(
        new ClassPathResource("createUser_request.json", UserControllerTestIT.class).getFile(),
        UserCreateDto.class);
    final UserResponseDto createUserResponse = objectMapper.readValue(
          new ClassPathResource("createUser_response.json", UserControllerTestIT.class).getFile(),
          UserResponseDto.class);
    final User user = UserTestDataBuilder.builder().build().user();

    Mockito.when(clock.instant()).thenReturn(TestClock.FIXED_INSTANT);
    Mockito.when(commandBus.execute(ArgumentMatchers.any(CreateUserCommand.class)))
        .thenReturn(user);

    final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserRequest)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();

    AssertionsForClassTypes.assertThat(
            objectMapper.readValue(
                result.getResponse().getContentAsString(),
                UserResponseDto.class))
        .usingRecursiveComparison()
        .isEqualTo(createUserResponse);

    Mockito.verify(userController).createUser(createUserRequest);
    Mockito.verify(createUserCommandMapper).toCreateUserCommand(createUserRequest);
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(CreateUserCommand.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void getUserByUuid_shouldReturnOk_whenUserExists() throws Exception {
    final UserResponseDto getUserByUuidResponse = objectMapper.readValue(
          new ClassPathResource(
              "getUserByUuid_response.json",
              UserControllerTestIT.class).getFile(),
          UserResponseDto.class);

    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();

    Mockito.when(clock.instant()).thenReturn(TestClock.FIXED_INSTANT);
    Mockito.when(
        queryBus.execute(ArgumentMatchers.any(FindUserByUserIdQuery.class)))
        .thenReturn(user);

    final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/users/{userUuid}", user.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();

    AssertionsForClassTypes.assertThat(
            objectMapper.readValue(
                result.getResponse().getContentAsString(),
                UserResponseDto.class))
        .usingRecursiveComparison()
        .isEqualTo(getUserByUuidResponse);

    Mockito.verify(userController).getUserByUuid(user.getUserId());
    Mockito.verify(findUserByUserIdQueryMapper).toFindUserByUserIdQuery(user.getUserId());
    Mockito.verify(queryBus)
        .execute(ArgumentMatchers.any(FindUserByUserIdQuery.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void deleteUserByUuid_shouldReturnOk_whenUserExists() throws Exception {
    final UserResponseDto deleteUserByUuidResponse = objectMapper.readValue(
        new ClassPathResource(
            "deleteUserByUuid_response.json",
            UserControllerTestIT.class).getFile(),
        UserResponseDto.class);

    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();

    Mockito.when(clock.instant()).thenReturn(TestClock.FIXED_INSTANT);
    Mockito.when(commandBus.execute(ArgumentMatchers.any(DeleteUserCommand.class)))
        .thenReturn(user);

    final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.delete("/users/{userUuid}", user.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();

    AssertionsForClassTypes.assertThat(
            objectMapper.readValue(
                result.getResponse().getContentAsString(),
                UserResponseDto.class))
        .usingRecursiveComparison()
        .isEqualTo(deleteUserByUuidResponse);

    Mockito.verify(userController).deleteUserByUuid(user.getUserId());
    Mockito.verify(deleteUserCommandMapper).toDeleteUserCommand(user.getUserId());
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(DeleteUserCommand.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void updateUserByUuid_shouldReturnOk_whenRequestIsValid() throws Exception {
    final UserUpdateDto updateUserByUuidRequest = objectMapper.readValue(
        new ClassPathResource(
            "updateUserByUuid_request.json",
            UserControllerTestIT.class).getFile(),
        UserUpdateDto.class);
    final UserResponseDto updateUserByUuidResponse = objectMapper.readValue(
        new ClassPathResource(
            "updateUserByUuid_response.json",
            UserControllerTestIT.class).getFile(),
        UserResponseDto.class);

    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();

    Mockito.when(clock.instant()).thenReturn(TestClock.FIXED_INSTANT);
    Mockito.when(commandBus.execute(ArgumentMatchers.any(UpdateUserCommand.class)))
        .thenReturn(user);

    final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.put("/users/{userUuid}", user.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUserByUuidRequest)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();

    AssertionsForClassTypes.assertThat(
            objectMapper.readValue(
                result.getResponse().getContentAsString(),
                UserResponseDto.class))
        .usingRecursiveComparison()
        .isEqualTo(updateUserByUuidResponse);

    Mockito.verify(userController).updateUserByUuid(user.getUserId(), updateUserByUuidRequest);
    Mockito.verify(updateUserCommandMapper)
        .toUpdateUserCommand(ArgumentMatchers.eq(user.getUserId()), ArgumentMatchers.any());
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(UpdateUserCommand.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void patchUserByUuid_shouldReturnOk_whenRequestIsValid() throws Exception {
    final UserPatchDto patchUserByUuidRequest = objectMapper.readValue(
        new ClassPathResource("patchUserByUuid_request.json", UserControllerTestIT.class).getFile(),
        UserPatchDto.class);
    final UserResponseDto patchUserByUuidResponse = objectMapper.readValue(
        new ClassPathResource(
            "patchUserByUuid_response.json",
            UserControllerTestIT.class).getFile(),
        UserResponseDto.class);

    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();

    Mockito.when(clock.instant()).thenReturn(TestClock.FIXED_INSTANT);
    Mockito.when(commandBus.execute(ArgumentMatchers.any(PatchUserCommand.class)))
        .thenReturn(user);

    final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.patch("/users/{userUuid}", user.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patchUserByUuidRequest)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();

    AssertionsForClassTypes.assertThat(
            objectMapper.readValue(
                result.getResponse().getContentAsString(),
                UserResponseDto.class))
        .usingRecursiveComparison()
        .isEqualTo(patchUserByUuidResponse);

    Mockito.verify(userController).patchUserByUuid(user.getUserId(), patchUserByUuidRequest);
    Mockito.verify(patchUserCommandMapper)
        .toPatchUserCommand(ArgumentMatchers.eq(user.getUserId()), ArgumentMatchers.any());
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(PatchUserCommand.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void headUserByUuid_shouldReturnOk_whenUserExists() throws Exception {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();

    Mockito.when(queryBus.execute(ArgumentMatchers.any(UserExistsQuery.class)))
        .thenReturn(null);

    mockMvc.perform(
            MockMvcRequestBuilders.head("/users/{userUuid}", user.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();

    Mockito.verify(userExistsQueryMapper).toUserExistsQuery(user.getUserId());
    Mockito.verify(queryBus).execute(ArgumentMatchers.any(UserExistsQuery.class));
  }

  @Test
  void getUserByUuid_shouldReturn404_whenUserNotFound() throws Exception {
    final User user = UserTestDataBuilder.builder().build().user();
    final ResponseErrorDto expected = ResponseErrorDtoTestDataBuilder.builder()
        .status(HttpStatus.NOT_FOUND.value())
        .title(HttpStatus.NOT_FOUND.getReasonPhrase())
        .detail(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + user.getUserId())
        .build()
        .responseErrorDto();

    Mockito.when(clock.instant()).thenReturn(TestClock.FIXED_INSTANT);
    Mockito.when(queryBus.execute(ArgumentMatchers.any(FindUserByUserIdQuery.class)))
        .thenThrow(new ResourceNotFoundException(
            ExceptionMessage.NOT_FOUND_DATA_MESSAGE + user.getUserId()));

    final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/users/{userUuid}", user.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andReturn();

    AssertionsForClassTypes.assertThat(
            objectMapper.readValue(result.getResponse().getContentAsString(), ResponseErrorDto.class))
        .usingRecursiveComparison()
        .isEqualTo(expected);

    Mockito.verify(userController).getUserByUuid(user.getUserId());
    Mockito.verify(queryBus).execute(ArgumentMatchers.any(FindUserByUserIdQuery.class));
    Mockito.verify(clock).instant();
  }

  @Test
  void deleteUserByUuid_shouldReturn404_whenUserNotFound() throws Exception {
    final User user = UserTestDataBuilder.builder().build().user();
    final ResponseErrorDto expected = ResponseErrorDtoTestDataBuilder.builder()
        .status(HttpStatus.NOT_FOUND.value())
        .title(HttpStatus.NOT_FOUND.getReasonPhrase())
        .detail(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + user.getUserId())
        .build()
        .responseErrorDto();

    Mockito.when(clock.instant()).thenReturn(TestClock.FIXED_INSTANT);
    Mockito.when(commandBus.execute(ArgumentMatchers.any(DeleteUserCommand.class)))
        .thenThrow(new ResourceNotFoundException(
            ExceptionMessage.NOT_FOUND_DATA_MESSAGE + user.getUserId()));

    final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.delete("/users/{userUuid}", user.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andReturn();

    AssertionsForClassTypes.assertThat(
            objectMapper.readValue(result.getResponse().getContentAsString(), ResponseErrorDto.class))
        .usingRecursiveComparison()
        .isEqualTo(expected);

    Mockito.verify(userController).deleteUserByUuid(user.getUserId());
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(DeleteUserCommand.class));
    Mockito.verify(clock).instant();
  }

  @Test
  void createUser_shouldReturn400_whenEmailIsInvalid() throws Exception {
    final UserCreateDto createUserRequest = objectMapper.readValue(
        new ClassPathResource("createUser_request.json", UserControllerTestIT.class).getFile(),
        UserCreateDto.class);
    final ResponseErrorDto expected = ResponseErrorDtoTestDataBuilder.builder()
        .status(HttpStatus.BAD_REQUEST.value())
        .title(HttpStatus.BAD_REQUEST.getReasonPhrase())
        .detail(ExceptionMessage.INVALID_EMAIL_FORMAT)
        .build()
        .responseErrorDto();

    Mockito.when(clock.instant()).thenReturn(TestClock.FIXED_INSTANT);
    Mockito.when(commandBus.execute(ArgumentMatchers.any(CreateUserCommand.class)))
        .thenThrow(new IllegalArgumentException(ExceptionMessage.INVALID_EMAIL_FORMAT));

    final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserRequest)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andReturn();

    AssertionsForClassTypes.assertThat(
            objectMapper.readValue(result.getResponse().getContentAsString(), ResponseErrorDto.class))
        .usingRecursiveComparison()
        .isEqualTo(expected);

    Mockito.verify(userController).createUser(ArgumentMatchers.any());
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(CreateUserCommand.class));
    Mockito.verify(clock).instant();
  }

}
