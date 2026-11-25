package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.databuilder.data.time;

import com.architecture.hexagonal.domain.databuilder.model.vo.time.TestTime;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.time.DateDao;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

import lombok.Builder;

@Builder
public class DateDaoTestDataBuilder {

  @Builder.Default private Instant date = TestTime.FIXED_INSTANT;

  @Builder.Default private ZoneId zone = ZoneOffset.UTC;

  public DateDao dateDao() {
    return DateDao.builder().date(date).zone(zone).build();
  }
}
