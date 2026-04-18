package com.architecture.hexagonal.infrastructure.outbound.database.mapper.converter;

import com.architecture.hexagonal.domain.model.vo.EmailVo;
import com.architecture.hexagonal.domain.model.vo.factory.EmailVoFactory;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mapstruct.Named;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
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