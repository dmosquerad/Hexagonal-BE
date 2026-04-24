package com.architecture.hexagonal.infrastructure.inbound.rest.resources.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.core.io.Resource;

@TestConfiguration
public class UserResponseResource {

  @Value("classpath:user/response/getAllUsers.json")
  public Resource getAllUsers;

  @Value("classpath:user/response/createUser.json")
  public Resource createUser;

  @Value("classpath:user/response/getUserByUuid.json")
  public Resource getUserByUuid;

  @Value("classpath:user/response/deleteUserByUuid.json")
  public Resource deleteUserByUuid;

  @Value("classpath:user/response/updateUserByUuid.json")
  public Resource updateUserByUuid;

  @Value("classpath:user/response/patchUserByUuid.json")
  public Resource patchUserByUuid;

}
