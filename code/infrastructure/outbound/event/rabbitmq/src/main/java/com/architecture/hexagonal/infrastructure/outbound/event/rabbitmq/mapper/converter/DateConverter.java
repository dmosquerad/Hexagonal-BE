package com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.mapper.converter;

import com.architecture.hexagonal.domain.model.vo.time.DateVo;
import com.architecture.hexagonal.domain.model.vo.time.factory.DateVoFactory;
import java.time.OffsetDateTime;
import lombok.experimental.UtilityClass;
import org.mapstruct.Named;

@UtilityClass
public final class DateConverter {

  public static final String TO_DATE_VO = "toDateVo";

  @Named(TO_DATE_VO)
  public static DateVo toDateVo(final OffsetDateTime dateTime) {
    return DateVoFactory.from(dateTime);
  }
}
