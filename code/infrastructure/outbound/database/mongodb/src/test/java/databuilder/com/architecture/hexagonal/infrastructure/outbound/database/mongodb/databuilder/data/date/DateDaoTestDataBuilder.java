package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.databuilder.data.date;

import com.architecture.hexagonal.domain.databuilder.model.time.TestClock;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.date.DateDao;
import lombok.Builder;
import java.time.Instant;
import java.time.ZoneId;

@Builder
public class DateDaoTestDataBuilder {

    @Builder.Default private Instant date = TestClock.FIXED_INSTANT;

    @Builder.Default private ZoneId zoneId = TestClock.FIXED_CLOCK.getZone();

    public DateDao dateDao() {
        final DateDao dateDao = new DateDao();
        dateDao.setDate(date);
        dateDao.setZoneId(zoneId.getId());

        return dateDao;
    }

}