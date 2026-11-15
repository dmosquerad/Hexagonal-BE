package com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.mapper.converter;

import com.architecture.hexagonal.domain.model.vo.date.DateVo;
import java.time.OffsetDateTime;
import java.util.Objects;
import lombok.experimental.UtilityClass;
import org.mapstruct.Named;

@UtilityClass
public class DateVoConverter {

  public static final String TO_DATE_VO = "toDateVo";
  public static final String TO_OFFSET_DATE_TIME = "toOffsetDateTime";

  @Named(TO_DATE_VO)
  public static DateVo toDateVo(final OffsetDateTime offsetDateTime) {
    if (Objects.isNull(offsetDateTime)) {
      return null;
    }

    return DateVo.builder()
        .date(offsetDateTime.toInstant())
        .zoneId(offsetDateTime.getOffset())
        .build();
  }

  @Named(TO_OFFSET_DATE_TIME)
  public static OffsetDateTime toOffsetDateTime(final DateVo dateVo) {
    if (Objects.isNull(dateVo)) {
      return null;
    }

    return OffsetDateTime.ofInstant(dateVo.date(), dateVo.zoneId());
  }
}
