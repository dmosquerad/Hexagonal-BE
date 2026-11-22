package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;
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
  public MongoCustomConversions mongoCustomConversions() {
    return MongoCustomConversions.create(
        adapter -> {
          adapter.registerConverter(new OffsetDateTimeToStringConverter());
          adapter.registerConverter(new StringToOffsetDateTimeConverter());
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
  private static class OffsetDateTimeToStringConverter
      implements Converter<OffsetDateTime, String> {

    @Override
    public String convert(final OffsetDateTime offsetDateTime) {
      return offsetDateTime.toString();
    }
  }

  @ReadingConverter
  private static class StringToOffsetDateTimeConverter
      implements Converter<String, OffsetDateTime> {

    @Override
    public OffsetDateTime convert(final String offsetDateTime) {
      return OffsetDateTime.parse(offsetDateTime);
    }
  }
}
