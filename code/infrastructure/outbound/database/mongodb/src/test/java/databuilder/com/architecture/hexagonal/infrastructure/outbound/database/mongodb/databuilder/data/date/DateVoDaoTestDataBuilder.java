package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.databuilder.data.date;

import com.architecture.hexagonal.domain.databuilder.model.time.TestClock;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.date.DateVoDao;
import lombok.Builder;
import java.time.Instant;
import java.time.ZoneId;

@Builder
public class DateVoDaoTestDataBuilder {

    @Builder.Default private Instant date = TestClock.FIXED_INSTANT;

    @Builder.Default private ZoneId zoneId = TestClock.FIXED_CLOCK.getZone();

    public DateVoDao dateVoDao() {
        final DateVoDao dateVoDao = new DateVoDao();
        dateVoDao.setDate(date);
        dateVoDao.setZoneId(zoneId.getId());

        return dateVoDao;
    }

}