package com.architecture.hexagonal.domain.model.vo;

import com.architecture.hexagonal.domain.model.vo.predicate.EmailVoPredicate;
import lombok.Builder;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

@Value
@Builder
public class EmailVo {
  String username;
  String host;
  String tld;

  public String getEmail() {
    if (!EmailVoPredicate.CAN_FORM_EMAIL.test(this)) {
      return StringUtils.EMPTY;
    }

    return this.getUsername() + "@" + this.getHost() + "." + this.getTld();
  }

  public String getDomain() {
    if (!EmailVoPredicate.CAN_FORM_DOMAIN.test(this)) {
      return StringUtils.EMPTY;
    }

    return this.getHost() + "." + this.getTld();
  }

}
