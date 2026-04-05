package com.architecture.hexagonal.application.cqrs.query.request;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GetAllUserQuery {

  String host;
  Boolean blockEmail;
}