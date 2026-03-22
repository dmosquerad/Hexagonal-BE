package com.architecture.hexagonal.infrastructure.inbound.rest.mapper.util;

import com.architecture.hexagonal.domain.data.vo.EmailVo;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mapstruct.Named;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ToEmailUtil {

  public static final String TO_EMAIL = "toEmail";

  @Named(TO_EMAIL)
  public static String toEmail(EmailVo email) {
    if (Objects.isNull(email)) {
      return StringUtils.EMPTY;
    }

    return email.getEmail();
  }
}