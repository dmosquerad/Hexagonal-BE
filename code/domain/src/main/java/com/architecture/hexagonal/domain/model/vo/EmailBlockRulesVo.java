package com.architecture.hexagonal.domain.model.vo;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EmailBlockRulesVo {

  List<String> email;
  List<String> host;
  List<String> tld;
  List<String> domain;
  List<String> username;
}
