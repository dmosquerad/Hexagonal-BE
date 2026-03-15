package com.architecture.hexagonal.infrastructure.inbound.rest.controller;

import com.architecture.hexagonal.domain.data.User;
import com.architecture.hexagonal.domain.input.command.CreateUserCommand;
import com.architecture.hexagonal.domain.input.query.FindUserByUserIdQuery;
import com.architecture.hexagonal.domain.port.in.CreateUserUseCasePort;
import com.architecture.hexagonal.domain.port.in.FindUserByUserIdUseCasePort;
import com.architecture.hexagonal.domain.port.in.GetAllUsersUseCasePort;
import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UserCreateDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UserResponseDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UsersResponseDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.UserReadDtoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.architecture.hexagonal.infrastructure.inbound.rest.config.TestApplication;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.time.TestClock;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.entity.UserTestDataBuilder;
import java.time.Clock;
import java.util.Collections;
import java.util.UUID;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
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

  @MockitoSpyBean
  UserReadDtoMapper userReadDtoMapper;

  @MockitoSpyBean
  Clock clock;

  @Test
  void getAllUsers() throws Exception {
    final UsersResponseDto getAllUsersResponse = objectMapper.readValue(
          new ClassPathResource("getAllUsers_response.json", UserControllerTestIT.class).getFile(),
          UsersResponseDto.class);

    Mockito.when(clock.instant()).thenReturn(TestClock.FIXED_INSTANT);
    Mockito.when(getAllUsersUseCasePort.execute()).thenReturn(Collections.singleton(
        UserTestDataBuilder
            .builder()
            .build()
            .user()));

    final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();

    AssertionsForClassTypes.assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), UsersResponseDto.class))
        .usingRecursiveComparison()
        .isEqualTo(getAllUsersResponse);

    Mockito.verify(userController).getAllUsers();
    Mockito.verify(getAllUsersUseCasePort).execute();
    Mockito.verify(userReadDtoMapper).toUserReadDto(ArgumentMatchers.any(User.class));
  }

  @Test
  void createUser() throws Exception {
    final UserCreateDto createUserRequest = objectMapper.readValue(
        new ClassPathResource("createUser_request.json", UserControllerTestIT.class).getFile(),
        UserCreateDto.class);
    final UserResponseDto createUserResponse = objectMapper.readValue(
          new ClassPathResource("createUser_response.json", UserControllerTestIT.class).getFile(),
          UserResponseDto.class);

    Mockito.when(clock.instant()).thenReturn(TestClock.FIXED_INSTANT);
    Mockito.when(createUserUseCasePort.execute(ArgumentMatchers.any()))
        .thenReturn(UserTestDataBuilder
            .builder()
            .build()
            .user());

    final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserRequest)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();

    AssertionsForClassTypes.assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), UserResponseDto.class))
        .usingRecursiveComparison()
        .isEqualTo(createUserResponse);

    Mockito.verify(userController).createUser(ArgumentMatchers.any(UserCreateDto.class));
    Mockito.verify(createUserUseCasePort).execute(ArgumentMatchers.any(CreateUserCommand.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(ArgumentMatchers.any(User.class));
    Mockito.verify(clock).instant();
  }

  @Test
  void getUserByUuid() throws Exception {
    final  UserResponseDto getUserByUuidResponse = objectMapper.readValue(
          new ClassPathResource("getUserByUuid_response.json", UserControllerTestIT.class).getFile(),
          UserResponseDto.class);

    Mockito.when(clock.instant()).thenReturn(TestClock.FIXED_INSTANT);
    Mockito.when(findUserByUserIdUseCasePort.execute(
        ArgumentMatchers.any(FindUserByUserIdQuery.class)))
        .thenReturn(UserTestDataBuilder
            .builder()
            .build()
            .user());

    final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/users/{userUuid}", UserTestDataBuilder
                    .builder()
                    .build()
                    .user().getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();

    AssertionsForClassTypes.assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), UserResponseDto.class))
        .usingRecursiveComparison()
        .isEqualTo(getUserByUuidResponse);

    Mockito.verify(userController).getUserByUuid(ArgumentMatchers.any(UUID.class));
    Mockito.verify(findUserByUserIdUseCasePort).execute(ArgumentMatchers.any(FindUserByUserIdQuery.class));
    Mockito.verify(userReadDtoMapper).toUserReadDto(ArgumentMatchers.any(User.class));
    Mockito.verify(clock).instant();
  }
}
