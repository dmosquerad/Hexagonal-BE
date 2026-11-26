package com.architecture.hexagonal.domain.model.vo.email;

import lombok.Builder;
import org.apache.commons.lang3.StringUtils;

@Builder
public record EmailVo(String username, String host, String tld) {
  public String getEmail() {
    if (!canFormEmail()) {
      return StringUtils.EMPTY;
    }

    return this.username + "@" + this.host() + "." + this.tld;
  }

  public String getDomain() {
    if (!canFormDomain()) {
      return StringUtils.EMPTY;
    }

    return this.host + "." + this.tld;
  }

  public boolean canFormEmail() {
    return StringUtils.isNoneBlank(this.username, this.host, this.tld);
  }

  public boolean canFormDomain() {
    return StringUtils.isNoneBlank(this.host, this.tld);
  }
}
