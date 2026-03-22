package com.architecture.hexagonal.domain.data.vo.factory;

import com.architecture.hexagonal.domain.data.vo.EmailVo;
import java.util.Objects;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailVoFactory {

  public static Optional<EmailVo> from(String email) {
    if (Objects.isNull(email) || !email.contains("@")) {
      return Optional.empty();
    }

    int atIndex = email.indexOf('@');
    String username = email.substring(0, atIndex);
    String domain = email.substring(atIndex + 1);

    return Optional.of(EmailVo.builder()
        .username(username)
        .domain(domain)
        .build());
  }

}
