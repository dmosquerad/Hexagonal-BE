package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.converter;

import com.architecture.hexagonal.domain.model.vo.date.DateVo;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.date.DateVoDao;
import java.time.ZoneId;
import java.util.Objects;
import lombok.experimental.UtilityClass;
import org.mapstruct.Named;

@UtilityClass
public class DateVoMongoConverter {

  public static final String TO_DATE_VO_DAO = "toDateVoDao";
  public static final String TO_DATE_VO = "toDateVo";

  @Named(TO_DATE_VO_DAO)
  public static DateVoDao toDateVoDao(final DateVo dateVo) {
    if (Objects.isNull(dateVo)) {
      return null;
    }

    final DateVoDao dateVoDao = new DateVoDao();
    dateVoDao.setDate(dateVo.date());
    dateVoDao.setZoneId(dateVo.zoneId().getId());
    return dateVoDao;
  }

  @Named(TO_DATE_VO)
  public static DateVo toDateVo(final DateVoDao dateVoDao) {
    if (Objects.isNull(dateVoDao)) {
      return null;
    }

    return DateVo.builder()
        .date(dateVoDao.getDate())
        .zoneId(ZoneId.of(dateVoDao.getZoneId()))
        .build();
  }
}
