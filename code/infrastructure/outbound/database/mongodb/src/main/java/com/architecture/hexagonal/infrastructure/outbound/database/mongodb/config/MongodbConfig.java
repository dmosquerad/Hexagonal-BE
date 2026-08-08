package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
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

  @Bean
  public MongoCustomConversions mongoCustomConversions(Clock clock) {
    return new MongoCustomConversions(
        List.of(
            new OffsetDateTimeToDateConverter(clock), new DateToOffsetDateTimeConverter(clock)));
  }

  @WritingConverter
  @RequiredArgsConstructor
  private static class OffsetDateTimeToDateConverter implements Converter<OffsetDateTime, Date> {

    private final Clock clock;

    @Override
    public Date convert(final OffsetDateTime offsetDateTime) {
      return Objects.isNull(offsetDateTime)
          ? null
          : Date.from(offsetDateTime.atZoneSameInstant(clock.getZone()).toInstant());
    }
  }

  @ReadingConverter
  @RequiredArgsConstructor
  private static class DateToOffsetDateTimeConverter implements Converter<Date, OffsetDateTime> {

    private final Clock clock;

    @Override
    public OffsetDateTime convert(final Date date) {
      return Objects.isNull(date)
          ? null
          : date.toInstant().atZone(clock.getZone()).toOffsetDateTime();
    }
  }
}
