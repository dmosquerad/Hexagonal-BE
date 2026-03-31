package com.architecture.hexagonal.domain.service;

import com.architecture.hexagonal.domain.model.entity.User;
import java.util.Set;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailPolicy {

  private static final Set<String> blockedHost = Set.of("test");

  public static Boolean filterAllowedEmailHost(final User user) {
    final String host = user.getEmail().getHost();

    return !StringUtils.isBlank(host) && !blockedHost.contains(host);
  }

}


