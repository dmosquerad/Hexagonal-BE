package com.architecture.hexagonal.infrastructure.inbound.rest.controller;

import com.architecture.hexagonal.domain.data.User;
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
import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UserCreateDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UserPatchDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UserResponseDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UserUpdateDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UsersResponseDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.UserReadDtoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.architecture.hexagonal.infrastructure.inbound.rest.config.TestApplication;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.time.TestClock;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.entity.UserTestDataBuilder;
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

@SpringBootTest(classes = {UserController.class})
@AutoConfigureMockMvc
@ContextConfiguration(classes = TestApplication.class)
class UserControllerTestIT {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

  @MockitoSpyBean
  UserController userController;

  @MockitoBean
  GetAllUsersUseCasePort getAllUsersUseCasePort;

  @MockitoBean
  CreateUserUseCasePort createUserUseCasePort;

  @MockitoBean
  FindUserByUserIdUseCasePort findUserByUserIdUseCasePort;

  @MockitoBean
  DeleteUserUseCasePort deleteUserUseCasePort;

  @MockitoBean
  UpdateUserUseCasePort updateUserUseCasePort;

  @MockitoBean
  PatchUserUseCasePort patchUserUseCasePort;

  @MockitoBean
  UserExistsUseCasePort userExistsUseCasePort;

  @MockitoSpyBean
  UserReadDtoMapper userReadDtoMapper;

  @MockitoSpyBean
  Clock clock;

  @Test
  void getAllUsers() throws Exception {
    final UsersResponseDto getAllUsersResponse = objectMapper.readValue(
          new ClassPathResource("getAllUsers_response.json", UserControllerTestIT.class).getFile(),
          UsersResponseDto.class);

    final User user = UserTestDataBuilder.builder().build().user();      

    Mockito.when(clock.instant()).thenReturn(TestClock.FIXED_INSTANT);
    Mockito.when(getAllUsersUseCasePort.execute()).thenReturn(Collections.singleton(user));

    final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/users")
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

    Mockito.verify(userController).getAllUsers();
    Mockito.verify(getAllUsersUseCasePort).execute();
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
  }

  @Test
  void createUser() throws Exception {
    final UserCreateDto createUserRequest = objectMapper.readValue(
        new ClassPathResource("createUser_request.json", UserControllerTestIT.class).getFile(),
        UserCreateDto.class);
    final UserResponseDto createUserResponse = objectMapper.readValue(
          new ClassPathResource("createUser_response.json", UserControllerTestIT.class).getFile(),
          UserResponseDto.class);
    final User user = UserTestDataBuilder.builder().build().user();

    Mockito.when(clock.instant()).thenReturn(TestClock.FIXED_INSTANT);
    Mockito.when(createUserUseCasePort.execute(ArgumentMatchers.any(CreateUserCommand.class)))
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
    Mockito.verify(createUserUseCasePort).execute(ArgumentMatchers.any(CreateUserCommand.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void getUserByUuid() throws Exception {
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
            findUserByUserIdUseCasePort.execute(ArgumentMatchers.any(FindUserByUserIdQuery.class)))
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
    Mockito.verify(findUserByUserIdUseCasePort)
        .execute(ArgumentMatchers.any(FindUserByUserIdQuery.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void deleteUserByUuid() throws Exception {
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
    Mockito.when(deleteUserUseCasePort.execute(ArgumentMatchers.any(DeleteUserCommand.class)))
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
    Mockito.verify(deleteUserUseCasePort).execute(ArgumentMatchers.any(DeleteUserCommand.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void updateUserByUuid() throws Exception {
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
    Mockito.when(updateUserUseCasePort.execute(ArgumentMatchers.any(UpdateUserCommand.class)))
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
    Mockito.verify(updateUserUseCasePort).execute(ArgumentMatchers.any(UpdateUserCommand.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void patchUserByUuid() throws Exception {
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
    Mockito.when(patchUserUseCasePort.execute(ArgumentMatchers.any(PatchUserCommand.class)))
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
    Mockito.verify(patchUserUseCasePort).execute(ArgumentMatchers.any(PatchUserCommand.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(user);
    Mockito.verify(clock).instant();
  }

  @Test
  void headUserByUuid() throws Exception {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();

    Mockito.doNothing()
        .when(userExistsUseCasePort)
        .execute(ArgumentMatchers.any(UserExistsQuery.class));

    mockMvc.perform(
            MockMvcRequestBuilders.head("/users/{userUuid}", user.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();

    Mockito.verify(userExistsUseCasePort).execute(ArgumentMatchers.any(UserExistsQuery.class));
  }

}
