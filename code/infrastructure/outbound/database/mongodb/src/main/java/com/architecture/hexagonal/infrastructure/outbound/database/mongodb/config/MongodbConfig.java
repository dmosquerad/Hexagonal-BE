package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.annotation.Id;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertCallback;
import org.springframework.util.ReflectionUtils;

@Configuration
public class MongodbConfig {

  @Bean
  public MongoCustomConversions mongoCustomConversions(final Clock clock) {
    return MongoCustomConversions.create(
        adapter -> {
          adapter.registerConverter(new ZonedDateTimeToDateConverter());
          adapter.registerConverter(new DateToZonedDateTimeConverter(clock));
        });
  }

  @Bean
  public BeforeConvertCallback<Object> uuidGeneratorCallback() {
    return (entity, collection) -> {
      ReflectionUtils.doWithFields(
          entity.getClass(),
          field -> {
            if (field.isAnnotationPresent(Id.class) && UUID.class.equals(field.getType())) {

              ReflectionUtils.makeAccessible(field);

              if (Objects.isNull(field.get(entity))) {
                field.set(entity, UUID.randomUUID());
              }
            }
          });

      return entity;
    };
  }

  @WritingConverter
  private static class ZonedDateTimeToDateConverter implements Converter<ZonedDateTime, Date> {

    @Override
    public Date convert(final ZonedDateTime zonedDateTime) {
      return Date.from(zonedDateTime.toInstant());
    }
  }

  @ReadingConverter
  @RequiredArgsConstructor
  private static class DateToZonedDateTimeConverter implements Converter<Date, ZonedDateTime> {

    private final Clock clock;

    @Override
    public ZonedDateTime convert(final Date date) {
      return date.toInstant().atZone(clock.getZone());
    }
  }
}
