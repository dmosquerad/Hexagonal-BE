package com.hexagonal.inbound.rest.controller;

import com.architecture.hexagonal.domain.data.UserDo;
import com.architecture.hexagonal.domain.input.command.CreateUserCommand;
import com.architecture.hexagonal.domain.input.query.FindUserByUserIdQuery;
import com.architecture.hexagonal.domain.port.in.CreateUserUseCasePort;
import com.architecture.hexagonal.domain.port.in.FindUserByUserIdUseCasePort;
import com.architecture.hexagonal.domain.port.in.GetAllUsersUseCasePort;
import com.architecture.hexagonal.inbound.rest.controller.UserController;
import com.architecture.hexagonal.inbound.rest.dto.UserDto;
import com.architecture.hexagonal.inbound.rest.dto.UserResponseDto;
import com.architecture.hexagonal.inbound.rest.dto.UsersResponseDto;
import com.architecture.hexagonal.inbound.rest.mapper.UserDtoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexagonal.inbound.rest.config.TestApplication;
import com.hexagonal.stub.InstantStub;
import com.hexagonal.stub.UserDoStub;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper = new ObjectMapper();

  @MockitoSpyBean
  private UserController userController;

  @MockitoBean
  GetAllUsersUseCasePort getAllUsersUseCasePort;

  @MockitoBean
  CreateUserUseCasePort createUserUseCasePort;

  @MockitoBean
  FindUserByUserIdUseCasePort findUserByUserIdUseCasePort;

  @MockitoSpyBean
  UserDtoMapper userDtoMapper;

  @MockitoSpyBean
  Clock clock;

  @Test
  void getAllUsers() throws Exception {
    final UserDto userDto = new UserDto();
    userDto.setUserId(UserDoStub.USERDO.getUserId());
    userDto.setEmail(UserDoStub.USERDO.getEmail());
    userDto.setName(UserDoStub.USERDO.getName());

    final UsersResponseDto userResponseDto = new UsersResponseDto();
    userResponseDto.setData(Set.of(userDto));
    userResponseDto.setDate(OffsetDateTime.ofInstant(InstantStub.INSTANT_DATE, ZoneOffset.UTC));
    userResponseDto.setStatus(HttpStatus.OK.value());

    Mockito.when(clock.instant()).thenReturn(InstantStub.INSTANT_DATE);
    Mockito.when(getAllUsersUseCasePort.execute())
        .thenReturn(Collections.singleton(UserDoStub.USERDO));

    final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();

    final UsersResponseDto response = objectMapper.readValue(result.getResponse().getContentAsString(), UsersResponseDto.class);

    Assertions.assertEquals(userResponseDto, response);

    Mockito.verify(userController).getAllUsers();
    Mockito.verify(getAllUsersUseCasePort).execute();
    Mockito.verify(userDtoMapper).toUserDtoSet(ArgumentMatchers.anySet());
  }

  @Test
  void createUser() throws Exception {
    final UserDto userDto = new UserDto();
    userDto.setEmail(UserDoStub.USERDO.getEmail());
    userDto.setName(UserDoStub.USERDO.getName());

    final UserDto userDtoResponse = new UserDto();
    userDtoResponse.setUserId(UserDoStub.USERDO.getUserId());
    userDtoResponse.setEmail(UserDoStub.USERDO.getEmail());
    userDtoResponse.setName(UserDoStub.USERDO.getName());

    final UserResponseDto userResponseDto = new UserResponseDto();
    userResponseDto.setData(userDtoResponse);
    userResponseDto.setDate(OffsetDateTime.ofInstant(InstantStub.INSTANT_DATE, ZoneOffset.UTC));
    userResponseDto.setStatus(HttpStatus.OK.value());

    Mockito.when(clock.instant()).thenReturn(InstantStub.INSTANT_DATE);
    Mockito.when(createUserUseCasePort.execute(ArgumentMatchers.any())).thenReturn(UserDoStub.USERDO);

    final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();

    final UserResponseDto response = objectMapper.readValue(result.getResponse().getContentAsString(), UserResponseDto.class);

    Assertions.assertEquals(userResponseDto, response);

    Mockito.verify(userController).createUser(ArgumentMatchers.any(UserDto.class));
    Mockito.verify(createUserUseCasePort).execute(ArgumentMatchers.any(CreateUserCommand.class));
    Mockito.verify(userDtoMapper).toUserDto(ArgumentMatchers.any(UserDo.class));
    Mockito.verify(clock).instant();
  }

  @Test
  void getUserByUuid() throws Exception {
    final UserDto userDto = new UserDto();
    userDto.setUserId(UserDoStub.USERDO.getUserId());
    userDto.setEmail(UserDoStub.USERDO.getEmail());
    userDto.setName(UserDoStub.USERDO.getName());

    final UserResponseDto userResponseDto = new UserResponseDto();
    userResponseDto.setData(userDto);
    userResponseDto.setDate(OffsetDateTime.ofInstant(InstantStub.INSTANT_DATE, ZoneOffset.UTC));
    userResponseDto.setStatus(HttpStatus.OK.value());

    Mockito.when(clock.instant()).thenReturn(InstantStub.INSTANT_DATE);
    Mockito.when(findUserByUserIdUseCasePort.execute(ArgumentMatchers.any(FindUserByUserIdQuery.class))).thenReturn(UserDoStub.USERDO);

    final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/users/{userUuid}", UserDoStub.USERDO.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();

    final UserResponseDto response = objectMapper.readValue(result.getResponse().getContentAsString(), UserResponseDto.class);

    Assertions.assertEquals(userResponseDto, response);

    Mockito.verify(userController).getUserByUuid(ArgumentMatchers.any(UUID.class));
    Mockito.verify(findUserByUserIdUseCasePort).execute(ArgumentMatchers.any(FindUserByUserIdQuery.class));
    Mockito.verify(userDtoMapper).toUserDto(ArgumentMatchers.any(UserDo.class));
    Mockito.verify(clock).instant();
  }
}
