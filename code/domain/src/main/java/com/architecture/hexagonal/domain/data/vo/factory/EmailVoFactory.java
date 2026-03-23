package com.architecture.hexagonal.domain.data.vo.factory;

import com.architecture.hexagonal.domain.data.vo.EmailVo;
import java.util.Arrays;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailVoFactory {

  public static EmailVo from(final String email) {
    if (StringUtils.isBlank(email)) {
      return EmailVo.builder().build();
    }

    final int atIndex = email.indexOf('@');
    if (atIndex <= 0 || atIndex != email.lastIndexOf('@') || atIndex == email.length() - 1) {
      return EmailVo.builder().build();
    }

    final String username = email.substring(0, atIndex);
    final String rightPart = email.substring(atIndex + 1);
    final String[] hostParts = rightPart.split("\\.", -1);

    if (StringUtils.isBlank(username)
        || hostParts.length < 2
        || Arrays.stream(hostParts).anyMatch(StringUtils::isBlank)) {
      return EmailVo.builder().build();
    }

    final String host = String.join(".", Arrays.copyOf(hostParts, hostParts.length - 1));
    final String tld = hostParts[hostParts.length - 1];

    return EmailVo.builder()
        .username(username)
        .host(host)
        .tld(tld)
        .build();
  }

}
