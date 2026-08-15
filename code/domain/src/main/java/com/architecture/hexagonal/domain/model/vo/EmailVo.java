package com.architecture.hexagonal.domain.model.vo;

import com.architecture.hexagonal.domain.model.vo.predicate.EmailVoPredicate;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;

@Builder
public record EmailVo(String username, String host, String tld) {
  public String getEmail() {
    if (!EmailVoPredicate.CAN_FORM_EMAIL.test(this)) {
      return StringUtils.EMPTY;
    }

    return this.username + "@" + this.host() + "." + this.tld;
  }

  public String getDomain() {
    if (!EmailVoPredicate.CAN_FORM_DOMAIN.test(this)) {
      return StringUtils.EMPTY;
    }

    return this.host + "." + this.tld;
  }
}
