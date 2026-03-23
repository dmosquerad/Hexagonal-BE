package com.architecture.hexagonal.domain.data.vo;

import lombok.Builder;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

@Value
@Builder
public class EmailVo {

  String username;

  String host;

  String tld;

  public boolean canFormEmail() {
    return StringUtils.isNotBlank(this.username)
        && StringUtils.isNotBlank(this.host)
        && StringUtils.isNotBlank(this.tld);
  }

  public String getEmail() {
    if (!this.canFormEmail()) {
      return StringUtils.EMPTY;
    }

    return this.getUsername() + "@" + this.getHost() + "." + this.getTld();
  }

}
