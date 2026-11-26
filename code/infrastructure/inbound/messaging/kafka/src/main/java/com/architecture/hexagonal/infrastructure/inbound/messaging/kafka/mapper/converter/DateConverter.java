package com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.mapper.converter;

import com.architecture.hexagonal.application.model.vo.time.Date;
import com.architecture.hexagonal.domain.model.vo.time.DateVo;
import com.architecture.hexagonal.domain.model.vo.time.factory.DateVoFactory;
import java.time.OffsetDateTime;
import java.util.Objects;
import lombok.experimental.UtilityClass;
import org.mapstruct.Named;

@UtilityClass
public class DateConverter {

  public static final String TO_DATE = "toDate";

  @Named(TO_DATE)
  public static Date toDate(final OffsetDateTime dateTime) {
    if (Objects.isNull(dateTime)) {
      return null;
    }

    final DateVo dateVo = DateVoFactory.from(dateTime);
    return Date.builder().date(dateVo.date()).zone(dateVo.zone().getId()).build();
  }
}
