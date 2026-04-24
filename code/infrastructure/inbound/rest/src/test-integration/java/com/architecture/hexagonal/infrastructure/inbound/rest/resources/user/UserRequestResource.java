package com.architecture.hexagonal.infrastructure.inbound.rest.resources.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.core.io.Resource;

@TestConfiguration
public class UserRequestResource {

  @Value("classpath:user/request/createUser.json")
  public Resource createUser;

  @Value("classpath:user/request/updateUserByUuid.json")
  public Resource updateUserByUuid;

  @Value("classpath:user/request/patchUserByUuid.json")
  public Resource patchUserByUuid;

}
