package com.architecture.hexagonal.infrastructure.inbound.rest.mapper.converter;

import com.architecture.hexagonal.domain.model.vo.email.EmailVo;
import java.util.Objects;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Named;

@UtilityClass
public final class EmailConverter {

  public static final String TO_EMAIL = "toEmail";

  @Named(TO_EMAIL)
  public static String toEmail(final EmailVo email) {
    if (Objects.isNull(email)) {
      return StringUtils.EMPTY;
    }

    return email.getEmail();
  }
}
