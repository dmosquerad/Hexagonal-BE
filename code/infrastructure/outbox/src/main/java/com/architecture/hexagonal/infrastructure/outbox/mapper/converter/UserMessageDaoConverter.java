package com.architecture.hexagonal.infrastructure.outbox.mapper.converter;

import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserData;
import com.architecture.hexagonal.domain.model.vo.date.DateVo;
import java.time.OffsetDateTime;
import java.util.Objects;
import lombok.experimental.UtilityClass;
import org.mapstruct.Named;
import tools.jackson.databind.ObjectMapper;

@UtilityClass
public class UserMessageDaoConverter {

  public static final String TO_USER_DATA = "toUserData";
  public static final String TO_OFFSET_DATE_TIME = "toOffsetDateTime";

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @Named(TO_OFFSET_DATE_TIME)
  public static OffsetDateTime toOffsetDateTime(final DateVo date) {
    return Objects.isNull(date) ? null : OffsetDateTime.ofInstant(date.date(), date.zoneId());
  }

  @Named(TO_USER_DATA)
  public static UserData toUserData(final Object object) {
    return OBJECT_MAPPER.convertValue(object, UserData.class);
  }
}
