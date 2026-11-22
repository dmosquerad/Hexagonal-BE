package com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.mapper.converter;

import com.architecture.hexagonal.contract.bus.model.common.Date;
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
    final Date date = new Date();
    date.setDate(dateVo.date());
    date.setZone(dateVo.zone().getId());
    return date;
  }
}
