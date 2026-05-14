package com.architecture.hexagonal.domain.model.vo;

import java.util.Set;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EmailBlockRulesVo {

  Set<String> email;
  Set<String> host;
  Set<String> tld;
  Set<String> domain;
  Set<String> username;
}
