package com.architecture.hexagonal.infrastructure.outbound.database.postgresql.mapper.converter;

import com.architecture.hexagonal.domain.model.vo.email.EmailVo;
import com.architecture.hexagonal.domain.model.vo.email.factory.EmailVoFactory;
import java.util.Objects;
import lombok.experimental.UtilityClass;
import org.mapstruct.Named;

@UtilityClass
public final class EmailVoConverter {

  public static final String TO_EMAIL_VO = "toEmailVo";

  @Named(TO_EMAIL_VO)
  public static EmailVo toEmailVo(final String email) {
    if (Objects.isNull(email)) {
      return EmailVo.builder().build();
    }
    return EmailVoFactory.from(email);
  }
}
