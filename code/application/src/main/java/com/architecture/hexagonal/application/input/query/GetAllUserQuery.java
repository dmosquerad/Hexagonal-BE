package com.architecture.hexagonal.application.input.query;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GetAllUserQuery {

  String host;
  Boolean blockEmail;
}