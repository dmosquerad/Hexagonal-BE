package com.architecture.hexagonal.application.mapper.converter;

import com.architecture.hexagonal.domain.model.vo.email.EmailVo;
import com.architecture.hexagonal.domain.model.vo.email.factory.EmailVoFactory;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Named;

@UtilityClass
public class EmailVoConverter {
  public static final String TO_EMAIL = "toEmail";
  public static final String TO_EMAIL_VO = "toEmailVo";
  public static final String TO_OPTIONAL_EMAIL_VO = "toOptionalEmailVo";

  @Named(TO_EMAIL)
  public static String toEmail(final EmailVo email) {
    return email.getEmail();
  }

  @Named(TO_EMAIL_VO)
  public static EmailVo toEmailVo(final String email) {
    return EmailVoFactory.from(email);
  }

  @Named(TO_OPTIONAL_EMAIL_VO)
  public static EmailVo toOptionalEmailVo(final String email) {
    return StringUtils.isBlank(email) ? null : EmailVoFactory.from(email);
  }
}
