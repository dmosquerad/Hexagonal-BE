package com.architecture.hexagonal.infrastructure.outbound.message.mapper.converter;

import com.architecture.hexagonal.domain.model.vo.EmailVo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Named;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailVoToStringConverter {

  public static final String TO_EMAIL = "toEmail";

  @Named(TO_EMAIL)
  public static String toEmail(final EmailVo email) {
    return Objects.nonNull(email) ? email.getEmail() : StringUtils.EMPTY;
  }
}
