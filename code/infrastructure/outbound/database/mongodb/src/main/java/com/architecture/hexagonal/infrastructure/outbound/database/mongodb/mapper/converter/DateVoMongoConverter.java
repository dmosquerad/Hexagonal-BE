package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.converter;

import com.architecture.hexagonal.domain.model.vo.date.DateVo;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.date.DateDao;
import java.time.ZoneId;
import java.util.Objects;
import lombok.experimental.UtilityClass;
import org.mapstruct.Named;

@UtilityClass
public class DateVoMongoConverter {

  public static final String TO_DATE_VO_DAO = "toDateVoDao";
  public static final String TO_DATE_VO = "toDateVo";

  @Named(TO_DATE_VO_DAO)
  public static DateDao toDateVoDao(final DateVo dateVo) {
    if (Objects.isNull(dateVo)) {
      return null;
    }

    final DateDao dateDao = new DateDao();
    dateDao.setDate(dateVo.date());
    dateDao.setZoneId(dateVo.zoneId().getId());
    return dateDao;
  }

  @Named(TO_DATE_VO)
  public static DateVo toDateVo(final DateDao dateDao) {
    if (Objects.isNull(dateDao)) {
      return null;
    }

    return DateVo.builder()
        .date(dateDao.getDate())
        .zoneId(ZoneId.of(dateDao.getZoneId()))
        .build();
  }
}
