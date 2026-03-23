package com.architecture.hexagonal.infrastructure.outbound.database.mapper.util;

import com.architecture.hexagonal.domain.data.vo.EmailVo;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Named;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ToEmailUtil {

  public static final String TO_EMAIL = "toEmail";

  @Named(TO_EMAIL)
  public static String toEmail(final EmailVo email) {
    if (Objects.isNull(email)) {
      return StringUtils.EMPTY;
    }

    return email.getEmail();
  }
}